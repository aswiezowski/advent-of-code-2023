import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MirageMaintenanceTest {


    @Test
    void getExtrapolatedNumber() {
        // when
        var input = """
                    0 3 6 9 12 15
                    1 3 6 10 15 21
                    10 13 16 21 30 45
                    """;

        // when
        var history = MirageMaintenance.parseInput(input);
        var actual1 = MirageMaintenance.extrapolateNumberRight(history.get(0));
        var actual2 = MirageMaintenance.extrapolateNumberRight(history.get(1));
        var actual3 = MirageMaintenance.extrapolateNumberRight(history.get(2));

        // then
        assertEquals(18, actual1);
        assertEquals(28, actual2);
        assertEquals(68, actual3);
    }

    @Test
    void getExtrapolatedNumberLeft() {
        // when
        var input = """
                    0 3 6 9 12 15
                    1 3 6 10 15 21
                    10 13 16 21 30 45
                    """;

        // when
        var history = MirageMaintenance.parseInput(input);
        var actual1 = MirageMaintenance.extrapolateNumberLeft(history.get(0));
        var actual3 = MirageMaintenance.extrapolateNumberLeft(history.get(2));

        // then
        assertEquals(-3, actual1);
        assertEquals(5, actual3);
    }

    @Test
    void getSumOfExtrapolatedNumbers() {
        // when
        var input = """
                    0 3 6 9 12 15
                    1 3 6 10 15 21
                    10 13 16 21 30 45
                    """;

        // when
        var history = MirageMaintenance.parseInput(input);
        var actual = MirageMaintenance.getSumOfRightExtrapolatedNumbers(history);

        // then
        assertEquals(114, actual);
    }

}