import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.stream.Stream;

public class GearRatios {

    public static void main(String[] args) throws URISyntaxException, IOException {
        List<String> lines = Files.readAllLines(Path.of(ClassLoader.getSystemResource("input").toURI()));

        final int partNumbersSum = getPartNumbersSum(lines);
        System.out.println("Part numbers sum: " + partNumbersSum);

        final int rationSum = getRationSum(lines);
        System.out.println("Ratio sum: " + rationSum);

    }

    record PartNumber(int number, int positionX, int positionXEnd, int lineNumber) {
    }


    static int getPartNumbersSum(List<String> lines) {
        var potentialPartNumbers = new ArrayList<PartNumber>();
        for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
            final List<PartNumber> numbers = getNumbersWithPosition(lines.get(lineNumber), lineNumber);
            potentialPartNumbers.addAll(numbers);
        }
        final List<PartNumber> partNumbers = potentialPartNumbers.stream()
                                                                 .filter(number -> hasAdjacentSymbol(number, lines))
                                                                 .toList();
        return partNumbers.stream()
                          .mapToInt(PartNumber::number)
                          .sum();
    }

    static boolean hasAdjacentSymbol(final PartNumber number, final List<String> lines) {
        var searchFromPositionX = number.positionX - 1;
        var searchToPositionX = number.positionXEnd + 1;
        var searchFromPositionY = number.lineNumber - 1;
        var searchToPositionY = number.lineNumber + 1;

        if (searchFromPositionX < 0) {
            searchFromPositionX = 0;
        }
        if (searchToPositionX >= lines.iterator().next().length()) {
            searchToPositionX = lines.iterator().next().length();
        }
        if (searchFromPositionY < 0) {
            searchFromPositionY = 0;
        }
        if (searchToPositionY >= lines.size()) {
            searchToPositionY = lines.size() - 1;
        }

        for (int posY = searchFromPositionY; posY <= searchToPositionY; posY++) {
            final String line = lines.get(posY);
            var regionToLookForSymbol = line.substring(searchFromPositionX, searchToPositionX);
            final boolean matches = regionToLookForSymbol.matches(".*[^0-9\\.].*");
            if (matches) {
                return true;
            }
        }

        return false;
    }

    record AsteriskPosition(int positionX, int positionY) {
    }

    static int getRationSum(List<String> lines) {
        var potentialPartNumbers = new ArrayList<PartNumber>();
        for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
            final List<PartNumber> numbers = getNumbersWithPosition(lines.get(lineNumber), lineNumber);
            potentialPartNumbers.addAll(numbers);
        }
        final List<PartNumber> partNumbers = potentialPartNumbers.stream()
                                                                 .filter(number -> hasAdjacentSymbol(number, lines))
                                                                 .toList();
        var asteriskToPartNumber = new HashMap<AsteriskPosition, List<PartNumber>>();
        for (PartNumber partNumber: partNumbers){
            List<AsteriskPosition> adjacentAsterisk = getAdjacentAsterisk(partNumber, lines);
            for(AsteriskPosition position: adjacentAsterisk ){
                asteriskToPartNumber.merge(position, List.of(partNumber), (partNumbers1, partNumbers2) -> {
                    var newList = new ArrayList<PartNumber>();
                    newList.addAll(partNumbers1);
                    newList.addAll(partNumbers2);
                    return newList;
                });
            }
        }

        return asteriskToPartNumber.values().stream()
                                   .filter(numbers -> numbers.size() > 1)
                                   .mapToInt(numbers -> numbers.stream().mapToInt(PartNumber::number).reduce(1, (left, right) -> left * right))
                                   .sum();
    }

    static List<AsteriskPosition> getAdjacentAsterisk(final PartNumber number, final List<String> lines) {
        var searchFromPositionX = number.positionX - 1;
        var searchToPositionX = number.positionXEnd + 1;
        var searchFromPositionY = number.lineNumber - 1;
        var searchToPositionY = number.lineNumber + 1;

        if (searchFromPositionX < 0) {
            searchFromPositionX = 0;
        }
        if (searchToPositionX >= lines.iterator().next().length()) {
            searchToPositionX = lines.iterator().next().length();
        }
        if (searchFromPositionY < 0) {
            searchFromPositionY = 0;
        }
        if (searchToPositionY >= lines.size()) {
            searchToPositionY = lines.size() - 1;
        }

        List<AsteriskPosition> positions = new ArrayList<>();
        for (int posY = searchFromPositionY; posY <= searchToPositionY; posY++) {
            final String line = lines.get(posY);
            var startPosition = searchFromPositionX;
            while (line.indexOf("*", startPosition) != -1 && line.indexOf("*", startPosition) < searchToPositionX) {
                positions.add(new AsteriskPosition(line.indexOf("*", startPosition), posY));
                startPosition = line.indexOf("*", startPosition)+1;
            }
        }
        return positions;
    }

    static List<PartNumber> getNumbersWithPosition(String line, int lineNumber) {
        final Scanner scanner = new Scanner(line);
        final Stream<MatchResult> result = scanner.findAll("\\d+");
        final List<PartNumber> partNumbers = result
                .map(match -> new PartNumber(Integer.parseInt(match.group()), match.start(), match.end(), lineNumber))
                .toList();
        scanner.close();
        return partNumbers;
    }
}
