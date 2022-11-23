public class Kata {
    private static char combine(char a, char b) {
        if(a == b) return a;
        return switch (a + b) {
            case 'R' + 'G' -> 'B';
            case 'R' + 'B' -> 'G';
            case 'G' + 'B' -> 'R';
            default -> '?';
        };
    }

    private interface SimpleCharSequence extends CharSequence {
        @Override
        default CharSequence subSequence(int start, int end) {
            if(end > this.length() || start > end || start < 0) throw new IndexOutOfBoundsException();
            final SimpleCharSequence th = this;
            return new SimpleCharSequence() {
                @Override
                public int length() {
                    return end-start;
                }

                @Override
                public char charAt(int index) {
                    return th.charAt(index+start);
                }
            };
        }
    }

    private static CharSequence shrinkRow(final CharSequence row, final int p3) {
        assert row.length() > p3 : "row too large";
        return new SimpleCharSequence() {
            @Override
            public int length() {
                return row.length() - p3;
            }

            @Override
            public char charAt(int index) {
                return combine(row.charAt(index), row.charAt(index+p3));
            }
        };
    }

    public static char triangle(final String row) {
        int p = 1;
        while(p < row.length()) p*=3;
        CharSequence seq = row;
        for(;p > 0;p/=3) {
            while(seq.length() > p) {
                seq = shrinkRow(seq, p);
            }
        }
        assert seq.length() == 1 : "Nonstandard row "+seq.length();
        return seq.charAt(0);
    }

}