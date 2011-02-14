package surefirexyz;

import org.junit.Test;

/**
 * @author Kristian Rosenvold
 */
public class Test6
{
    @Test
    public void test6WithException1()
    {
        System.out.println( "test6WithException1 to stdout" );
        System.err.println( "test6WithException1 to stderr" );
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test6WithException2()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test6WithException3()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test6WithException4()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test6WithException5()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test6WithException6()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test6WithException7()
    {
        throw new RuntimeException( "We expect this" );
    }

    @Test
    public void test6WithException8()
    {
        throw new RuntimeException( "We expect this" );
    }

}
