import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PipeMazeTest {


    @Test
    void getFatherst () {
        // when
        var input = """
                    .....
                    .S-7.
                    .|.|.
                    .L-J.
                    .....
                    """;

        // when
        var map = PipeMaze.parseInput(input);
        var actual = PipeMaze.traverseMap(map);

        // then
        assertEquals(4, actual);
    }

    @Test
    void getFathestPipe2() {
        // when
        var input = """
                    ..F7.
                    .FJ|.
                    SJ.L7
                    |F--J
                    LJ...
                    """;

        // when
        var map = PipeMaze.parseInput(input);
        var actual = PipeMaze.traverseMap(map);

        // then
        assertEquals(8, actual);
    }

    @Test
    void getFarthestPipeInAnotherPipes() {
        // when
        var input = """
                    -L|F7
                    7S-7|
                    L|7||
                    -L-J|
                    L|-JF
                    """;

        // when
        var map = PipeMaze.parseInput(input);
        var actual = PipeMaze.traverseMap(map);

        // then
        assertEquals(4, actual);
    }

    @Test
    void getNumberOfEnclosedTiles() {
        // when
        var input = """
                    ...........
                    .S-------7.
                    .|F-----7|.
                    .||.....||.
                    .||.....||.
                    .|L-7.F-J|.
                    .|..|.|..|.
                    .L--J.L--J.
                    ...........
                    """;

        // when
        var map = PipeMaze.parseInput(input);
        var actual = PipeMaze.getNumberOfEnclosedTiles(map);

        // then
        assertEquals(4, actual);
    }

    @Test
    void getNumberOfEnclosedTiles2() {
        // when
        var input = """
                    ..........
                    .S------7.
                    .|F----7|.
                    .||....||.
                    .||....||.
                    .|L-7F-J|.
                    .|..||..|.
                    .L--JL--J.
                    ..........
                    """;

        // when
        var map = PipeMaze.parseInput(input);
        var actual = PipeMaze.getNumberOfEnclosedTiles(map);

        // then
        assertEquals(4, actual);
    }

    @Test
    void getNumberOfEnclosedTiles3() {
        // when
        var input = """
                    .F----7F7F7F7F-7....
                    .|F--7||||||||FJ....
                    .||.FJ||||||||L7....
                    FJL7L7LJLJ||LJ.L-7..
                    L--J.L7...LJS7F-7L7.
                    ....F-J..F7FJ|L7L7L7
                    ....L7.F7||L7|.L7L7|
                    .....|FJLJ|FJ|F7|.LJ
                    ....FJL-7.||.||||...
                    ....L---J.LJ.LJLJ...
                    """;

        // when
        var map = PipeMaze.parseInput(input);
        var actual = PipeMaze.getNumberOfEnclosedTiles(map);

        // then
        assertEquals(8, actual);
    }

    @Test
    void getNumberOfEnclosedTiles4() {
        // when
        var input = """
                    FF7FSF7F7F7F7F7F---7
                    L|LJ||||||||||||F--J
                    FL-7LJLJ||||||LJL-77
                    F--JF--7||LJLJ7F7FJ-
                    L---JF-JLJ.||-FJLJJ7
                    |F|F-JF---7F7-L7L|7|
                    |FFJF7L7F-JF7|JL---7
                    7-L-JL7||F7|L7F-7F7|
                    L.L7LFJ|||||FJL7||LJ
                    L7JLJL-JLJLJL--JLJ.L
                    """;

        // when
        var map = PipeMaze.parseInput(input);
        var actual = PipeMaze.getNumberOfEnclosedTiles(map);

        // then
        assertEquals(10, actual);
    }



}