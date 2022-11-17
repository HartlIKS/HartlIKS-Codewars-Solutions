import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.function.ObjIntConsumer;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class Paintfuck {
    private record Point(int x, int y) {
        public Point move(int dx, int dy) {
            return new Point(x + dx, y + dy);
        }

        public int mx(int div) {
            int ret = x % div;
            if (ret < 0) ret += div;
            return ret;
        }

        public int my(int div) {
            int ret = y % div;
            if (ret < 0) ret += div;
            return ret;
        }
    }

    private record State(Point head, long it) {
        public State next(Point pos) {
            return new State(pos, it + 1);
        }

        public State next() {
            return next(head);
        }
    }

    private interface Op {
        Op FLIP = (r, w, s, lim) -> {
            if (s.it() >= lim) return s;
            w.accept(s.head(), 1 - r.applyAsInt(s.head()));
            return s.next();
        };

        State execute(ToIntFunction<Point> read, ObjIntConsumer<Point> write, State state, long limit);
    }

    private enum MoveOp implements Op {
        N(0, -1), E(1, 0), S(0, 1), W(-1, 0);
        public final int dx;
        public final int dy;

        MoveOp(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        @Override
        public State execute(ToIntFunction<Point> read, ObjIntConsumer<Point> write, State state, long limit) {
            if (state.it() >= limit) return state;
            return state.next(state.head().move(dx, dy));
        }
    }

    private record BlockOp(List<? extends Op> ops) implements Op {
        public State execute(ToIntFunction<Point> read, ObjIntConsumer<Point> write, State state, long limit) {
            for (Op op : ops) {
                if (state.it() >= limit) break;
                state = op.execute(read, write, state, limit);
            }
            return state;
        }
    }

    private record LoopOp(Op body) implements Op {
        @Override
        public State execute(ToIntFunction<Point> read, ObjIntConsumer<Point> write, State state, long limit) {
            if (state.it() >= limit) return state;
            for (state = state.next(); read.applyAsInt(state.head()) != 0; state = state.next()) {
                if (state.it() >= limit) return state;
                state = body.execute(read, write, state, limit);
                if (state.it() >= limit) return state;
            }
            return state;
        }
    }

    private static List<? extends Op> parse(PrimitiveIterator.OfInt source, boolean sub) {
        List<Op> ret = new LinkedList<>();
        while (source.hasNext()) {
            int c = source.nextInt();
            switch (c) {
            case 'n' -> ret.add(MoveOp.N);
            case 'e' -> ret.add(MoveOp.E);
            case 's' -> ret.add(MoveOp.S);
            case 'w' -> ret.add(MoveOp.W);
            case '*' -> ret.add(Op.FLIP);
            case '[' -> ret.add(new LoopOp(new BlockOp(parse(source, true))));
            case ']' -> {
                if (!sub) throw new IllegalArgumentException("Unopened ]");
                return ret;
            }
            default -> {
                //Do Nothing
            }
            }
        }
        if (sub) throw new IllegalArgumentException("Unclosed [");
        return ret;
    }

    public static String interpreter(final String code, final int iterations, final int width, final int height) {
        final BlockOp ops = new BlockOp(parse(code.codePoints().iterator(), false));
        final int[][] board = new int[height][width];
        State done = ops.execute(
            p -> board[p.my(height)][p.mx(width)],
            (p, i) -> board[p.my(height)][p.mx(width)] = i,
            new State(new Point(0, 0), 0),
            iterations
        );
        return Arrays.stream(board)
            .map(Arrays::stream)
            .map(s -> s.mapToObj(Integer::toString)
                .collect(Collectors.joining())
            )
            .collect(Collectors.joining("\r\n"));
    }
}