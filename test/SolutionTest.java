import org.junit.jupiter.api.Test;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class SolutionTest {
    @Test
    public void test() {
        for (int d = 1; d <= 18; d++) {
            Predicate<String> matcher = Pattern.compile(Solution.regexDivisibleBy(d)).asMatchPredicate();
            for (int i = 0; i < 30; i++) {
                if (i % d == 0) assertTrue(matcher.test(Integer.toBinaryString(i)), i + " is divisible by " + d);
                else assertFalse(matcher.test(Integer.toBinaryString(i)), i + " is not divisible by " + d);
            }
            System.err.printf("Mod %d tested%n", d);
        }
    }
}
