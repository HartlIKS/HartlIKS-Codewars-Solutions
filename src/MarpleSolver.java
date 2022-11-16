import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MarpleSolver {
    private static final List<Set<String>> INITIAL_OPTIONS = List.of(
        Set.of("A", "B", "C", "D", "E"),
        Set.of("F", "G", "H", "I", "J"),
        Set.of("K", "L", "M", "N", "O"),
        Set.of("P", "Q", "R", "S", "T")
    );

    private interface Clue {
        boolean allows(String value, int column, List<? extends List<? extends Set<String>>> other);
    }

    private record SameColumn(String s1, String s3) implements Clue {
        private boolean containsS1(Set<String> s) {
            return s.contains(s1);
        }

        private boolean containsS3(Set<String> s) {
            return s.contains(s3);
        }

        @Override
        public boolean allows(String value, int column, List<? extends List<? extends Set<String>>> other) {
            if (s1.equals(value)) {
                return other.get(column)
                    .stream()
                    .anyMatch(this::containsS3);
            } else if (s3.equals(value)) {
                return other.get(column)
                    .stream()
                    .anyMatch(this::containsS1);
            } else {
                return true;
            }
        }
    }

    private record LeftOf(String s1, String s3) implements Clue {
        private boolean containsS1(Set<String> s) {
            return s.contains(s1);
        }

        private boolean containsS3(Set<String> s) {
            return s.contains(s3);
        }

        @Override
        public boolean allows(String value, int column, List<? extends List<? extends Set<String>>> other) {
            if (s1.equals(value)) {
                return other.stream()
                    .skip(column + 1)
                    .flatMap(List::stream)
                    .anyMatch(this::containsS3);
            } else if (s3.equals(value)) {
                return other.stream()
                    .limit(column)
                    .flatMap(List::stream)
                    .anyMatch(this::containsS1);
            } else {
                return true;
            }
        }
    }

    public record Between(String s1, String s2, String s3) implements Clue {
        private boolean containsS1(Set<String> s) {
            return s.contains(s1);
        }

        private boolean containsS2(Set<String> s) {
            return s.contains(s2);
        }

        private boolean containsS3(Set<String> s) {
            return s.contains(s3);
        }

        @Override
        public boolean allows(String value, int column, List<? extends List<? extends Set<String>>> other) {
            if (s1.equals(value)) {
                return IntStream.range(column + 1, other.size())
                    .filter(i -> other.get(i)
                        .stream()
                        .anyMatch(this::containsS2)
                    )
                    .min()
                    .stream()
                    .boxed()
                    .flatMap(i -> other.stream()
                        .skip(i + 1)
                    )
                    .flatMap(List::stream)
                    .anyMatch(this::containsS3) ||
                    IntStream.range(0, column)
                        .filter(i -> other.get(i)
                            .stream()
                            .anyMatch(this::containsS2)
                        )
                        .max()
                        .stream()
                        .boxed()
                        .flatMap(i -> other.stream()
                            .limit(i)
                        )
                        .flatMap(List::stream)
                        .anyMatch(this::containsS3);
            } else if (s3.equals(value)) {
                return IntStream.range(column + 1, other.size())
                    .filter(i -> other.get(i)
                        .stream()
                        .anyMatch(this::containsS2)
                    )
                    .min()
                    .stream()
                    .boxed()
                    .flatMap(i -> other.stream()
                        .skip(i + 1)
                    )
                    .flatMap(List::stream)
                    .anyMatch(this::containsS1) ||
                    IntStream.range(0, column)
                        .filter(i -> other.get(i)
                            .stream()
                            .anyMatch(this::containsS2)
                        )
                        .max()
                        .stream()
                        .boxed()
                        .flatMap(i -> other.stream()
                            .limit(i)
                        )
                        .flatMap(List::stream)
                        .anyMatch(this::containsS1);
            } else if (s2.equals(value)) {
                Set<String> left = other.stream()
                    .limit(column)
                    .flatMap(List::stream)
                    .flatMap(Set::stream)
                    .collect(Collectors.toUnmodifiableSet());
                Set<String> right = other.stream()
                    .skip(column + 1)
                    .flatMap(List::stream)
                    .flatMap(Set::stream)
                    .collect(Collectors.toUnmodifiableSet());
                return (containsS1(left) && containsS3(right)) || (containsS1(right) && containsS3(left));
            } else {
                return true;
            }
        }
    }

    private record NextTo(String s1, String s2) implements Clue {
        private boolean containsS1(Set<String> s) {
            return s.contains(s1);
        }

        private boolean containsS2(Set<String> s) {
            return s.contains(s2);
        }

        @Override
        public boolean allows(String value, int column, List<? extends List<? extends Set<String>>> other) {
            if (s1.equals(value)) {
                return IntStream.of(column - 1, column + 1)
                    .filter(i -> i >= 0 && i < other.size())
                    .mapToObj(other::get)
                    .flatMap(List::stream)
                    .anyMatch(this::containsS2);
            } else if (s2.equals(value)) {
                return IntStream.of(column - 1, column + 1)
                    .filter(i -> i >= 0 && i < other.size())
                    .mapToObj(other::get)
                    .flatMap(List::stream)
                    .anyMatch(this::containsS1);
            } else {
                return true;
            }
        }
    }

    public static String solve(String[] clues) {
        final List<? extends List<? extends Set<String>>> options = IntStream.range(0, 5)
            .mapToObj(i -> INITIAL_OPTIONS)
            .map(List::stream)
            .map(s -> s.map(HashSet::new))
            .map(Stream::toList)
            .toList();

        final Collection<? extends Clue> parsedClues = Arrays.stream(clues)
            .map(s -> s.charAt(0) == s.charAt(2) ?
                new NextTo(s.substring(0, 1), s.substring(1, 2)) :
                switch (s.charAt(1)) {
                    case '^' -> new SameColumn(s.substring(0, 1), s.substring(2, 3));
                    case '<' -> new LeftOf(s.substring(0, 1), s.substring(2, 3));
                    default -> new Between(s.substring(0, 1), s.substring(1, 2), s.substring(2, 3));
                }
            )
            .toList();

        while (options.stream()
            .flatMap(List::stream)
            .mapToInt(Set::size)
            .anyMatch(i -> i > 1)
        ) {
            boolean ddelta = false;
            while (true) {
                boolean delta = false;
                final ListIterator<? extends List<? extends Set<String>>> iterator = options.listIterator();
                while (iterator.hasNext()) {
                    final List<? extends Set<String>> col = iterator.next();
                    for (Set<String> opt : col) {
                        if (opt.removeIf(s -> parsedClues.stream()
                            .anyMatch(
                                Predicate.not(clue ->
                                    clue.allows(s, iterator.previousIndex(), options)
                                )
                            )
                        )) delta = true;
                    }
                }
                if (!delta) break;
                else ddelta = true;
            }
            while (true) {
                boolean delta = false;
                for (List<? extends Set<String>> l : options) {
                    final ListIterator<? extends Set<String>> iterator2 = l.listIterator();
                    while (iterator2.hasNext()) {
                        final Set<String> s = iterator2.next();
                        if (s.size() == 1) {
                            final String single = s.iterator().next();
                            for (List<? extends Set<String>> l2 : options) {
                                Set<String> s2 = l2.get(iterator2.previousIndex());
                                if (s2 == s) continue;
                                if (s2.remove(single)) delta = true;
                            }
                        }
                        final Optional<String> single2 = s.stream()
                            .filter(str -> options.stream()
                                .map(l2 -> l2.get(iterator2.previousIndex()))
                                .filter(s2 -> s2 != s)
                                .noneMatch(s2 -> s2.contains(str))
                            )
                            .findAny();
                        if (single2.isPresent() && s.retainAll(Set.of(single2.get()))) delta = true;
                    }
                }
                if (!delta) break;
                else ddelta = true;
            }
            if(!ddelta) return null;
        }
        StringBuilder b = new StringBuilder(20);
        for(int i=0;i<4;i++) {
            for(int j=0;j<5;j++) {
                b.append(String.join("", options.get(j).get(i)));
            }
        }
        return b.toString();
    }

}