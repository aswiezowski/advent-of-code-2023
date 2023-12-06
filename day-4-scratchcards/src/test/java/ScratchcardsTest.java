import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ScratchcardsTest {

    @Test
    void cardIsParsedCorrectly() {
        // given
        var line = "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53";

        // when
        final Scratchcards.Card actual = Scratchcards.parseCard(line);

        // then
        assertEquals(new Scratchcards.Card(1, List.of("41", "48", "83", "86", "17"), List.of("83", "86", "6", "31", "17", "9", "48", "53")), actual);
    }

    @Test
    void countWinningNumbersReturnsMatchCount() {
        // given
        var line = "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53";
        var card = Scratchcards.parseCard(line);

        // when
        var actual = Scratchcards.countWinningNumbers(card);

        // then
        assertEquals(4, actual);
    }

    @ParameterizedTest
    @MethodSource("getNumberToPoints")
    void countPointsReturnsCorrectValues(long count, int expectedPoints) {
        // given

        // when
        var actual = Scratchcards.countPoints(count);

        // then
        assertEquals(expectedPoints, actual);
    }

    static Stream<Arguments> getNumberToPoints(){
        return Stream.of(Arguments.of(0,0),
                Arguments.of(1,1),
                Arguments.of(2,2),
                Arguments.of(3,4),
                Arguments.of(4,8));
    }

    @Test
    void scratchcardPointsAreSummedCorrectly(){
        // given
        var scratchcard = """
                          Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
                          Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
                          Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
                          Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
                          Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
                          Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
                          """;

        // when
        final int actual = Scratchcards.countScratchcardPoints(scratchcard.lines().toList());

        // then
        assertEquals(13, actual);
    }

    @Test
    void scratchcardCountIsCorrect(){
        // given
        var scratchcard = """
                          Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
                          Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
                          Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
                          Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
                          Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
                          Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
                          """;

        // when
        final List<String> linesList = scratchcard.lines().toList();
        final int actual = Scratchcards.countScratchCardsCopies(linesList, linesList, 0);

        // then
        assertEquals(30, actual);
    }

}