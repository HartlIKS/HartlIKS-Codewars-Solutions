import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeneralizedFibonacciTest {

    @Test
    public void Test1SmallInputs() {
        byte[] array11 = {0, 1};
        byte[] array12 = {1, 1};
        assertEquals(new BigInteger("0"), GeneralizedFibonacci.Get(array11, array12, 0), "0");
        assertEquals(new BigInteger("1"), GeneralizedFibonacci.Get(array11, array12, 1), "1");
        assertEquals(new BigInteger("1"), GeneralizedFibonacci.Get(array11, array12, 2), "2");
        assertEquals(new BigInteger("2"), GeneralizedFibonacci.Get(array11, array12, 3), "3");
        byte[] array21 = {1, 1, 1};
        byte[] array22 = {0, 1, 1};
        assertEquals(new BigInteger("1"), GeneralizedFibonacci.Get(array21, array22, 0));
        assertEquals(new BigInteger("1"), GeneralizedFibonacci.Get(array21, array22, 1));
        assertEquals(new BigInteger("1"), GeneralizedFibonacci.Get(array21, array22, 2));
        assertEquals(new BigInteger("2"), GeneralizedFibonacci.Get(array21, array22, 3));
        assertEquals(new BigInteger("2"), GeneralizedFibonacci.Get(array21, array22, 4));
        assertEquals(new BigInteger("3"), GeneralizedFibonacci.Get(array21, array22, 5));
        assertEquals(new BigInteger("4"), GeneralizedFibonacci.Get(array21, array22, 6));
        assertEquals(new BigInteger("12"), GeneralizedFibonacci.Get(array21, array22, 10));
        byte[] array31 = {0, 0, 1};
        byte[] array32 = {1, 1, 1};
        assertEquals(new BigInteger("274"), GeneralizedFibonacci.Get(array31, array32, 12));
        byte[] array41 = {1, 1, 1};
        byte[] array42 = {0, 1, 1};
        assertEquals(new BigInteger("265"), GeneralizedFibonacci.Get(array41, array42, 21));
    }

    @Test
    public void Test2BiggerInputs() {
        byte[] array51 = {2, -1, -1};
        byte[] array52 = {1, 2, 2};
        assertEquals(new BigInteger("-207"), GeneralizedFibonacci.Get(array51, array52, 10));
        byte[] array61 = {2, 0, -1};
        byte[] array62 = {2, 1, 2};
        assertEquals(new BigInteger("2325123"), GeneralizedFibonacci.Get(array61, array62, 18));
        byte[] array71 = {-1, 3, -2};
        byte[] array72 = {2, 2, -2};
        assertEquals(new BigInteger("195328"), GeneralizedFibonacci.Get(array71, array72, 17));
        byte[] array81 = {-5, 0, 4};
        byte[] array82 = {-1, 2, -1};
        assertEquals(new BigInteger("-9593024"), GeneralizedFibonacci.Get(array81, array82, 23));
    }
}