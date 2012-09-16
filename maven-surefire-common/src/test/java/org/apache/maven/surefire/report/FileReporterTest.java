package org.apache.maven.surefire.report;

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

import junit.framework.TestCase;
import org.apache.maven.plugin.surefire.report.FileReporter;
import org.apache.maven.plugin.surefire.report.ReportEntryType;
import org.apache.maven.plugin.surefire.report.TestSetStats;
import org.apache.maven.plugin.surefire.report.WrappedReportEntry;

import java.io.File;
import java.util.ArrayList;

public class FileReporterTest
    extends TestCase
{

    private FileReporter reporter;

    private ReportEntry reportEntry;

    private static final String testName = "org.apache.maven.surefire.report.FileReporterTest";

    public void testFileNameWithoutSuffix()
    {
        File reportDir = new File( "target" );
        reportEntry = new SimpleReportEntry( this.getClass().getName(), testName );
        WrappedReportEntry wrappedReportEntry =
            new WrappedReportEntry( reportEntry, ReportEntryType.success, 12, null, null );
        reporter = new FileReporter( reportDir, null );
        reporter.testSetCompleted( wrappedReportEntry, createTestSetStats(), new ArrayList<String>() );

        File expectedReportFile = new File( reportDir, testName + ".txt" );
        assertTrue( "Report file (" + expectedReportFile.getAbsolutePath() + ") doesn't exist",
                    expectedReportFile.exists() );
        expectedReportFile.delete();
    }

    private TestSetStats createTestSetStats()
    {
        return new TestSetStats( true, true, true );
    }

    public void testFileNameWithSuffix()
    {
        File reportDir = new File( "target" );
        String suffixText = "sampleSuffixText";
        reportEntry = new SimpleReportEntry( this.getClass().getName(), testName );
        WrappedReportEntry wrappedReportEntry =
            new WrappedReportEntry( reportEntry, ReportEntryType.success, 12, null, null );
        reporter = new FileReporter( reportDir, suffixText );
        reporter.testSetCompleted( wrappedReportEntry, createTestSetStats(), new ArrayList<String>() );

        File expectedReportFile = new File( reportDir, testName + "-" + suffixText + ".txt" );
        assertTrue( "Report file (" + expectedReportFile.getAbsolutePath() + ") doesn't exist",
                    expectedReportFile.exists() );
        expectedReportFile.delete();
    }

}
