import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParabolicReflectorDishTest {


    @Test
    void getSumOfLoadValue() {
        // given
        var input = """
                    O....#....
                    O.OO#....#
                    .....##...
                    OO.#O....O
                    .O.....O#.
                    O.#..O.#.#
                    ..O..#O..O
                    .......O..
                    #....###..
                    #OO..#....
                    """;

        // when
        var dish = ParabolicReflectorDish.parseInput(input);
        var dish2 = ParabolicReflectorDish.tiltNorth(dish);
        var actual = ParabolicReflectorDish.calculateLoad(dish2);

        // then
        assertEquals(136, actual);
    }


    @Test
    void get1Cycle() {
        // given
        var input = """
                    O....#....
                    O.OO#....#
                    .....##...
                    OO.#O....O
                    .O.....O#.
                    O.#..O.#.#
                    ..O..#O..O
                    .......O..
                    #....###..
                    #OO..#....
                    """;

        // when
        var dish = ParabolicReflectorDish.parseInput(input);
        ParabolicReflectorDish.cycle(dish);

        // then
        assertEquals("""
                     .....#....
                     ....#...O#
                     ...OO##...
                     .OO#......
                     .....OOO#.
                     .O#...O#.#
                     ....O#....
                     ......OOOO
                     #...O###..
                     #..OO#....
                     """.trim(), dish.stream().map(list -> list.stream().map(String::valueOf).collect(Collectors.joining(""))).collect(Collectors.joining("\n")));
    }


    @Test
    void getSumOfLoadValueAfter1000000000Cylces() {
        // given
        var input = """
                    O....#....
                    O.OO#....#
                    .....##...
                    OO.#O....O
                    .O.....O#.
                    O.#..O.#.#
                    ..O..#O..O
                    .......O..
                    #....###..
                    #OO..#....
                    """;

        // when
        var dish = ParabolicReflectorDish.parseInput(input);
        ParabolicReflectorDish.cycleNumberOfTimes(dish, 1000);
        var actual = ParabolicReflectorDish.calculateLoad(dish);

        // then
        assertEquals(64, actual);
    }




}