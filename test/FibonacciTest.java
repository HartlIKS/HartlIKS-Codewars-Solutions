import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FibonacciTest {

    @Test
    public void testFib0() {
        testFib(0, 0);
    }

    @Test
    public void testFib1() {
        testFib(1, 1);
    }

    @Test
    public void testFib2() {
        testFib(1, 2);
    }

    @Test
    public void testFib3() {
        testFib(2, 3);
    }

    @Test
    public void testFib4() {
        testFib(3, 4);
    }

    @Test
    public void testFib5() {
        testFib(5, 5);
    }

    @Test
    public void testFibN1() {
        testFib(1, -1);
    }

    @Test
    public void testFibN2() {
        testFib(-1, -2);
    }

    @Test
    public void testFibN3() {
        testFib(2, -3);
    }

    private static void testFib(long expected, long input) {
        BigInteger found;
        try {
            found = Fibonacci.fib(BigInteger.valueOf(input));
        }
        catch (AssertionError e) {
            throw e;
        }
        catch (Throwable e) {
            // see https://github.com/Codewars/codewars.com/issues/21
            throw new AssertionError("exception during test: "+e, e);
        }
        assertEquals(BigInteger.valueOf(expected), found);
    }

    @Test
    public void ab2() {
        assertEquals(new Fibonacci.AB(BigInteger.ONE, BigInteger.ONE), Fibonacci.AB.ONE.n2());
    }

    @Test
    public void ab3() {
        assertEquals(new Fibonacci.AB(BigInteger.TWO, BigInteger.ONE), Fibonacci.AB.ONE.n2().add(Fibonacci.AB.ONE));
    }

    @Test
    public void ab3m() {
        assertEquals(new Fibonacci.AB(BigInteger.TWO, BigInteger.ONE), Fibonacci.AB.ONE.multiply(BigInteger.valueOf(3)));
    }

    @Test
    public void ab0() {
        assertEquals(Fibonacci.AB.ONE, Fibonacci.AB.ZERO.add(Fibonacci.AB.ONE));
    }

    @Test
    public void ab0a() {
        assertEquals(Fibonacci.AB.ONE, Fibonacci.AB.ONE.add(Fibonacci.AB.ZERO));
    }
}