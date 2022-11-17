import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfiniteDigitalString {

    public static long findPosition(final String s) {
        StringBuilder rgx = new StringBuilder("(?<result>" + s.substring(s.length() - 1) + ".*+)");
        for (int i = s.length() - 2; i >= 0; i--) {
            rgx.insert(0, "(?:" + s.charAt(i));
            rgx.append("?+)");
        }
        rgx.append("$");
        Pattern finder = Pattern.compile(rgx.toString());
        long offset = 0;
        String part = "";
        long next = 1;
        long millis = System.currentTimeMillis();
        while (true) {
            if (part.length() < s.length()) {
                StringBuilder b = new StringBuilder(part);
                for (int i = 0; i % 10 != 0 || b.length() < s.length(); i++) {
                    b.append(next++);
                }
                part = b.toString();
            }
            Matcher m = finder.matcher(part);
            int mov;
            if (!m.find()) mov = part.length();
            else {
                int idx = m.start();
                if (m.group().startsWith(s)) return offset + idx;
                mov = idx;
            }
            offset += mov;
            part = part.substring(mov);
            if (System.currentTimeMillis() >= millis + 1000) {
                millis = System.currentTimeMillis();
                System.err.printf("seek = %s%noffset = %d%nnext = %d%n%n", s, offset, next);
            }
        }
    }
}