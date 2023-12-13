import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HauntedWasteland {

    public static void main(String[] args) throws URISyntaxException, IOException {
        final String input = Files.readString(Path.of(ClassLoader.getSystemResource("input").toURI()));

        var desertMap = parseInput(input);
        var pathLength = findClosestPath(desertMap);
        System.out.println("Path length: " + pathLength);

        var pathLengthForMultipleNodes = findClosestPathForMultipleNodes(desertMap);
        System.out.println("Path length for multiple: " + pathLengthForMultipleNodes);
    }


    record DesertMap(List<Character> directions, Map<String, Connection> connections) {
    }

    record Connection(String left, String right) {

    }

    static Pattern pattern = Pattern.compile("(.*) = \\((.*), (.*)\\)");

    static DesertMap parseInput(String input) {
        final String[] lines = input.split("\n");
        final Map<String, Connection> connections = Stream.of(lines).skip(2)
                                                          .collect(Collectors.toMap(s -> s.substring(0, 3),
                                                                  s -> new Connection(s.substring(7, 10), s.substring(12, 15))));
        final List<Character> directions = lines[0].chars().mapToObj(direction -> (char) direction).collect(Collectors.toList());
        return new DesertMap(directions, connections);
    }


    static final String FINAL_NODE = "ZZZ";

    static long findClosestPath(DesertMap desertMap) {
        long pathLength = 0;
        var connections = desertMap.connections;
        var currentNode = "AAA";
        var directionsIterator = desertMap.directions.iterator();
        while (!currentNode.equals(FINAL_NODE)) {
            pathLength++;
            final Character next = directionsIterator.next();
            if (next == 'L') {
                currentNode = connections.get(currentNode).left;
            } else {
                currentNode = connections.get(currentNode).right;
            }
            if (!directionsIterator.hasNext()) {
                directionsIterator = desertMap.directions.iterator();
            }
        }
        return pathLength;
    }

    static long findClosestPathForMultipleNodes(DesertMap desertMap) {
        var currentNodes = desertMap.connections.keySet().stream().filter(node -> node.endsWith("A"))
                                                .map(node -> findClosestPathWithEndingZ(desertMap, node)).toList();
        System.out.println(currentNodes);

        return leastCommonMultiplier(currentNodes);
    }

    static long findClosestPathWithEndingZ(DesertMap desertMap, String currentNode) {
        long pathLength = 0;
        var connections = desertMap.connections;
        var directionsIterator = desertMap.directions.iterator();
        while (!currentNode.endsWith("Z")) {
            pathLength++;
            final Character next = directionsIterator.next();
            if (next == 'L') {
                currentNode = connections.get(currentNode).left;
            } else {
                currentNode = connections.get(currentNode).right;
            }
            if (!directionsIterator.hasNext()) {
                directionsIterator = desertMap.directions.iterator();
            }
        }

        return pathLength;
    }

    private static long greatestCommonDivisor(long x, long y) {
        return (y == 0) ? x : greatestCommonDivisor(y, x % y);
    }


    // works because path restarts after going to end path
    public static long leastCommonMultiplier(List<Long> numbers) {
        return numbers.stream().reduce(1L, (x, y) -> x * (y / greatestCommonDivisor(x, y)));
    }

}
