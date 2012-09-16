package org.apache.maven.surefire.its;

import org.apache.maven.surefire.its.fixture.SurefireJUnit4IntegrationTestCase;

import org.junit.Test;

/**
 *
 * Tests differnt
 */
public class FailureResultCountingIT extends SurefireJUnit4IntegrationTestCase
{
    @Test
    public void jUnit4Counting(){
        unpack( "failure-result-counting" ).executeTestWithFailure().assertTestSuiteResults( 14, 5,5,2 );
    }

    @Test
    public void jUnit3xCounting(){
        unpack( "failure-result-counting" ).setJUnitVersion( "3.8.1" ).executeTestWithFailure().assertTestSuiteResults( 14, 6,6,2 );
    }
}
