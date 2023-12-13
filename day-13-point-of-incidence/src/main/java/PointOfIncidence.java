import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PointOfIncidence {

    public static void main(String[] args) throws URISyntaxException, IOException {
        final String input = Files.readString(Path.of(ClassLoader.getSystemResource("input").toURI()));

        var lines = parseAllNotes(input);
        var sum = getSummarize(lines);

        System.out.println("Summary: " + sum);

        var sumWithSmudge = getSummarizeWithSmudge(lines);
        System.out.println("Summary: " + sumWithSmudge);


    }

    static List<List<List<Character>>> parseAllNotes(String input) {
        return Arrays.stream(input.split("\n\n")).map(PointOfIncidence::parseInputNotes).toList();
    }

    static List<List<Character>> parseInputNotes(String input) {
        final String[] lines = input.split("\n");
        var map = Stream.of(lines)
                        .map(PointOfIncidence::parseLine)
                        .toList();
        return map;
    }


    static List<Character> parseLine(final String line) {
        return line.chars().mapToObj(character -> (char) character).toList();
    }


    static long getSummarize(List<List<List<Character>>> notes) {
        return notes.stream().mapToLong(note ->
                findHorizontalReflection(note) * 100L + findVerticalReflection(note)
        ).sum();
    }

    static long getSummarizeWithSmudge(List<List<List<Character>>> notes) {
        return notes.stream().mapToLong(note ->
                findHorizontalReflectionWithSmudge(note) * 100L + findVerticalReflectionWithSmudge(note)
        ).sum();
    }

    static int findHorizontalReflection(List<List<Character>> lines) {
        for (int lineNumber = 0; lineNumber < lines.size() - 1; lineNumber++) {
            if (lines.get(lineNumber).equals(lines.get(lineNumber + 1))) {
                if (isHorizontalPointOfReflection(lines, lineNumber)) {
                    return lineNumber + 1;
                }
            }
        }

        return 0;
    }

    static int findHorizontalReflectionWithSmudge(List<List<Character>> lines) {
        for (int lineNumber = 0; lineNumber < lines.size() - 1; lineNumber++) {
            final int finalLineNumber = lineNumber;
            final long numberOfWrongMirrors = IntStream.range(0, lines.iterator().next().size())
                                                       .filter(xPosition -> !lines.get(finalLineNumber).get(xPosition).equals(lines.get(finalLineNumber + 1).get(xPosition)))
                                                       .count();
            if (numberOfWrongMirrors == 1) {
                if (isHorizontalPointOfReflectionWithSmudge(lines, lineNumber, false)) {
                    return lineNumber + 1;
                }
            } else if (numberOfWrongMirrors == 0) {
                if (isHorizontalPointOfReflectionWithSmudge(lines, lineNumber, false)) {
                    return lineNumber + 1;
                }
            }
        }

        return 0;
    }

    private static boolean isHorizontalPointOfReflectionWithSmudge(final List<List<Character>> lines, final int pointOfReflection, boolean swappedAnyPoint) {
        for (int index = pointOfReflection, index2 = pointOfReflection + 1; index >= 0 && index2 < lines.size(); index--, index2++) {

            final int finalIndex = index;
            final int finalIndex1 = index2;
            final long numberOfWrongMirrors = IntStream.range(0, lines.iterator().next().size())
                                                       .filter(xPosition -> !lines.get(finalIndex).get(xPosition).equals(lines.get(finalIndex1).get(xPosition)))
                                                       .count();
            if (numberOfWrongMirrors > 1) {
                return false;
            } else if (numberOfWrongMirrors == 1 && !swappedAnyPoint) {
                swappedAnyPoint = true;
            } else if (numberOfWrongMirrors == 1) {
                return false;
            }
        }
        return swappedAnyPoint;
    }

    private static boolean isHorizontalPointOfReflection(final List<List<Character>> lines, final int pointOfReflection) {
        for (int index = pointOfReflection, index2 = pointOfReflection + 1; index >= 0 && index2 < lines.size(); index--, index2++) {
            if (!lines.get(index).equals(lines.get(index2))) {
                return false;
            }
        }
        return true;
    }

    static int findVerticalReflection(List<List<Character>> lines) {
        for (int xPosition = 0; xPosition < lines.stream().iterator().next().size() - 1; xPosition++) {
            boolean areTwoVerticalLinesSame = true;

            for (int yPosition = 0; yPosition < lines.size() - 1; yPosition++) {
                if (!lines.get(yPosition).get(xPosition).equals(lines.get(yPosition).get(xPosition + 1))) {
                    areTwoVerticalLinesSame = false;
                }
            }
            if (areTwoVerticalLinesSame) {
                if (isVerticalPointOfReflection(lines, xPosition)) {
                    return xPosition + 1;
                }
            }
        }
        return 0;
    }

    private static boolean isVerticalPointOfReflection(final List<List<Character>> lines, final int pointOfReflection) {
        for (int index = pointOfReflection, index2 = pointOfReflection + 1; index >= 0 && index2 < lines.iterator().next().size(); index--, index2++) {
            for (int yPosition = 0; yPosition < lines.size(); yPosition++) {
                if (!lines.get(yPosition).get(index).equals(lines.get(yPosition).get(index2))) {
                    return false;
                }
            }
        }
        return true;
    }

    static int findVerticalReflectionWithSmudge(List<List<Character>> lines) {
        for (int xPosition = 0; xPosition < lines.stream().iterator().next().size() - 1; xPosition++) {
            var countOfWrongMirrors = 0;

            for (int yPosition = 0; yPosition < lines.size() - 1; yPosition++) {
                if (!lines.get(yPosition).get(xPosition).equals(lines.get(yPosition).get(xPosition + 1))) {
                    countOfWrongMirrors++;
                }
            }
            if (countOfWrongMirrors == 0) {
                if (isVerticalPointOfReflectionWithSmudge(lines, xPosition, false)) {
                    return xPosition + 1;
                }
            } else if (countOfWrongMirrors == 1) {
                if (isVerticalPointOfReflectionWithSmudge(lines, xPosition, false)) {
                    return xPosition + 1;
                }
            }
        }
        return 0;
    }

    private static boolean isVerticalPointOfReflectionWithSmudge(final List<List<Character>> lines, final int pointOfReflection, boolean swappedAnyPoint) {
        for (int index = pointOfReflection, index2 = pointOfReflection + 1; index >= 0 && index2 < lines.iterator().next().size(); index--, index2++) {
            var countOfWrongMirrors = 0;
            for (int yPosition = 0; yPosition < lines.size(); yPosition++) {
                if (!lines.get(yPosition).get(index).equals(lines.get(yPosition).get(index2))) {
                    countOfWrongMirrors++;
                }
            }
            if (countOfWrongMirrors > 1) {
                return false;
            } else if (countOfWrongMirrors == 1 && !swappedAnyPoint) {
                swappedAnyPoint = true;
            } else if (countOfWrongMirrors == 1) {
                return false;
            }
        }
        return swappedAnyPoint;
    }


}
