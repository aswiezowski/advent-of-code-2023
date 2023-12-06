import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TrebuchetTest {

    @Test
    void convertLineTestFor2Digits(){
        // given
        var line = "pqr3stu8vwx";

        // when
        final int actual = Trebuchet.convertToNumber(line);

        // then
        assertEquals(38, actual);
    }

    @Test
    void convertLineTestFor1Digit(){
        // given
        var line = "treb7uchet";

        // when
        final int actual = Trebuchet.convertToNumber(line);

        // then
        assertEquals(77, actual);
    }

    @Test
    void convertLineTestForMoreThan2Digit(){
        // given
        var line = "a1b2c3d4e5f";

        // when
        final int actual = Trebuchet.convertToNumber(line);

        // then
        assertEquals(15, actual);
    }

    @ParameterizedTest
    @MethodSource("testedLinesWithResults")
    void convertLineTestForMoreNumberAsText(String line, int expected){
        // given

        // when
        final int actual = Trebuchet.convertToNumber(line);

        // then
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testedLinesWithResults() {
        return Stream.of(
                Arguments.of("two1nine", 29),
                Arguments.of("eightwothree", 83),
                Arguments.of("abcone2threexyz", 13),
                Arguments.of("xtwone3four", 24),
                Arguments.of("4nineeightseven2", 42),
                Arguments.of("zoneight234", 14),
                Arguments.of("7pqrstsixteen", 76)
        );
    }
}