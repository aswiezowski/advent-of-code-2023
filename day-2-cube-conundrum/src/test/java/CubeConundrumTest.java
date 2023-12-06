import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CubeConundrumTest {

    @Test
    void gameIdIsParsedCorrectly() {
        // given
        var input = "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green";

        // when
        var actual = CubeConundrum.getGameStatics(input);

        // then
        assertEquals(1, actual.id());
    }

    @ParameterizedTest
    @MethodSource("getSubsetsWithExpectedCount")
    void subsetIsParsedCorrectly(String inputLine, CubeConundrum.CubeSubset expected) {
        // given

        // when
        var actual = CubeConundrum.parseSubset(inputLine);

        // then
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> getSubsetsWithExpectedCount() {
        return Stream.of(
                Arguments.of("3 blue, 4 red", new CubeConundrum.CubeSubset(4, 0, 3)),
                Arguments.of("1 red, 2 green, 6 blue", new CubeConundrum.CubeSubset(1, 2, 6)),
                Arguments.of("2 green", new CubeConundrum.CubeSubset(0, 2, 0))
        );
    }

    @Test
    void getGameStatics() {
        // given
        var input = "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green";

        // when
        var actual = CubeConundrum.getGameStatics(input);

        // then
        var expected = new CubeConundrum.Game(1,
                List.of(new CubeConundrum.CubeSubset(4, 0, 3),
                        new CubeConundrum.CubeSubset(1, 2, 6),
                        new CubeConundrum.CubeSubset(0, 2, 0)));
        assertEquals(expected, actual);
    }

    @Test
    void getSumOfIdsToPossibleGames(){
        // given
        var input = """
                    Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
                    Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
                    Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
                    Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
                    Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
                    """;

        // when
        var actual = CubeConundrum.getSumOfIdsForInput(input);

        // then
        assertEquals(8, actual);
    }
    @Test
    void getPowerOfGameCubes(){
        // given
        var input = new CubeConundrum.Game(1,
                List.of(new CubeConundrum.CubeSubset(4, 0, 3),
                        new CubeConundrum.CubeSubset(1, 2, 6),
                        new CubeConundrum.CubeSubset(0, 2, 0)));

        // when
        var actual = CubeConundrum.getPowerOfGameCubes(input);

        // then
        assertEquals(48, actual);
    }

    @Test
    void getSumOfPowersOfGameCubes(){
        // given
        var input = """
                    Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
                    Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
                    Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
                    Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
                    Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
                    """;

        // when
        var actual = CubeConundrum.getSumOfPowersOfCubesForInput(input);

        // then
        assertEquals(2286, actual);
    }
}