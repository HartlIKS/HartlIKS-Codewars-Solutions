import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class SolutionTest {
    @Test
    public void sampleCase1() {
        int[][][] clues = {
            {{1, 1}, {4}, {1, 1, 1}, {3}, {1}},
            {{1}, {2}, {3}, {2, 1}, {4}}
        };

        int[][] answers = {
            {0, 0, 1, 0, 0},
            {1, 1, 0, 0, 0},
            {0, 1, 1, 1, 0},
            {1, 1, 0, 1, 0},
            {0, 1, 1, 1, 1}
        };

        assertArrayEquals(answers, Nonogram.solve(clues));
    }

    @Test
    public void sampleCase2() {
        int[][][] clues = {
            {{1}, {3}, {1}, {3, 1}, {3, 1}},
            {{3}, {2}, {2, 2}, {1}, {1, 2}}
        };

        int[][] answers = {
            {0, 0, 1, 1, 1},
            {0, 0, 0, 1, 1},
            {1, 1, 0, 1, 1},
            {0, 1, 0, 0, 0},
            {0, 1, 0, 1, 1}
        };

        assertArrayEquals(answers, Nonogram.solve(clues));
    }

    @Test
    public void sampleCase3() {
        int[][][] clues = {
            {{2}, {3}, {4}, {1, 1}, {1, 1}},
            {{2}, {3, 1}, {3}, {1, 1}, {2}}
        };

        int[][] answers = {
            {1, 1, 0, 0, 0},
            {1, 1, 1, 0, 1},
            {0, 1, 1, 1, 0},
            {0, 0, 1, 0, 1},
            {0, 0, 1, 1, 0}
        };

        assertArrayEquals(answers, Nonogram.solve(clues));
    }
}