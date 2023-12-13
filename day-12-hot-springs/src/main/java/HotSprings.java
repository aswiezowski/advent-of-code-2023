import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HotSprings {

    public static void main(String[] args) throws URISyntaxException, IOException {
        final String input = Files.readString(Path.of(ClassLoader.getSystemResource("input").toURI()));

        var lines = parseInput(input);

        final long sumOfArrangements = getSumOfArrangements(lines);
        System.out.println("Sum of arrangements: " + sumOfArrangements);

        var unfoldedLines = parseUnfoldedInput(input);
        final long sumOfUnfoldedArrangements = getSumOfArrangements(unfoldedLines);
        System.out.println("Sum of unfolded arrangements: " + sumOfUnfoldedArrangements);
    }


    static long getSumOfArrangements(List<Line> lines) {
        return lines.stream().mapToLong(HotSprings::getNumberOfArrangements).sum();
    }

    public static long getNumberOfArrangements(final Line line) {
        cache.clear();
        damagedGroupCache.clear();
        final List<List<Character>> permutations = getPermutations(line.springs, 0, line.damagedSprings);
        int possibleSolutions = 0;
        for (List<Character> permutation : permutations) {
            final List<Integer> damagedGroups = getDamagedGroups(permutation);
            if (damagedGroups.equals(line.damagedSprings)) {
                possibleSolutions++;
            }
        }

        return possibleSolutions;
    }

    static HashMap<List<Character>, List<Integer>> damagedGroupCache = new HashMap<>();

    static List<Integer> getDamagedGroups(List<Character> springs) {
        if (damagedGroupCache.containsKey(springs)) {
            return damagedGroupCache.get(springs);
        }
        int damagedCount = 0;
        var damagedGroups = new ArrayList<Integer>();
        for (int index = 0; index < springs.size(); index++) {
            if (springs.get(index) == '#') {
                damagedCount++;
            } else if (damagedCount > 0) {
                damagedGroups.add(damagedCount);
                damagedCount = 0;
            }
        }
        if (damagedCount > 0) {
            damagedGroups.add(damagedCount);
        }
//        damagedGroupCache.put(springs, damagedGroups);
        return damagedGroups;
    }

    static Map<Integer, List<List<Character>>> cache = new HashMap<>();

    static List<List<Character>> getPermutations(List<Character> springs, int index, List<Integer> damagedGroups) {
        if (index >= springs.size()) {
            return List.of(springs);
        }

        if (springs.get(index) == '?') {
            final ArrayList<List<Character>> newPermutation = new ArrayList<>();
            List<List<Character>> permutations = getPermutations(springs, index + 1, damagedGroups);
            for (List<Character> permutation : permutations) {
                final ArrayList<Character> permutation1 = new ArrayList<>(permutation);
                permutation1.set(index, '.');
                final List<Integer> damagedGroups1 = getDamagedGroups(permutation1.subList(index, permutation1.size()));
                if (damagedGroups1.size() <= damagedGroups.size()) {
                    if (damagedGroups1.equals(damagedGroups.subList(damagedGroups.size() - damagedGroups1.size(), damagedGroups.size()))) {
                        newPermutation.add(permutation1);
                    }
                }

                final ArrayList<Character> permutation2 = new ArrayList<>(permutation);
                permutation2.set(index, '#');

                final List<Integer> damagedGroups2 = getDamagedGroups(permutation2.subList(index, permutation2.size()));
                if (damagedGroups2.size() <= damagedGroups.size()) {
                    if (damagedGroups2.size() > 1) {
                        if (damagedGroups2.subList(1, damagedGroups2.size())
                                          .equals(damagedGroups.subList(damagedGroups.size() - damagedGroups2.size() + 1, damagedGroups.size()))) {
                            if (damagedGroups2.get(0) <= damagedGroups.get(damagedGroups.size() - damagedGroups2.size())) {
                                if (damagedGroups2.get(0) + index >= damagedGroups.get(damagedGroups.size() - damagedGroups2.size())) {
                                    newPermutation.add(permutation2);
                                    System.out.println("GRP" + damagedGroups2 + " " + permutation2);
                                }
                            }
                        }
                    } else if (damagedGroups2.size() == 1) {
                        if (damagedGroups2.get(0) <= damagedGroups.get(damagedGroups.size() - damagedGroups2.size())) {
                            newPermutation.add(permutation2);
                            System.out.println("GRP2" + damagedGroups2 + " " + permutation2);
                        }
                    }

                }
            }
            System.out.println("Index: " + index + ", damaged groups: " + damagedGroups.size() + ", permutation size: " + newPermutation.size());
            return newPermutation;
        } else {
            final List<List<Character>> permutations = getPermutations(springs, index + 1, damagedGroups);
            return permutations;
        }
    }

    record Line(List<Character> springs, List<Integer> damagedSprings) {

    }

    static List<Line> parseInput(String input) {
        final String[] lines = input.split("\n");
        var map = Stream.of(lines)
                        .map(HotSprings::parseLine)
                        .toList();
        return map;
    }


    static Line parseLine(final String line) {
        final String[] springsAndDamaged = line.split(" ");
        final List<Integer> damagedSprings = Arrays.stream(springsAndDamaged[1].split(",")).map(Integer::parseInt).toList();
        return new Line(springsAndDamaged[0].chars().mapToObj(character -> (char) character).toList(), damagedSprings);
    }


    static List<Line> parseUnfoldedInput(String input) {
        final String[] lines = input.split("\n");
        var map = Stream.of(lines)
                        .map(HotSprings::parseLineWithUnfold)
                        .toList();
        return map;
    }

    static Line parseLineWithUnfold(final String line) {
        final String[] springsAndDamaged = line.split(" ");
        final List<Integer> damagedSprings = Arrays.stream(springsAndDamaged[1].split(",")).map(Integer::parseInt).toList();
        final List<Character> list = springsAndDamaged[0].chars().mapToObj(character -> (char) character).toList();
        var unfoldedList = new ArrayList<Character>();
        var unfoldedSprings = new ArrayList<Integer>();
        for (int index = 0; index < 5; index++) {
            unfoldedList.addAll(list);
            if (index != 4) {
                unfoldedList.add('?');
            }
            unfoldedSprings.addAll(damagedSprings);
        }
        return new Line(unfoldedList, unfoldedSprings);
    }


}
