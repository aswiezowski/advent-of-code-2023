import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PointOfIncidenceTest {


    @Test
    void getVerticalMatch() {
        // given
        var input = """
                    #.##..##.
                    ..#.##.#.
                    ##......#
                    ##......#
                    ..#.##.#.
                    ..##..##.
                    #.#.##.#.
                    """;

        // when
        var line = PointOfIncidence.parseInputNotes(input);
        var actual = PointOfIncidence.findVerticalReflection(line);

        // then
        assertEquals(5, actual);
    }

    @Test
    void getNonHorizontalMatch() {
        // given
        var input = """
                    #.##..##.
                    ..#.##.#.
                    ##......#
                    ##......#
                    ..#.##.#.
                    ..##..##.
                    #.#.##.#.
                    """;

        // when

        var line = PointOfIncidence.parseInputNotes(input);
        var actual = PointOfIncidence.findHorizontalReflection(line);

        // then
        assertEquals(0, actual);

    }


    @Test
    void getHorizontalMatch() {
        // given
        var input = """
                    #...##..#
                    #....#..#
                    ..##..###
                    #####.##.
                    #####.##.
                    ..##..###
                    #....#..#
                    """;

        // when
        var line = PointOfIncidence.parseInputNotes(input);
        var actual = PointOfIncidence.findHorizontalReflection(line);

        // then
        assertEquals(4, actual);
    }

    @Test
    void getHorizontalMatchWithSmudge() {
        // given
        var input = """
                    #.##..##.
                    ..#.##.#.
                    ##......#
                    ##......#
                    ..#.##.#.
                    ..##..##.
                    #.#.##.#.
                    """;

        // when
        var line = PointOfIncidence.parseInputNotes(input);
        var actual = PointOfIncidence.findHorizontalReflectionWithSmudge(line);

        // then
        assertEquals(3, actual);
    }

    @Test
    void getHorizontalMatchWithSmudge2() {
        // given
        var input = """
                    #...##..#
                    #....#..#
                    ..##..###
                    #####.##.
                    #####.##.
                    ..##..###
                    #....#..#
                    """;

        // when
        var line = PointOfIncidence.parseInputNotes(input);
        var actual = PointOfIncidence.findHorizontalReflectionWithSmudge(line);

        // then
        assertEquals(1, actual);
    }

    @Test
    void getNotesSummarryWithSmudge() {
        // given
        var input = """
                    #.##..##.
                    ..#.##.#.
                    ##......#
                    ##......#
                    ..#.##.#.
                    ..##..##.
                    #.#.##.#.
                                        
                    #...##..#
                    #....#..#
                    ..##..###
                    #####.##.
                    #####.##.
                    ..##..###
                    #....#..#
                    """;

        // when
        var line = PointOfIncidence.parseAllNotes(input);
        var actual = PointOfIncidence.getSummarizeWithSmudge(line);

        // then
        assertEquals(400, actual);
    }


}