import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HauntedWastelandTest {


    @Test
    void getLengthCalculatesCorrectly() {
        // when
        var input = """
                    RL
                                        
                    AAA = (BBB, CCC)
                    BBB = (DDD, EEE)
                    CCC = (ZZZ, GGG)
                    DDD = (DDD, DDD)
                    EEE = (EEE, EEE)
                    GGG = (GGG, GGG)
                    ZZZ = (ZZZ, ZZZ)
                    """;

        // when
        var desertMap = HauntedWasteland.parseInput(input);
        var actual = HauntedWasteland.findClosestPath(desertMap);

        // then
        assertEquals(2, actual);
    }

    @Test
    void getLengthCalculatesCorrectlyForLoopingDirections() {
        // when
        var input = """
                    LLR
                                        
                    AAA = (BBB, BBB)
                    BBB = (AAA, ZZZ)
                    ZZZ = (ZZZ, ZZZ)
                    """;

        // when
        var desertMap = HauntedWasteland.parseInput(input);
        var actual = HauntedWasteland.findClosestPath(desertMap);

        // then
        assertEquals(6, actual);
    }

    @Test
    void getLengthCalculatesCorrectlyForMultipleNodes() {
        // when
        var input = """
                    LR
                                        
                    11A = (11B, XXX)
                    11B = (XXX, 11Z)
                    11Z = (11B, XXX)
                    22A = (22B, XXX)
                    22B = (22C, 22C)
                    22C = (22Z, 22Z)
                    22Z = (22B, 22B)
                    XXX = (XXX, XXX)
                    """;

        // when
        var desertMap = HauntedWasteland.parseInput(input);
        var actual = HauntedWasteland.findClosestPathForMultipleNodes(desertMap);

        // then
        assertEquals(6, actual);
    }
}