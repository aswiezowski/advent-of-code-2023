import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class Scratchcards {


    public static void main(String[] args) throws URISyntaxException, IOException {
        final List<String> lines = Files.readAllLines(Path.of(ClassLoader.getSystemResource("input").toURI()));

        final int points = Scratchcards.countScratchcardPoints(lines);
        System.out.println("Scratchcards sum of points: " + points);

        var copiesCount = Scratchcards.countScratchCardsCopies(lines, lines, 0);
        System.out.println("Scratchcards copies: " + copiesCount);
    }

    record Card(int id, List<String> winningNumbers, List<String> numbersIHave) {

    }


    static Card parseCard(String cardLine) {
        final String[] cardElements = cardLine.split(":");
        final String[] cardIdentifier = cardElements[0].split(" ");
        final String id = cardIdentifier[cardIdentifier.length - 1];

        final String numbersPart = cardElements[1];
        final String[] numbers = numbersPart.split("\\|");
        final String winningNumbersPart = numbers[0];
        final String numbersIHavePart = numbers[1];

        final List<String> winningNumbers = Stream.of(winningNumbersPart.trim().split(" ")).filter(number -> !number.isBlank()).toList();
        final List<String> numbersIHave = Stream.of(numbersIHavePart.trim().split(" ")).filter(number -> !number.isBlank()).toList();

        return new Card(Integer.parseInt(id), winningNumbers, numbersIHave);
    }

    static long countWinningNumbers(Card card) {
        return card.numbersIHave.stream().filter(card.winningNumbers::contains).count();
    }

    static int countPoints(long winningNumberPoint) {
        if (winningNumberPoint > 1) {
            return (int) Math.pow(2, winningNumberPoint - 1);
        }
        if (winningNumberPoint == 1) {
            return 1;
        }
        return 0;
    }

    static int countScratchcardPoints(List<String> lines) {
        return lines.stream().map(Scratchcards::parseCard)
                    .map(Scratchcards::countWinningNumbers)
                    .mapToInt(Scratchcards::countPoints)
                    .sum();
    }

    static int countScratchCardsCopies(List<String> lines, List<String> subList, int startLineNumber) {
        int scractchCardCount = subList.size();
        for (int lineNumber = 0; lineNumber < subList.size(); lineNumber++) {
            var countOfWinningNumbers = (int) countWinningNumbers(parseCard(subList.get(lineNumber)));
            final int fromIndex = startLineNumber + lineNumber + 1;
            var copies = lines.subList(fromIndex, fromIndex + countOfWinningNumbers);
            scractchCardCount += countScratchCardsCopies(lines, copies, fromIndex);
        }
        return scractchCardCount;
    }

}
