import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WaitForItTest {

    @Test
    void parseRaces() {
        // given
        var input = """
                    Time:      7  15   30
                    Distance:  9  40  200
                    """;

        // when
        var actual = WaitForIt.parseRaces(input);

        // then
        assertEquals(List.of(new WaitForIt.TimeAndDistance(7, 9),
                        new WaitForIt.TimeAndDistance(15, 40),
                        new WaitForIt.TimeAndDistance(30, 200)),
                actual);
    }


    @Test
    void calculateWinPossibilities() {
        // given
        var input = """
                    Time:      7  15   30
                    Distance:  9  40  200
                    """;

        // when
        var timeAndDistances = WaitForIt.parseRaces(input);
        var actual = WaitForIt.calculateWinPossibilities(timeAndDistances);

        // then
        assertEquals(List.of(4, 8, 9), actual);
    }

    @Test
    void calculateWinMultiplication() {
        // given
        var input = """
                    Time:      7  15   30
                    Distance:  9  40  200
                    """;

        // when
        var timeAndDistances = WaitForIt.parseRaces(input);
        var actual = WaitForIt.calculateWinMultiplication(timeAndDistances);

        // then
        assertEquals(288, actual);
    }

    @Test
    void calculateWinMultiplicationForJoinedInteger() {
        // given
        var input = """
                    Time:      71530
                    Distance:  940200
                    """;

        // when
        var timeAndDistances = WaitForIt.parseRaces(input);
        var actual = WaitForIt.calculateWinMultiplication(timeAndDistances);

        // then
        assertEquals(71503, actual);
    }
}