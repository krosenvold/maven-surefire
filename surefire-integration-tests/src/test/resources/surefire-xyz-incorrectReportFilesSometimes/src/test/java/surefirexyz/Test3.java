package surefirexyz;

import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.fail;

/**
 * @author Kristian Rosenvold
 */
public class Test3
{
    @Test
    public void testAllok()
    {
        System.out.println( "testAllok to stdout" );
        System.err.println( "testAllok to stderr" );
    }


    @Ignore
    @Test
    public void test3WithIgnore1()
    {
    }

    @Ignore
    @Test
    public void test3WithIgnore2()
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
    public void test3WithException1()
    {
        System.out.println( "test3WithException1 to stdout" );
        System.err.println( "test3WithException1 to stderr" );
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test3WithException2()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test3WithException3()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test3WithException4()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test3WithException5()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test3WithException6()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test3WithException7()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test3WithException8()
    {
        throw new RuntimeException( "We expect this" );
    }

}
