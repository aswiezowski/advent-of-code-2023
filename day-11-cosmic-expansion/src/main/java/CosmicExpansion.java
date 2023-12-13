import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class CosmicExpansion {

    public static void main(String[] args) throws URISyntaxException, IOException {
        final String input = Files.readString(Path.of(ClassLoader.getSystemResource("input").toURI()));

        var map = parseInput(input);
        var sumOfDistances = calculateAllDistancesSum(map, 2);
        System.out.println("Sum of distances: " + sumOfDistances);
        var sumOfDistances2 = calculateAllDistancesSum(map, 1_000_000);
        System.out.println("Sum of distances with expansion rate 1 000 000: " + sumOfDistances2);

    }

    record Position(int x, int y) {

    }

    static List<List<Character>> parseInput(String input) {
        final String[] lines = input.split("\n");
        var map = Stream.of(lines)
                        .map(line -> line.chars().mapToObj(intChar -> (char) intChar).toList())
                        .toList();
        return map;
    }

    static long calculateAllDistancesSum(List<List<Character>> map, final int expansionRate) {
        var allPositions = getAllPositions(map);


        var galaxyPairs = getGalaxyPairs(allPositions);

        List<Long> distances = new ArrayList<>();

        var emptyRows = getEmptyRows(map);
        var emptyColumns = getEmptyColumns(map);

        for (var pair : galaxyPairs.entrySet()) {
            for (var galaxy2 : pair.getValue()) {
                final Position galaxy1 = pair.getKey();
                final long distance = getDistanceBetweenGalaxies(galaxy2, galaxy1, emptyRows, emptyColumns, expansionRate);
                distances.add(distance);
            }
        }
        return distances.stream().mapToLong(distance -> distance).sum();
    }

    static long getDistanceBetweenGalaxies(final Position galaxy2, final Position galaxy1, final List<Integer> emptyRows, final List<Integer> emptyColumns,
                                           final int expansionRate) {
        final int minX = Math.min(galaxy1.x, galaxy2.x);
        final int maxX = Math.max(galaxy1.x, galaxy2.x);
        final int minY = Math.min(galaxy1.y, galaxy2.y);
        final int maxY = Math.max(galaxy1.y, galaxy2.y);

        var xDistance = maxX - minX;
        var yDistance = maxY - minY;

        final long crossedEmptyRows = emptyRows.stream().filter(row -> row > minY && row < maxY).count();
        final long crossedEmptyColumns = emptyColumns.stream().filter(column -> column > minX && column < maxX).count();

        return xDistance + yDistance + crossedEmptyRows * (expansionRate - 1) + crossedEmptyColumns * (expansionRate - 1);
    }

    private static HashMap<Position, List<Position>> getGalaxyPairs(final List<Position> allPositions) {
        var galaxyPairs = new HashMap<Position, List<Position>>();
        for (int index = 0; index < allPositions.size(); index++) {
            for (int index2 = index + 1; index2 < allPositions.size(); index2++) {
                final Position galaxy1 = allPositions.get(index);
                final Position galaxy2 = allPositions.get(index2);
                if (galaxyPairs.getOrDefault(galaxy1, List.of()).stream().noneMatch(galaxy2::equals) &&
                        galaxyPairs.getOrDefault(galaxy2, List.of()).stream().noneMatch(galaxy1::equals)) {
                    final List<Position> positions = galaxyPairs.getOrDefault(galaxy1, new ArrayList<>());
                    positions.add(galaxy2);
                    galaxyPairs.put(galaxy1, positions);
                }
            }
        }
        return galaxyPairs;
    }

    static List<Position> getAllPositions(List<List<Character>> map) {
        var galaxyPositions = new ArrayList<Position>();

        final int xSize = map.iterator().next().size();
        for (int yPosition = 0; yPosition < map.size(); yPosition++) {
            for (int xPosition = 0; xPosition < xSize; xPosition++) {
                if (map.get(yPosition).get(xPosition) == '#') {
                    galaxyPositions.add(new Position(xPosition, yPosition));
                }
            }
        }
        return galaxyPositions;
    }

    static List<Integer> getEmptyRows(List<List<Character>> map) {
        var emptyRows = new ArrayList<Integer>();

        for (int yPosition = 0; yPosition < map.size(); yPosition++) {
            if (map.get(yPosition).stream().allMatch(space -> space == '.')) {
                emptyRows.add(yPosition);
            }
        }
        return emptyRows;
    }

    static List<Integer> getEmptyColumns(List<List<Character>> map) {
        var emptyColumns = new ArrayList<Integer>();

        final int xSize = map.iterator().next().size();
        for (int xPosition = 0; xPosition < xSize; xPosition++) {

            final int finalXPosition = xPosition;
            if (map.stream().map(characters -> characters.get(finalXPosition)).allMatch(space -> space == '.')) {
                emptyColumns.add(xPosition);
            }
        }
        return emptyColumns;
    }


}
