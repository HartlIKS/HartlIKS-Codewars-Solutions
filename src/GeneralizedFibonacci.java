import java.math.BigInteger;
import java.util.Arrays;

public class GeneralizedFibonacci {
    private static BigInteger[][] squareMatrix(final BigInteger[][] mat) {
        final BigInteger[][] ret = new BigInteger[mat.length][mat.length];
        for (int i = 0; i < mat.length; i++) {
            for (int k = 0; k < mat.length; k++) {
                ret[i][k] = BigInteger.ZERO;
                for (int j = 0; j < mat.length; j++) {
                    ret[i][k] = mat[i][j].multiply(mat[j][k]).add(ret[i][k]);
                }
            }
        }
        return ret;
    }

    private static BigInteger[] vectorMul(final BigInteger[][] mat, final BigInteger[] vector) {
        final BigInteger[] ret = new BigInteger[mat.length];
        for (int i = 0; i < mat.length; i++) {
            ret[i] = BigInteger.ZERO;
            for (int j = 0; j < vector.length; j++) {
                ret[i] = mat[i][j].multiply(vector[j]).add(ret[i]);
            }
        }
        return ret;
    }

    public static BigInteger Get(final byte[] a, final byte[] b, long power) {
        BigInteger[][] baseMatrix = new BigInteger[b.length][b.length];
        for(BigInteger[] row:baseMatrix) Arrays.fill(row, BigInteger.ZERO);
        for (int i = 0; i < b.length; i++) {
            baseMatrix[b.length-1][i] = BigInteger.valueOf(b[b.length-1-i]);
        }
        for(int i=1; i<b.length;i++) {
            baseMatrix[i-1][i] = BigInteger.ONE;
        }
        BigInteger[] baseVector = new BigInteger[a.length];
        Arrays.setAll(baseVector, i -> BigInteger.valueOf(a[i]));
        while(power > 0) {
            if((power & 1) == 1) {
                baseVector = vectorMul(baseMatrix, baseVector);
            }
            baseMatrix = squareMatrix(baseMatrix);
            power >>= 1;
        }
        return baseVector[0];
    }
}
