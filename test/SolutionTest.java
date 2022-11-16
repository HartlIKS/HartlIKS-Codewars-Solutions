import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class SolutionTest {
    @Test
    public void sampleCases() {
        int[][][][] clues = new int[][][][] {
            {{{1, 1}, {4}, {1, 1, 1}, {3}, {1}},
                {{1}, {2}, {3}, {2, 1}, {4}}},
            {{{1}, {3}, {1}, {3, 1}, {3, 1}},
                {{3}, {2}, {2, 2}, {1}, {1, 2}}},
            {{{2}, {3}, {4}, {1, 1}, {1, 1}},
                {{2}, {3, 1}, {3}, {1, 1}, {2}}}};

        int[][][] answers = new int[][][]{
            {{0, 0, 1, 0, 0},
                {1, 1, 0, 0, 0},
                {0, 1, 1, 1, 0},
                {1, 1, 0, 1, 0},
                {0, 1, 1, 1, 1}},
            {{0, 0, 1, 1, 1},
                {0, 0, 0, 1, 1},
                {1, 1, 0, 1, 1},
                {0, 1, 0, 0, 0},
                {0, 1, 0, 1, 1}},
            {{1, 1, 0, 0, 0},
                {1, 1, 1, 0, 1},
                {0, 1, 1, 1, 0},
                {0, 0, 1, 0, 1},
                {0, 0, 1, 1, 0}}};

        assertArrayEquals(answers[0], Nonogram.solve(clues[0]));
        assertArrayEquals(answers[1], Nonogram.solve(clues[1]));
        assertArrayEquals(answers[2], Nonogram.solve(clues[2]));
    }
}