import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class PipeMaze {

    public static void main(String[] args) throws URISyntaxException, IOException {
        final String input = Files.readString(Path.of(ClassLoader.getSystemResource("input").toURI()));

        var map = parseInput(input);
        var farthestPoint = traverseMap(map);
        System.out.println("Farthest point: " + farthestPoint);
        System.out.println("Enclosed tiles: " + getNumberOfEnclosedTiles(map));

    }

    record Position(int x, int y) {

    }

    record PositionWithLastDirection(Position position, Direction direction) {

    }

    static List<List<Character>> parseInput(String input) {
        final String[] lines = input.split("\n");
        var map = Stream.of(lines)
                        .map(line -> line.chars().mapToObj(intChar -> (char) intChar).toList())
                        .toList();
        return map;
    }

    static Position findStartPosition(List<List<Character>> map) {
        for (int yPosition = 0; yPosition < map.size(); yPosition++) {
            var yLine = map.get(yPosition);
            for (int xPosition = 0; xPosition < yLine.size(); xPosition++) {
                if (yLine.get(xPosition).equals('S')) {
                    return new Position(xPosition, yPosition);
                }
            }
        }
        throw new IllegalStateException("Start position not found");
    }


    enum Direction {NORTH, SOUTH, EAST, WEST;}

    static Map<Character, List<Direction>> pipeToDirection = Map.of(
            '|', List.of(Direction.NORTH, Direction.SOUTH),
            '-', List.of(Direction.EAST, Direction.WEST),
            'L', List.of(Direction.NORTH, Direction.EAST),
            'J', List.of(Direction.NORTH, Direction.WEST),
            '7', List.of(Direction.SOUTH, Direction.WEST),
            'F', List.of(Direction.SOUTH, Direction.EAST),
            '.', List.of(),
            'S', List.of(Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.NORTH));

    static Map<Direction, Direction> connectedDirections = Map.of(Direction.EAST, Direction.WEST,
            Direction.WEST, Direction.EAST,
            Direction.NORTH, Direction.SOUTH,
            Direction.SOUTH, Direction.NORTH);

    static long traverseMap(List<List<Character>> map) {
        var startPosition = new PositionWithLastDirection(findStartPosition(map), null);
        var currentPositions = List.of(startPosition);

        var maxLength = 0L;

        var alreadyVisitedPositions = new ArrayList<Position>();
        while (currentPositions.stream().map(PositionWithLastDirection::position)
                               .noneMatch(alreadyVisitedPositions::contains) &&
                !(currentPositions.stream().map(PositionWithLastDirection::position).distinct().count() == 1 &&
                        currentPositions.size() > 1)
        ) {
            alreadyVisitedPositions.addAll(currentPositions.stream().map(PositionWithLastDirection::position).toList());
            currentPositions = currentPositions.stream()
                                               .flatMap(position -> getNextPositions(map, position).stream())
                                               .toList();
            maxLength++;
        }
        return maxLength;
    }

    static List<PositionWithLastDirection> getNextPositions(List<List<Character>> map, PositionWithLastDirection currentPositionWithLastDirection) {
        var currentPosition = currentPositionWithLastDirection.position;
        var lastDirection = currentPositionWithLastDirection.direction;
        final Character currentPipe = map.get(currentPosition.y).get(currentPosition.x);
        final List<Direction> directions = pipeToDirection.get(currentPipe);

        var nextPositions = new ArrayList<PositionWithLastDirection>();

        if (directions.contains(Direction.WEST) && currentPosition.x - 1 >= 0 && lastDirection != Direction.EAST) {
            final Character possibleNextPipe = map.get(currentPosition.y).get(currentPosition.x - 1);
            if (pipeToDirection.get(possibleNextPipe).contains(Direction.EAST)) {
                nextPositions.add(new PositionWithLastDirection(new Position(currentPosition.x - 1, currentPosition.y), Direction.WEST));
            }
        }

        if (directions.contains(Direction.EAST) && currentPosition.x + 1 < map.iterator().next().size() && lastDirection != Direction.WEST) {
            final Character possibleNextPipe = map.get(currentPosition.y).get(currentPosition.x + 1);
            if (pipeToDirection.get(possibleNextPipe).contains(Direction.WEST)) {
                nextPositions.add(new PositionWithLastDirection(new Position(currentPosition.x + 1, currentPosition.y), Direction.EAST));
            }
        }

        if (directions.contains(Direction.NORTH) && currentPosition.y - 1 >= 0 && lastDirection != Direction.SOUTH) {
            final Character possibleNextPipe = map.get(currentPosition.y - 1).get(currentPosition.x);
            if (pipeToDirection.get(possibleNextPipe).contains(Direction.SOUTH)) {
                nextPositions.add(new PositionWithLastDirection(new Position(currentPosition.x, currentPosition.y - 1), Direction.NORTH));
            }
        }

        if (directions.contains(Direction.SOUTH) && currentPosition.y + 1 < map.size() && lastDirection != Direction.NORTH) {
            final Character possibleNextPipe = map.get(currentPosition.y + 1).get(currentPosition.x);
            if (pipeToDirection.get(possibleNextPipe).contains(Direction.NORTH)) {
                nextPositions.add(new PositionWithLastDirection(new Position(currentPosition.x, currentPosition.y + 1), Direction.SOUTH));
            }
        }

        return nextPositions;
    }


    // Ray casting algorithm
    public static long getNumberOfEnclosedTiles(final List<List<Character>> map) {
        final List<Character> horizontalList = List.of('|', 'F', 'L', 'S', 'J', '7');
        final List<Character> verticalIndicator = List.of('-', 'F', 'L');
        final List<Position> allPointsOfMaze = getAllPointsOfMaze(map);

        final int xSize = map.iterator().next().size();
        long numberInsideTiles = 0;
        for (int posX = 0; posX < xSize; posX++) {
            for (int posY = 0; posY < map.size(); posY++) {
                if (map.get(posY).get(posX) == '.' || !allPointsOfMaze.contains(new Position(posX, posY))) {
                    int numberOfCrossedSides = 0;
                    for (int restOfX = posX + 1; restOfX < xSize; restOfX++) {
                        if (allPointsOfMaze.contains(new Position(restOfX, posY))) {
                            var nextTile = map.get(posY).get(restOfX);
                            if (nextTile.equals('|')) {
                                numberOfCrossedSides++;
                            } else if (horizontalList.contains(nextTile)) {

                                /*
                                If the intersection point is a vertex of a tested polygon side, then the intersection counts only if the other vertex of the side lies below the ray. This is effectively equivalent to considering vertices on the ray as lying slightly above the ray.
                                 */
                                var nextPositions = getNextPositions(map, new PositionWithLastDirection(new Position(restOfX, posY), null));
                                if (nextPositions.stream().anyMatch(pos -> pos.direction == Direction.SOUTH)) {
                                    numberOfCrossedSides++;
                                }
                            }
                        }

                    }
                    if (numberOfCrossedSides % 2 == 1) {
                        numberInsideTiles++;
                        System.out.println("Tile " + posX + " " + posY);
                    }
                }
            }
        }
        return numberInsideTiles;
    }

    static List<Position> getAllPointsOfMaze(List<List<Character>> map) {
        var startPosition = new PositionWithLastDirection(findStartPosition(map), null);
        var currentPositions = List.of(startPosition);

        var maxLength = 0L;

        var alreadyVisitedPositions = new ArrayList<Position>();
        while (currentPositions.stream().map(PositionWithLastDirection::position)
                               .noneMatch(alreadyVisitedPositions::contains) &&
                !(currentPositions.stream().map(PositionWithLastDirection::position).distinct().count() == 1 &&
                        currentPositions.size() > 1)
        ) {
            alreadyVisitedPositions.addAll(currentPositions.stream().map(PositionWithLastDirection::position).toList());
            currentPositions = currentPositions.stream()
                                               .flatMap(position -> getNextPositions(map, position).stream())
                                               .toList();
            maxLength++;
        }
        alreadyVisitedPositions.addAll(currentPositions.stream().map(PositionWithLastDirection::position).toList());
        return alreadyVisitedPositions.stream().distinct().toList();
    }

}
