package failureresultcounting;

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

import junit.framework.TestResult;
import junit.framework.TestSuite;

public class RunTests
{
    public static void main(String args[]) {
        TestSuite testSuite = new TestSuite(  );
        testSuite.addTest(  new BeforeClassError() );
        testSuite.addTest( new BeforeClassFailure() );
        testSuite.addTest( new BeforeError() );
        testSuite.addTest( new BeforeFailure() );
        testSuite.addTest( new OrdinaryError() );
        testSuite.addTest( new NoErrors() );
        TestResult testResult = new TestResult();
        testSuite.run(  testResult);
        System.out.println(testResult );
        /*     org.junit.runner.JUnitCore.main(BeforeClassError.class.getName(),
        BeforeClassFailure.class.getName(),
        BeforeError.class.getName(),
        BeforeFailure.class.getName(),
        OrdinaryError.class.getName(),
        NoErrors.class.getName()
        );*/
    }
}
