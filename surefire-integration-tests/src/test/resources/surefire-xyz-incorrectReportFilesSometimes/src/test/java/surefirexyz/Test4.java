package surefirexyz;

import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.fail;

/**
 * @author Kristian Rosenvold
 */
public class Test4
{
    @Test
    public void testAllok()
    {
        System.out.println( "testAllok to stdout" );
        System.err.println( "testAllok to stderr" );
    }


    @Ignore
    @Test
    public void test4WithIgnore1()
    {
    }

    @Ignore
    @Test
    public void test4WithIgnore2()
    {
    }

    @Test
    public void testiWithFail1()
    {
        fail( "We excpect this" );
    }

    @Test
    public void testiWithFail2()
    {
        fail( "We excpect this" );
    }

    @Test
    public void testiWithFail3()
    {
        fail( "We excpect this" );
    }

    @Test
    public void testiWithFail4()
    {
        fail( "We excpect this" );
    }

    @Test
    public void test4WithException1()
    {
        System.out.println( "test4WithException1 to stdout" );
        System.err.println( "test4WithException1 to stderr" );
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test4WithException2()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test4WithException3()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test4WithException4()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test4WithException5()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test4WithException6()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test4WithException7()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test4WithException8()
    {
        throw new RuntimeException( "We expect this" );
    }

}
