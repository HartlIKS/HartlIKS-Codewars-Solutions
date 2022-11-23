import java.math.BigInteger;
import java.util.*;

public class Fibonacci {
    public record AB(BigInteger a, BigInteger b) {
        public static final AB ZERO = new AB(BigInteger.ZERO, BigInteger.ONE);
        public static final AB ONE = new AB(BigInteger.ONE, BigInteger.ZERO);
        public static final AB NEG_ONE = new AB(BigInteger.ONE, BigInteger.ONE.negate());

        public AB n2() {
            return add(this);
        }

        public AB add(AB other) {
            final BigInteger g = other.a;
            final BigInteger h = other.b;
            return new AB(a.multiply(g.add(h)).add(b.multiply(g)), a.multiply(g).add(b.multiply(h)));
        }

        public AB multiply(BigInteger n) {
            if(n.signum() < 0) throw new IllegalArgumentException("Only non-negative factors are supported");
            AB ret = ZERO;
            AB add = this;
            while(n.signum() > 0) {
                if(n.testBit(0)) {
                    ret = ret.add(add);
                }
                add = add.n2();
                n = n.shiftRight(1);
            }
            return ret;
        }
    }

    private static final NavigableMap<BigInteger, AB> preCalc = new TreeMap<>();

    public static BigInteger fib(final BigInteger on) {
        final NavigableMap<BigInteger, AB> preMap = switch(on.signum()) {
            case 1 -> preCalc.tailMap(BigInteger.ZERO, false).descendingMap();
            case -1 -> preCalc.headMap(BigInteger.ZERO, false);
            default -> Collections.emptyNavigableMap();
        };
        AB ret = AB.ZERO;
        BigInteger n = on;
        Map<BigInteger, AB> newAB = new LinkedHashMap<>();
        for(Map.Entry<BigInteger, AB> e: preMap.entrySet()) {
            if(n.signum() == 0) break;
            BigInteger d = n.divide(e.getKey());
            if(d.signum() > 0) {
                final AB ab = e.getValue().multiply(d);
                if(!d.equals(BigInteger.ONE)) {
                    newAB.put(e.getKey().multiply(d), ab);
                }
                ret = ret.add(ab);
                n = n.subtract(d.multiply(e.getKey()));
            }
        }
        if(n.signum() != 0) {
            final AB ab = (n.signum() > 0 ? AB.ONE : AB.NEG_ONE).multiply(n.abs());
            newAB.put(n, ab);
            ret = ret.add(ab);
        }
        preCalc.putAll(newAB);
        return ret.a();
    }
}