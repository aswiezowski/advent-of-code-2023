import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StepCounter {

    public static void main(String[] args) throws URISyntaxException, IOException {
        final String input = Files.readString(Path.of(ClassLoader.getSystemResource("input").toURI()));

        var map = parseInput(input);
        final var positionCountAfterNumberOfSteps = getPositionCountAfterNumberOfSteps(map, 64);
        System.out.println("Garden plots in 64 steps: " + positionCountAfterNumberOfSteps);
    }

    record Position(int x, int y) {
    }

    static List<List<Character>> parseInput(String input) {
        final String[] lines = input.split("\n");
        return Stream.of(lines)
                     .map(line -> line.chars().mapToObj(intChar -> (char) intChar).toList())
                     .toList();
    }


    static int getPositionCountAfterNumberOfSteps(List<List<Character>> map, int numberOfSteps){
        final var startPosition = findStartPosition(map);
        List<Position> nextPositions = new ArrayList<>();
        nextPositions.add(startPosition);

        for (int i=0;i<numberOfSteps;i++){
            nextPositions = getNextPositions(map, nextPositions);
        }
        return nextPositions.size();
    }

    static List<Position> getNextPositions(List<List<Character>> map, List<Position> positions) {
        final var nextPositions = new ArrayList<Position>();
        for (var position : positions) {
            if (position.x > 0) {
                int nextX = position.x - 1;
                final var newPosition = new Position(nextX, position.y);
                if (canBeMoved(map, newPosition)) {
                    nextPositions.add(newPosition);
                }
            }
            if (position.x < map.iterator().next().size() - 1) {
                int nextX = position.x + 1;
                final var newPosition = new Position(nextX, position.y);
                if (canBeMoved(map, newPosition)) {
                    nextPositions.add(newPosition);
                }
            }
            if (position.y > 0) {
                int nextY = position.y - 1;
                final var newPosition = new Position(position.x, nextY);
                if (canBeMoved(map, newPosition)) {
                    nextPositions.add(newPosition);
                }
            }
            if (position.y < map.size() - 1) {
                int nextY = position.y + 1;
                final var newPosition = new Position(position.x, nextY);
                if (canBeMoved(map, newPosition)) {
                    nextPositions.add(newPosition);
                }
            }
        }
        return nextPositions.stream().distinct().toList();
    }

    static boolean canBeMoved(List<List<Character>> map, Position position) {
        return map.get(position.y).get(position.x) != '#';
    }

    static Position findStartPosition(List<List<Character>> map) {
        for (int y = 0; y < map.size(); y++) {
            for (int x = 0; x < map.get(y).size(); x++) {
                if (map.get(y).get(x) == 'S') {
                    return new Position(x, y);
                }
            }
        }
        throw new IllegalStateException("Start not found");
    }
}
