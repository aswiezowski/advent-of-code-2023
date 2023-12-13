import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HotSpringsTest {


    @ParameterizedTest
    @MethodSource("getGalaxiesToDistance")
    void getNumberOfPossibilities(String inputLine, long numberOfPossibilities) {
        // given


        // when
        var line = HotSprings.parseLine(inputLine);
        var actual = HotSprings.getNumberOfArrangements(line);

        // then
        assertEquals(numberOfPossibilities, actual);
    }

    static Stream<Arguments> getGalaxiesToDistance() {
        return Stream.of(
                Arguments.of("???.### 1,1,3", 1),
                Arguments.of(".??..??...?##. 1,1,3", 4),
                Arguments.of("?#?#?#?#?#?#?#? 1,3,1,6", 1),
                Arguments.of("????.#...#... 4,1,1", 1),
                Arguments.of("????.######..#####. 1,6,5", 4),
                Arguments.of("?###???????? 3,2,1", 10)
        );
    }

    @Test
    void getNumberOfPossibilities2() {
        // given


        // when
        var line = HotSprings.parseLine("?#?#?#?#?#?#?#? 1,3,1,6");
        var actual = HotSprings.getNumberOfArrangements(line);

        // then
        assertEquals(1, actual);
    }


    @Test
    void getSumOfArrangements(){
        // given
        var input = """
                    ???.### 1,1,3
                    .??..??...?##. 1,1,3
                    ?#?#?#?#?#?#?#? 1,3,1,6
                    ????.#...#... 4,1,1
                    ????.######..#####. 1,6,5
                    ?###???????? 3,2,1
                    """;

        // when
        var lines = HotSprings.parseInput(input);
        var actual = HotSprings.getSumOfArrangements(lines);

        // then
        assertEquals(21, actual);
    }

    @ParameterizedTest
    @MethodSource("getUnfoldedGalaxiesToDistance")
    void getNumberOfUnfoldedPossibilities(String inputLine, long numberOfPossibilities) {
        // given


        // when
        var line = HotSprings.parseLineWithUnfold(inputLine);
        var actual = HotSprings.getNumberOfArrangements(line);

        // then
        assertEquals(numberOfPossibilities, actual);
    }

    static Stream<Arguments> getUnfoldedGalaxiesToDistance() {
        return Stream.of(
                Arguments.of("???.### 1,1,3", 1),
                Arguments.of(".??..??...?##. 1,1,3", 16384),
                Arguments.of("?#?#?#?#?#?#?#? 1,3,1,6", 1),
                Arguments.of("????.#...#... 4,1,1", 16),
                Arguments.of("????.######..#####. 1,6,5", 2500),
                Arguments.of("?###???????? 3,2,1", 506250)
        );
    }

}