import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Nonogram {
    private static int width;
    private static int height;

    private record Point(int x, int y) {
        public static final Point ORIGIN = new Point(0, 0);

        public Stream<Point> row() {
            return IntStream.range(0, width)
                .mapToObj(this::row);
        }

        public Point row(int x) {
            return new Point(x, y);
        }

        public Stream<Point> col() {
            return IntStream.range(0, height)
                .mapToObj(this::col);
        }

        public Point col(int y) {
            return new Point(x, y);
        }

        public <T> IntFunction<T> alterRow(final Function<? super Point, T> original, final T replacement) {
            return i -> i == x ? replacement : original.apply(row(i));
        }

        public <T> IntFunction<T> alterCol(final Function<? super Point, T> original, final T replacement) {
            return i -> i == y ? replacement : original.apply(col(i));
        }
    }

    private enum Options {
        TRUE, FALSE;

        public int toInt() {
            return switch (this) {
                case TRUE -> 1;
                case FALSE -> 0;
            };
        }

    }

    private interface IBRex {
        IBRex TERMINATOR = new IBRex() {
            @Override
            public boolean matches(IntFunction<EnumSet<Options>> input, int length) {
                return length == 0;
            }

            @Override
            public String toString() {
                return "$";
            }
        };
        boolean matches(IntFunction<EnumSet<Options>> input, int length);
    }

    private record BRex(Options val, boolean optional, boolean repeatable, IBRex next) implements IBRex {
        public static BRex parse(int[] values, int i) {
            if (values.length == i) return new BRex(Options.FALSE, true, true, IBRex.TERMINATOR);
            else {
                BRex ret = parse(values, i + 1);
                for (int j = 0; j < values[i]; j++) {
                    ret = new BRex(Options.TRUE, false, false, ret);
                }
                return new BRex(Options.FALSE, i == 0, true, ret);
            }
        }

        @Override
        public String toString() {
            return String.format("[%s]%s%s", val, switch ((optional ? 1 : 0) + (repeatable ? 2 : 0)) {
                case 0 -> "";
                case 1 -> "?";
                case 2 -> "+";
                case 3 -> "*";
                default -> "@";
            }, Optional.ofNullable(next).map(IBRex::toString).orElse(""));
        }

        public boolean matches(IntFunction<EnumSet<Options>> input, int length) {
            if (length == 0) {
                if (!optional) return false;
                return next.matches(input, length);
            } else {
                if (optional && next.matches(input, length)) return true;
                if (!input.apply(0).contains(val)) return false;
                final IntFunction<EnumSet<Options>> nextInput = i -> input.apply(i + 1);
                if (next.matches(nextInput, length - 1)) return true;
                return repeatable && matches(nextInput, length - 1);
            }
        }
    }

    private record Clue(BRex brex, boolean col) {
        public Clue(int[] clues, boolean col) {
            this(BRex.parse(clues, 0), col);
        }

        public static Clue col(int[] clues) {
            return new Clue(clues, true);
        }

        public static Clue row(int[] clues) {
            return new Clue(clues, false);
        }

        public boolean canHaveValue(Function<Point, EnumSet<Options>> board, Point point, Options value) {
            return brex.matches(
                col ?
                    point.alterCol(board, EnumSet.of(value)) :
                    point.alterRow(board, EnumSet.of(value)),
                col ? height : width
            );
        }
    }

    public static synchronized int[][] solve(final int[][][] clues) {
        final Clue[] colClues = Arrays.stream(clues[0])
            .map(Clue::col)
            .toArray(Clue[]::new);
        width = colClues.length;
        final Clue[] rowClues = Arrays.stream(clues[1])
            .map(Clue::row)
            .toArray(Clue[]::new);
        height = rowClues.length;
        final Map<Point, EnumSet<Options>> board = new HashMap<>(width * height);
        final Function<Point, EnumSet<Options>> boardAccess = p -> board.computeIfAbsent(
            p,
            p2 -> EnumSet.allOf(Options.class)
        );
        final Collection<Point> points = Point.ORIGIN.col()
            .flatMap(Point::row)
            .toList();
        while (true) {
            boolean delta = false;
            for (Point p : points) {
                final List<Clue> cellClues = List.of(
                    colClues[p.x()],
                    rowClues[p.y()]
                );
                EnumSet<Options> options = boardAccess.apply(p);
                if (options.removeIf(o ->
                    !cellClues.stream().allMatch(clue -> clue.canHaveValue(boardAccess, p, o))
                )) delta = true;
            }
            if (!delta) break;
        }
        return Point.ORIGIN.col()
            .map(Point::row)
            .map(s -> s.map(boardAccess)
                .flatMap(EnumSet::stream)
                .mapToInt(Options::toInt)
                .toArray()
            )
            .toArray(int[][]::new);

    }

    // Just to make 15x15 Kata work

    private final int[][][] clues;

    public Nonogram(int[][][] clues) {
        this.clues = clues;
    }

    public int[][] solve() {
        return solve(clues);
    }
}