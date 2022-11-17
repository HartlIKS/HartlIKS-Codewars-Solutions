import java.util.Collections;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

class HowManyNumbers {
    private static long pow10(long exp) {
        long ret = 1;
        for (; exp > 0; exp--) {
            ret *= 10;
        }
        return ret;
    }

    private static LongStream findAll(final int sumDigits, final int numDigits, final int minDigit) {
        if (numDigits == 1 && sumDigits < 10 && minDigit <= sumDigits) return LongStream.of(sumDigits);
        else if (numDigits <= 1) return LongStream.empty();
        else if (sumDigits < minDigit * numDigits) return LongStream.empty();
        else if (sumDigits > 9 * numDigits) return LongStream.empty();
        final long dpow = pow10(numDigits - 1);
        return IntStream.rangeClosed(minDigit, Math.max(9, sumDigits))
            .mapToObj(d ->
                findAll(sumDigits - d, numDigits - 1, d)
                    .map(l -> l + d * dpow)
            )
            .flatMapToLong(Function.identity());
    }

    public static List<Long> findAll(final int sumDigits, final int numDigits) {
        LongSummaryStatistics summary = findAll(sumDigits, numDigits, 1).summaryStatistics();
        if(summary.getCount() == 0) return Collections.emptyList();
        return List.of(summary.getCount(), summary.getMin(), summary.getMax());
    }
}