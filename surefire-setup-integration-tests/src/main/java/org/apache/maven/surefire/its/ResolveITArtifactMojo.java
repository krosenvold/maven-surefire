package org.apache.maven.surefire.its;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/**
 * Resolves a bunch of poms
 */
@Mojo( name = "resolve", defaultPhase = LifecyclePhase.TEST, threadSafe = true,
       requiresDependencyResolution = ResolutionScope.TEST )
public class ResolveITArtifactMojo
    extends AbstractMojo

{

    @Component
    protected ArtifactResolver artifactResolver;
    @Component
    protected ArtifactFactory artifactFactory;
    @Parameter( defaultValue = "${localRepository}", required = true, readonly = true )
    protected ArtifactRepository localRepository;
    @Parameter( defaultValue = "${project.pluginArtifactRepositories}" )
    protected List<ArtifactRepository> remoteRepositories;
    @Component
    protected ArtifactMetadataSource metadataSource;




    public static final String[] POM_EXTENSIONS = { "xml" };


    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        File root = new File("src/test/resources");
        try
        {
            resolve( root );
        }
        catch ( IOException e )
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch ( XmlPullParserException e )
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch ( ArtifactResolutionException e )
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch ( ArtifactNotFoundException e )
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String repl(Properties properties, String unres){
        if (unres.startsWith( "${" ) && unres.endsWith( "}" )){
            unres = StringUtils.removeStart(unres, "${");
            unres = StringUtils.removeEnd(unres, "}");
            if (properties.containsKey( unres ) ){
                return properties.getProperty( unres );
            } else return unres;
        }
        return unres;
    }

    public void resolve( File root )
        throws IOException, XmlPullParserException, ArtifactResolutionException, ArtifactNotFoundException
    {
        getLog().info( "Looking at " + root.getAbsolutePath() );
        if ( root.isDirectory() )
        {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Collection<File> poms = FileUtils.listFiles( root, POM_EXTENSIONS, true );
            for ( File file : poms )
            {
                if (!"pom.xml".equals( file.getName())){
                    continue;
                }
                getLog().info( "Reading file " + file.getAbsolutePath() );
                FileReader fileReader;
                try
                {
                    fileReader = new FileReader( file );
                    Model model = reader.read( fileReader );

                    List<Dependency> deps = model.getDependencies();
                    Properties properties = model.getProperties();
                    for ( Dependency dep : deps )
                    {
                        Artifact artifact;
                        if (dep.getClassifier() != null){
                            artifact =
                                artifactFactory.createArtifactWithClassifier( dep.getGroupId(), dep.getArtifactId(), repl( properties, dep.getVersion())
                                                                , dep.getType() , dep.getClassifier());

                        } else {
                        artifact =
                            artifactFactory.createArtifact( dep.getGroupId(), dep.getArtifactId(), repl( properties, dep.getVersion()),
                                                            dep.getScope(), dep.getType() );
                        }
                          getLog().info( "Resolving " + artifact);
                        try {
                            artifactResolver.resolve( artifact, remoteRepositories, localRepository );
                        }   catch (ArtifactNotFoundException e){
                            getLog().warn( "Could not resolve " + artifact );
                        }


                    }
                }
                catch ( IOException e )
                {
                    if ( getLog() != null )
                    {
                        getLog().warn( "Could not read from " + file, e );
                    }
                }
                catch ( XmlPullParserException e )
                {
                    if ( getLog()!= null )
                    {
                        getLog().warn( "Could not parse " + file, e );
                    }
                }
            }
        }
    }

}
