import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CosmicExpansionTest {


    @Test
    void getSumOfDistances() {
        // when
        var input = """
                    ...#......
                    .......#..
                    #.........
                    ..........
                    ......#...
                    .#........
                    .........#
                    ..........
                    .......#..
                    #...#.....
                    """;

        // when
        var map = CosmicExpansion.parseInput(input);
        var actual = CosmicExpansion.calculateAllDistancesSum(map, 2);

        // then
        assertEquals(374, actual);
    }

    @ParameterizedTest
    @MethodSource("getGalaxiesToDistance")
    void getDistanceBetweenGalaxies(CosmicExpansion.Position galaxy1, CosmicExpansion.Position galaxy2, long distance) {
        // given
        var input = """
                    ...#......
                    .......#..
                    #.........
                    ..........
                    ......#...
                    .#........
                    .........#
                    ..........
                    .......#..
                    #...#.....
                    """;

        // when
        var map = CosmicExpansion.parseInput(input);
        final List<Integer> emptyRows = CosmicExpansion.getEmptyRows(map);
        final List<Integer> emptyColumns = CosmicExpansion.getEmptyColumns(map);
        var actual = CosmicExpansion.getDistanceBetweenGalaxies(galaxy1, galaxy2, emptyRows, emptyColumns, 2);

        // then
        assertEquals(distance, actual);
    }

    static Stream<Arguments> getGalaxiesToDistance(){
        return Stream.of(
                Arguments.of(new CosmicExpansion.Position(3, 0), new CosmicExpansion.Position(7, 8), 15),
                Arguments.of(new CosmicExpansion.Position(0, 2), new CosmicExpansion.Position(9, 6), 17),
                Arguments.of(new CosmicExpansion.Position(0, 9), new CosmicExpansion.Position(4, 9), 5),
                Arguments.of(new CosmicExpansion.Position(1, 5), new CosmicExpansion.Position(4, 9), 9)
        );
    }


}