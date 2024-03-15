import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PulsePropagationTest {

    @Test
    void multipleLowAndHighPulsesAfterNumberOfRequests() {
        // given
        final var input = """
                      broadcaster -> a, b, c
                      %a -> b
                      %b -> c
                      %c -> inv
                      &inv -> a
                      """;

        // when
        final var board = PulsePropagation.parseInput(input);
        final var actual = PulsePropagation.multipleLowAndHighPulsesAfterNumberOfRequests(board, 1);

        // then
        assertEquals(32, actual);
    }

    @Test
    void multipleLowAndHighPulsesAfterNumberOfRequests2() {
        // given
        final var input = """
                      broadcaster -> a, b, c
                      %a -> b
                      %b -> c
                      %c -> inv
                      &inv -> a
                      """;

        // when
        final var board = PulsePropagation.parseInput(input);
        final var actual = PulsePropagation.multipleLowAndHighPulsesAfterNumberOfRequests(board, 1000);

        // then
        assertEquals(32000000, actual);
    }

    @Test
    void multipleLowAndHighPulsesAfterNumberOfRequests3() {
        // given
        final var input = """
                      broadcaster -> a
                      %a -> inv, con
                      &inv -> b
                      %b -> con
                      &con -> output
                      """;

        // when
        final var board = PulsePropagation.parseInput(input);
        final var actual = PulsePropagation.multipleLowAndHighPulsesAfterNumberOfRequests(board, 1000);

        // then
        assertEquals(11687500, actual);
    }
}