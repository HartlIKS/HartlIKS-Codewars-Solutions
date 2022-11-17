import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {
    private static class State {
        public final boolean isFinish;
        public final Map<State, Set<String>> regexMap =  new HashMap<>();
        public State(boolean isFinish) {
            this.isFinish = isFinish;
        }

        public State() {
            this(false);
        }

        public void setInitialStates(State zero, State one) {
            regexMap.clear();
            regexMap.put(zero, Set.of("0"));
            regexMap.put(one, Set.of("1"));
        }

        private static String makeRepeat(Set<String> rgx) {
            if(rgx.size() <= 1) {
                String rgxstr = rgx.stream().findAny().orElse("");
                if(rgxstr.isEmpty()) return rgxstr;
                else if(rgxstr.length() == 1) return rgxstr+"*+";
                else return "(?:"+rgxstr+")*+";
            } else return "(?:"+String.join("|", rgx)+")*+";
        }

        public void resolve(final State state) {
            if (state == this) return;
            Set<String> rgx = regexMap.remove(state);
            if (rgx == null) return;
            Set<String> repeatRgx = state.regexMap.getOrDefault(state, Set.of());
            final String rrgx = makeRepeat(repeatRgx);
            state.regexMap.forEach((nxt, rgxs) -> {
                if (nxt == state) return;
                regexMap.merge(
                    nxt,
                    rgx.stream()
                        .flatMap(r -> rgxs.stream()
                            .map(r2 -> r + rrgx + r2)
                        )
                        .collect(Collectors.toUnmodifiableSet()),
                    (a, b) -> Stream.concat(a.stream(), b.stream())
                        .collect(Collectors.toUnmodifiableSet())
                );
            });
        }
    }

    private static String compileRegex(int mod) {
        if(mod == 1) return "[01]++";
        final State finish = new State(true);
        final State[] remainder = new State[mod];
        for(int i=0;i< remainder.length;i++) {
            remainder[i] =new State();
        }
        for(int i=0;i< remainder.length;i++) {
            remainder[i].setInitialStates(
                remainder[(2*i) % remainder.length],
                remainder[(2*i+1) % remainder.length]
            );
        }
        remainder[0].regexMap.put(finish, Set.of(""));
        final State start = new State();
        start.setInitialStates(remainder[0], remainder[1]);
        for(int i=0;i< remainder.length;i++) {
            start.resolve(remainder[i]);
            for(int j=i+1;j<remainder.length;j++) {
                remainder[j].resolve(remainder[i]);
            }
        }
        assert start.regexMap.keySet().size() == 1;
        assert start.regexMap.containsKey(finish);
        return String.join("|", start.regexMap.get(finish));
    }

    public static String regexDivisibleBy(int n) {
        return Solution.compileRegex(n);
    }
}