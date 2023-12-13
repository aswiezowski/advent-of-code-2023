import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CamelCardsTest {

    @ParameterizedTest
    @MethodSource("getHandTypesWithExpectedType")
    void getGivenHandType(String card, CamelCards.HandType expectedType) {
        // given

        // when
        var actual = CamelCards.getHandType(card);

        // then
        assertEquals(expectedType, actual);
    }

    static Stream<Arguments> getHandTypesWithExpectedType() {
        return Stream.of(Arguments.of("32T3K", CamelCards.HandType.ONE_PAIR),
                Arguments.of("KK677", CamelCards.HandType.TWO_PAIR),
                Arguments.of("KTJJT", CamelCards.HandType.TWO_PAIR),
                Arguments.of("T55J5", CamelCards.HandType.THREE_OF_KIND),
                Arguments.of("QQQJA", CamelCards.HandType.THREE_OF_KIND),
                Arguments.of("11111", CamelCards.HandType.FIVE_OF_KIND),
                Arguments.of("1111A", CamelCards.HandType.FOUR_OF_KIND),
                Arguments.of("13567", CamelCards.HandType.HIGH_CARD),
                Arguments.of("11567", CamelCards.HandType.ONE_PAIR),
                Arguments.of("23332", CamelCards.HandType.FULL_HOUSE)
        );
    }

    @Test
    void parseInputWorksCorrectly() {
        // when
        var input = """
                    32T3K 765
                    T55J5 684
                    KK677 28
                    KTJJT 220
                    QQQJA 483
                    """;

        // when
        final List<CamelCards.Hand> actual = CamelCards.parseCards(input);

        // then
        assertEquals(List.of(new CamelCards.Hand("32T3K", 765, CamelCards.HandType.ONE_PAIR),
                new CamelCards.Hand("T55J5", 684, CamelCards.HandType.THREE_OF_KIND),
                new CamelCards.Hand("KK677", 28, CamelCards.HandType.TWO_PAIR),
                new CamelCards.Hand("KTJJT", 220, CamelCards.HandType.TWO_PAIR),
                new CamelCards.Hand("QQQJA", 483, CamelCards.HandType.THREE_OF_KIND)), actual);
    }

    @Test
    void getPoints() {
        // when
        var input = """
                    32T3K 765
                    T55J5 684
                    KK677 28
                    KTJJT 220
                    QQQJA 483
                    """;

        // when
        final List<CamelCards.Hand> hands = CamelCards.parseCards(input);
        var actual = CamelCards.getPoints(hands);

        // then
        assertEquals(6440L, actual);
    }

    @Test
    void getPointsWithJoker() {
        // when
        var input = """
                    32T3K 765
                    T55J5 684
                    KK677 28
                    KTJJT 220
                    QQQJA 483
                    """;

        // when
        final List<CamelCards.Hand> hands = CamelCards.parseCardsWithJoker(input);
        var actual = CamelCards.getPointsWithJoker(hands);

        // then
        assertEquals(5905L, actual);
    }
}