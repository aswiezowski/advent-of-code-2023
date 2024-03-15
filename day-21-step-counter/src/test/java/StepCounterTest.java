import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StepCounterTest {

    @Test
    void getPositionCountAfterNumberOfSteps() {
        // given
        var input = """
                    ...........
                    .....###.#.
                    .###.##..#.
                    ..#.#...#..
                    ....#.#....
                    .##..S####.
                    .##..#...#.
                    .......##..
                    .##.#.####.
                    .##..##.##.
                    ...........
                    """;

        // given
        var map = StepCounter.parseInput(input);
        var actual = StepCounter.getPositionCountAfterNumberOfSteps(map, 6);

        // then
        assertEquals(16, actual);
    }
}