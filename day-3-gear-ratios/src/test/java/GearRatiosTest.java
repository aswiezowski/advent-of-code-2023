import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GearRatiosTest {

    @ParameterizedTest
    @MethodSource("getArgumentsForTestingParsingNumbers")
    void getNumbersFromSingleLineReturnsNumbers(String line, List<GearRatios.PartNumber> expected) {
        // given

        // when
        final List<GearRatios.PartNumber> actual = GearRatios.getNumbersWithPosition(line, 0);

        // then
        assertEquals(expected, actual);
    }

    static Stream<Arguments> getArgumentsForTestingParsingNumbers() {
        return Stream.of(
                Arguments.of("467..114..", List.of(new GearRatios.PartNumber(467, 0, 3, 0), new GearRatios.PartNumber(114, 5, 8, 0))),
                Arguments.of("...*......", List.of()),
                Arguments.of("617*......", List.of(new GearRatios.PartNumber(617, 0, 3, 0))),
                Arguments.of("809*755", List.of(new GearRatios.PartNumber(809, 0, 3, 0), new GearRatios.PartNumber(755, 4, 7, 0)))
        );
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForTestingAdjacentSymbols")
    void hasAdjacentSymbolsReturnsTrueIfSymbolIsAdjacent(GearRatios.PartNumber partNumber, boolean expected) {
        // given
        var lines = """
                    467..114..
                    ...*......
                    ..35..633.
                    ......#...
                    617*......
                    .....+.58.
                    ..592.....
                    ......755.
                    ...$.*....
                    .664.598..
                    """;

        // when
        boolean actual = GearRatios.hasAdjacentSymbol(partNumber, lines.lines().toList());

        // then
        assertEquals(expected, actual);
    }

    static Stream<Arguments> getArgumentsForTestingAdjacentSymbols() {
        return Stream.of(
                Arguments.of(new GearRatios.PartNumber(467, 0, 3, 0), true),
                Arguments.of(new GearRatios.PartNumber(114, 5, 8, 0), false),
                Arguments.of(new GearRatios.PartNumber(35, 2, 4, 3), true),
                Arguments.of(new GearRatios.PartNumber(617, 0, 3, 4), true),
                Arguments.of(new GearRatios.PartNumber(58, 7, 9, 5), false),
                Arguments.of(new GearRatios.PartNumber(755, 6, 9, 8), true)
        );
    }

    @Test
    void getPartNumbersSumReturnsCorrectNumber() {
        // given
        var lines = """
                    467..114..
                    ...*......
                    ..35..633.
                    ......#...
                    617*......
                    .....+.58.
                    ..592.....
                    ......755.
                    ...$.*....
                    .664.598..
                    """;

        // when
        var actual = GearRatios.getPartNumbersSum(lines.lines().toList());

        // then
        assertEquals(4361, actual);
    }

    @Test
    void getAdjacentAsteriskReturnsAllAsterisks() {
        // given
        var lines = """
                    467..114..
                    ...*......
                    ..35..633.
                    ......#...
                    617*......
                    .....+.58.
                    ..592.....
                    ......755.
                    ...$.*....
                    .664.598..
                    .*........
                    ..30*45...
                    ...*......
                    """;

        // when
        var actual = GearRatios.getAdjacentAsterisk(new GearRatios.PartNumber(467, 0, 4, 0), lines.lines().toList());
        var actual2 = GearRatios.getAdjacentAsterisk(new GearRatios.PartNumber(30, 2, 4, 11), lines.lines().toList());

        // then
        assertEquals(List.of(new GearRatios.AsteriskPosition(3,1)), actual);
        assertEquals(List.of(new GearRatios.AsteriskPosition(1,10),
                new GearRatios.AsteriskPosition(4,11),
                new GearRatios.AsteriskPosition(3,12)), actual2);
    }

    @Test
    void getGearToRatioReturnCorrectValue() {
        // given
        var lines = """
                    467..114..
                    ...*......
                    ..35..633.
                    ......#...
                    617*......
                    .....+.58.
                    ..592.....
                    ......755.
                    ...$.*....
                    .664.598..
                    """;

        // when
        var actual = GearRatios.getRationSum(lines.lines().toList());

        // then
        assertEquals(467835, actual);
    }

}