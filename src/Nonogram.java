import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Nonogram {
    public static final int WIDTH = 5;
    public static final int HEIGHT = 5;

    private record Point(int x, int y) {
        public static final Point ORIGIN = new Point(0, 0);

        public Stream<Point> row() {
            return IntStream.range(0, WIDTH)
                .mapToObj(this::row);
        }

        public Point row(int x) {
            return new Point(x, y);
        }

        public Stream<Point> col() {
            return IntStream.range(0, HEIGHT)
                .mapToObj(this::col);
        }

        public Point col(int y) {
            return new Point(x, y);
        }

        public <T> IntFunction<T> alterRow(final Function<? super Point, T> original, final T replacement) {
            return i -> i == x ? replacement : original.apply(row(i));
        }

        public <T> IntFunction<T> alterCol(final Function<? super Point, T> original, final T replacement) {
            return i -> i == x ? replacement : original.apply(col(i));
        }
    }

    private enum Options {
        TRUE, FALSE;

        public Options negate() {
            return switch (this) {
                case TRUE -> FALSE;
                case FALSE -> TRUE;
            };
        }

        public int toInt() {
            return switch (this) {
                case TRUE -> 1;
                case FALSE -> 0;
            };
        }

    }

    private record BRex(Options val, boolean optional, boolean repeatable, BRex next) {
        public static BRex parse(int[] values, int i) {
            if (values.length == i) return new BRex(Options.FALSE, true, true, null);
            else {
                BRex ret = parse(values, i + 1);
                for (int j = 0; j < values[i]; j++) {
                    ret = new BRex(Options.TRUE, false, false, ret);
                }
                return new BRex(Options.FALSE, i == 0, true, ret);
            }
        }

        private boolean acceptsEmpty() {
            return optional && (next == null || next.acceptsEmpty());
        }

        @Override
        public String toString() {
            return String.format("[%s]%s%s", val, switch ((optional ? 1 : 0) + (repeatable ? 2 : 0)) {
                case 0 -> "";
                case 1 -> "?";
                case 2 -> "+";
                case 3 -> "*";
                default -> "@";
            }, Optional.ofNullable(next).map(BRex::toString).orElse(""));
        }

        private void debugPrint(IntFunction<EnumSet<Options>> input, int length) {
            System.err.printf("%s testing:%n", this);
            for(int i=0;i<length;i++) {
                System.err.printf("%s ", input.apply(i));
            }
            System.err.println();
        }

        public boolean matches(IntFunction<EnumSet<Options>> input, int length) {
            if(length == 0) {
                if(!optional) {
                    debugPrint(input, length);
                    System.err.println("Rejected by non-optional");
                    return false;
                }
                return next == null || next.matches(input, length);
            } else {
                if(optional && next != null && next.matches(input, length)) {
                    debugPrint(input, length);
                    System.err.println("Accepted optional by child match");
                    return true;
                }
                if(!input.apply(0).contains(val)) {
                    debugPrint(input, length);
                    System.err.println("Rejected by value mismatch");
                    return false;
                }
                if(next == null && length == 1) {
                    debugPrint(input, length);
                    System.err.println("Accepted end by value match");
                    return true;
                }
                final IntFunction nextInput = i -> input.apply(i-1);
                if(next != null && next.matches(nextInput, length-1)) {
                    debugPrint(input, length);
                    System.err.println("Accepted by child match");
                    return true;
                }
                if(repeatable && matches(nextInput, length-1)) {
                    debugPrint(input, length);
                    System.err.println("Accepted by repeat match");
                    return true;
                }
                debugPrint(input, length);
                System.err.println("Rejected by lack of child matches");
                return false;
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
                col ? HEIGHT : WIDTH
            );
        }
    }

    public static int[][] solve(final int[][][] clues) {
        final Clue[] colClues = Arrays.stream(clues[0])
            .map(Clue::col)
            .toArray(Clue[]::new);
        final Clue[] rowClues = Arrays.stream(clues[1])
            .map(Clue::row)
            .toArray(Clue[]::new);
        final Map<Point, EnumSet<Options>> board = new HashMap<>(WIDTH * HEIGHT);
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
        Point.ORIGIN.col()
            .forEach(p -> {
                p.row()
                    .map(boardAccess)
                    .forEach(opt -> System.err.printf("%s ", opt));
                System.err.println();
            });
        return Point.ORIGIN.col()
            .map(Point::row)
            .map(s -> s.map(boardAccess)
                .flatMap(EnumSet::stream)
                .mapToInt(Options::toInt)
                .toArray()
            )
            .toArray(int[][]::new);

    }

}