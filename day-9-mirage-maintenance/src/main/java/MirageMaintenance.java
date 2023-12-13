import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class MirageMaintenance {

    public static void main(String[] args) throws URISyntaxException, IOException {
        final String input = Files.readString(Path.of(ClassLoader.getSystemResource("input").toURI()));

        var history = parseInput(input);
        var sumOfExtrapolatedNumbers = getSumOfRightExtrapolatedNumbers(history);
        System.out.println("Sum of extrapolated numbers: " + sumOfExtrapolatedNumbers);

        var sumOfLeftExtrapolatedNumbers = getSumOfLeftExtrapolatedNumbers(history);
        System.out.println("Sum of left extrapolated numbers: " + sumOfLeftExtrapolatedNumbers);

    }


    static List<List<Long>> parseInput(String input) {
        final String[] lines = input.split("\n");
        var history = Stream.of(lines)
                            .map(MirageMaintenance::parseLine)
                            .toList();
        return history;
    }

    static List<Long> parseLine(String line) {
        return Arrays.stream(line.split(" ")).map(Long::valueOf).toList();
    }

    static long extrapolateNumberRight(List<Long> numbers) {
        if (numbers.stream().allMatch(number -> number.equals(0L))) {
            return 0;
        }
        var numbersDifference = new ArrayList<Long>();
        for (int index = 0; index < numbers.size() - 1; index++) {
            numbersDifference.add(numbers.get(index + 1) - numbers.get(index));
        }
        final long extrapolatedNumber = extrapolateNumberRight(numbersDifference);
        return extrapolatedNumber + numbers.get(numbers.size() - 1);
    }

    static long extrapolateNumberLeft(List<Long> numbers) {
        if (numbers.stream().allMatch(number -> number.equals(0L))) {
            return 0;
        }
        var numbersDifference = new ArrayList<Long>();
        for (int index = 0; index < numbers.size() - 1; index++) {
            numbersDifference.add(numbers.get(index + 1) - numbers.get(index));
        }
        final long extrapolatedNumber = extrapolateNumberLeft(numbersDifference);
        return numbers.get(0) - extrapolatedNumber;
    }

    static long getSumOfRightExtrapolatedNumbers(List<List<Long>> history) {
        return history.stream().map(MirageMaintenance::extrapolateNumberRight).reduce(0L, Long::sum);
    }

    static long getSumOfLeftExtrapolatedNumbers(List<List<Long>> history) {
        return history.stream().map(MirageMaintenance::extrapolateNumberLeft).reduce(0L, Long::sum);
    }


}
