import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ParabolicReflectorDish {

    public static void main(String[] args) throws URISyntaxException, IOException {
        final String input = Files.readString(Path.of(ClassLoader.getSystemResource("input").toURI()));

        var dish = parseInput(input);
        var tiltedDish = tiltNorth(dish);
        var load = calculateLoad(tiltedDish);

        System.out.println("Sum of load: " + load);

        dish = parseInput(input);
        cycleNumberOfTimes(dish, 1000);
        var loadAfterCycles = calculateLoad(dish);
        System.out.println("Sum of load after cycles: " + loadAfterCycles);

    }

    static List<List<Character>> parseInput(String input) {
        return Arrays.stream(input.split("\n"))
                     .map(line -> line.chars().mapToObj(item -> (char) item).collect(Collectors.toList()))
                     .toList();
    }

    static Map<List<List<Character>>, List<Integer>> cycleToLoopNumber = new HashMap<>();

    static void cycleNumberOfTimes(List<List<Character>> dish, int numberOfCycles) {
        for (int index = 0; index < numberOfCycles; index++) {
            cycle(dish);
            if (!cycleToLoopNumber.containsKey(dish)) {
                cycleToLoopNumber.put(dish, new ArrayList<>());
            }
            cycleToLoopNumber.get(dish).add(index);
        }
        cycleToLoopNumber.entrySet().forEach(cycle -> {
//            System.out.println(getStringRepresentation(cycle.getKey()));
            System.out.println(cycle.getValue() + "\n");
        });
        System.out.println();
        System.out.println(cycleToLoopNumber.size());
    }

    static void cycle(List<List<Character>> dish) {
        tiltNorth(dish);
        tiltWest(dish);
        tiltSouth(dish);
        tiltEast(dish);
    }

    static List<List<Character>> tiltNorth(List<List<Character>> newDish) {
        for (int yIndex = 1; yIndex < newDish.size(); yIndex++) {
            var currentLane = newDish.get(yIndex);
            for (int xIndex = 0; xIndex < newDish.get(yIndex).size(); xIndex++) {
                if (currentLane.get(xIndex) == 'O') {
                    int changedYIndex = Integer.MAX_VALUE;
                    for (int newYIndex = yIndex - 1; newYIndex >= 0 && newDish.get(newYIndex).get(xIndex) == '.'; newYIndex--) {
                        changedYIndex = newYIndex;
                    }
                    if (changedYIndex != Integer.MAX_VALUE) {
                        newDish.get(changedYIndex).set(xIndex, 'O');
                        newDish.get(yIndex).set(xIndex, '.');
                    }
                }
            }
        }
        return newDish;
    }

    static List<List<Character>> tiltSouth(List<List<Character>> dish) {
        for (int y = dish.size() - 2; y >= 0; y--) {
            for (int x = 0; x < dish.get(y).size(); x++) {
                if (dish.get(y).get(x) == 'O') {
                    int changedYIndex = Integer.MAX_VALUE;
                    for (int newYIndex = y + 1; newYIndex < dish.size() && dish.get(newYIndex).get(x) == '.'; newYIndex++) {
                        changedYIndex = newYIndex;
                    }
                    if (changedYIndex != Integer.MAX_VALUE) {
                        dish.get(changedYIndex).set(x, 'O');
                        dish.get(y).set(x, '.');
                    }
                }
            }
        }
        return dish;
    }

    static List<List<Character>> tiltEast(List<List<Character>> dish) {
        for (int x = dish.iterator().next().size() - 2; x >= 0; x--) {
            for (int y = 0; y < dish.size(); y++) {
                if (dish.get(y).get(x) == 'O') {
                    int changedXIndex = Integer.MAX_VALUE;
                    for (int newXIndex = x + 1; newXIndex < dish.iterator().next().size() && dish.get(y).get(newXIndex) == '.'; newXIndex++) {
                        changedXIndex = newXIndex;
                    }
                    if (changedXIndex != Integer.MAX_VALUE) {
                        dish.get(y).set(changedXIndex, 'O');
                        dish.get(y).set(x, '.');
                    }
                }
            }
        }
        return dish;
    }

    static List<List<Character>> tiltWest(List<List<Character>> dish) {
        for (int x = 1; x < dish.iterator().next().size(); x++) {
            for (int y = 0; y < dish.size(); y++) {
                if (dish.get(y).get(x) == 'O') {
                    int changedXIndex = Integer.MAX_VALUE;
                    for (int newXIndex = x - 1; newXIndex >= 0 && dish.get(y).get(newXIndex) == '.'; newXIndex--) {
                        changedXIndex = newXIndex;
                    }
                    if (changedXIndex != Integer.MAX_VALUE) {
                        dish.get(y).set(changedXIndex, 'O');
                        dish.get(y).set(x, '.');
                    }
                }
            }
        }
        return dish;
    }

    static long calculateLoad(List<List<Character>> dish) {
        var load = 0L;

        for (int y = 0; y < dish.size(); y++) {
            for (int x = 0; x < dish.get(y).size(); x++) {
                if (dish.get(y).get(x).equals('O')) {
                    load += dish.size() - y;
                }
            }
        }
        return load;
    }

    static String getStringRepresentation(List<List<Character>> dish) {
        return dish.stream().map(list -> list.stream().map(String::valueOf).collect(Collectors.joining(""))).collect(Collectors.joining("\n"));
    }

}
