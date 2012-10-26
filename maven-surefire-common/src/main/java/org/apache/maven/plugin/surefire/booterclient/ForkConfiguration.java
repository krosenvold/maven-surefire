package org.apache.maven.plugin.surefire.booterclient;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.plugin.surefire.util.Relocator;
import org.apache.maven.surefire.booter.ClassLoaderConfiguration;
import org.apache.maven.surefire.booter.Classpath;
import org.apache.maven.surefire.booter.ForkedBooter;
import org.apache.maven.surefire.booter.SurefireBooterForkException;
import org.apache.maven.surefire.util.UrlUtils;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * Configuration for forking tests.
 *
 * @author <a href="mailto:brett@apache.org">Brett Porter</a>
 * @author <a href="mailto:kenney@apache.org">Kenney Westerhof</a>
 * @author <a href="mailto:krosenvold@apache.org">Kristian Rosenvold</a>
 */
public class ForkConfiguration
{
    public static final String FORK_ONCE = "once";

    public static final String FORK_ALWAYS = "always";

    public static final String FORK_NEVER = "never";

    public static final String FORK_PERTHREAD = "perthread";

    private final int forkCount;

    private final Future<Classpath> bootClasspathConfiguration;

    private final String jvmExecutable;

    private final String argLine;

    private final Map<String, String> environmentVariables;

    private final File workingDirectory;

    private final File tempDirectory;

    private final boolean debug;

    private final String debugLine;

    public ForkConfiguration( Future<Classpath> bootClasspathConfiguration, File tmpDir, String debugLine, String jvmExecutable,
                              File workingDirectory, String argLine, Map<String, String> environmentVariables,
                              boolean debugEnabled, int forkCount )
    {
        this.bootClasspathConfiguration = bootClasspathConfiguration;
        this.tempDirectory = tmpDir;
        this.debugLine = debugLine;
        this.jvmExecutable = jvmExecutable;
        this.workingDirectory = workingDirectory;
        this.argLine = argLine;
        this.environmentVariables = environmentVariables;
        this.debug = debugEnabled;
        this.forkCount = forkCount;
    }

    public Classpath getBootClasspath() throws SurefireBooterForkException {
        try {
            return bootClasspathConfiguration.get();
        } catch (InterruptedException e) {
            throw new  SurefireBooterForkException("Interrupted in asynch artifact resolution", e);
        } catch (ExecutionException e) {
            throw new  SurefireBooterForkException("ExecutedExecption in asynch artifact resolution", e);
        }
    }

    public static String getEffectiveForkMode(String forkMode)
    {
        if ( "pertest".equalsIgnoreCase( forkMode ) )
        {
            return FORK_ALWAYS;
        }
        else if ( "none".equalsIgnoreCase( forkMode ) )
        {
            return FORK_NEVER;
        }
        else if ( forkMode.equals( FORK_NEVER ) || forkMode.equals( FORK_ONCE ) ||
            forkMode.equals( FORK_ALWAYS ) || forkMode.equals( FORK_PERTHREAD ) )
        {
            return forkMode;
        }
        else
        {
            throw new IllegalArgumentException( "Fork mode " + forkMode + " is not a legal value" );
        }
    }


    /**
     * @param classPath              cla the classpath arguments
     * @param classpathConfiguration the classpath configuration
     * @param shadefire              true if running shadefire
     * @return A commandline
     * @throws org.apache.maven.surefire.booter.SurefireBooterForkException
     *          when unable to perform the fork
     */
    public Commandline createCommandLine( List<String> classPath, ClassLoaderConfiguration classpathConfiguration,
                                          boolean shadefire )
        throws SurefireBooterForkException
    {
        return createCommandLine( classPath, classpathConfiguration.isManifestOnlyJarRequestedAndUsable(), shadefire );
    }

    public Commandline createCommandLine( List<String> classPath, boolean useJar, boolean shadefire )
        throws SurefireBooterForkException
    {
        Commandline cli = new Commandline();

        cli.setExecutable( jvmExecutable );

        if ( argLine != null )
        {
            cli.createArg().setLine( stripNewLines( argLine ) );
        }

        if ( environmentVariables != null )
        {

            for ( String key : environmentVariables.keySet() )
            {
                String value = environmentVariables.get( key );

                cli.addEnvironment( key, value );
            }
        }

        if ( getDebugLine() != null && !"".equals( getDebugLine() ) )
        {
            cli.createArg().setLine( getDebugLine() );
        }

        if ( useJar )
        {
            File jarFile;
            try
            {
                jarFile = createJar( classPath );
            }
            catch ( IOException e )
            {
                throw new SurefireBooterForkException( "Error creating archive file", e );
            }

            cli.createArg().setValue( "-jar" );

            cli.createArg().setValue( jarFile.getAbsolutePath() );
        }
        else
        {
            cli.addEnvironment( "CLASSPATH", StringUtils.join( classPath.iterator(), File.pathSeparator ) );

            final String forkedBooter = ForkedBooter.class.getName();

            cli.createArg().setValue( shadefire ? new Relocator().relocate( forkedBooter ) : forkedBooter );
        }

        cli.setWorkingDirectory( workingDirectory.getAbsolutePath() );

        return cli;
    }

    /**
     * Create a jar with just a manifest containing a Main-Class entry for BooterConfiguration and a Class-Path entry
     * for all classpath elements.
     *
     * @param classPath List&lt;String> of all classpath elements.
     * @return The file pointint to the jar
     * @throws java.io.IOException When a file operation fails.
     */
    public File createJar( List<String> classPath )
        throws IOException
    {
        File file = File.createTempFile( "surefirebooter", ".jar", tempDirectory );
        if ( !debug )
        {
            file.deleteOnExit();
        }
        FileOutputStream fos = new FileOutputStream( file );
        JarOutputStream jos = new JarOutputStream( fos );
        jos.setLevel( JarOutputStream.STORED );
        JarEntry je = new JarEntry( "META-INF/MANIFEST.MF" );
        jos.putNextEntry( je );

        Manifest man = new Manifest();

        // we can't use StringUtils.join here since we need to add a '/' to
        // the end of directory entries - otherwise the jvm will ignore them.
        String cp = "";
        for ( String el : classPath )
        {
            // NOTE: if File points to a directory, this entry MUST end in '/'.
            cp += UrlUtils.getURL( new File( el ) ).toExternalForm() + " ";
        }

        man.getMainAttributes().putValue( "Manifest-Version", "1.0" );
        man.getMainAttributes().putValue( "Class-Path", cp.trim() );
        man.getMainAttributes().putValue( "Main-Class", ForkedBooter.class.getName() );

        man.write( jos );
        jos.close();

        return file;
    }

    public boolean isDebug()
    {
        return debug;
    }

    public String stripNewLines( String argline )
    {
        return argline.replace( "\n", " " ).replace( "\r", " " );
    }

    public String getDebugLine()
    {
        return debugLine;
    }

    public File getTempDirectory()
    {
        return tempDirectory;
    }

    public int getForkCount()
    {
        return forkCount;
    }
}
