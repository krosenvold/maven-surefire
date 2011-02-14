package surefirexyz;

import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.fail;

/**
 * @author Kristian Rosenvold
 */
public class Test2
{
    @Test
    public void testAllok()
    {
        System.out.println( "testAllok to stdout" );
        System.err.println( "testAllok to stderr" );
    }


    @Ignore
    @Test
    public void testWithIgnore1()
    {
    }

    @Ignore
    @Test
    public void test2WithIgnore2()
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
    public void test2WithException1()
    {
        System.out.println( "test2WithException1 to stdout" );
        System.err.println( "test2WithException1 to stderr" );
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test2WithException2()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test2WithException3()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test2WithException4()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test2WithException5()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test2WithException6()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test2WithException7()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test2WithException8()
    {
        throw new RuntimeException( "We expect this" );
    }

}
