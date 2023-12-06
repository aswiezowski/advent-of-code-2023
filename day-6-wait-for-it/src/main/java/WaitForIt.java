import java.util.*;
import java.util.stream.Stream;

public class WaitForIt {

    public static void main(String[] args) {
        var input = """
                    Time:        40     82     91     66
                    Distance:   277   1338   1349   1063
                    """;

        var timeAndDistances = parseRaces(input);
        var winMultiplication = calculateWinMultiplication(timeAndDistances);
        System.out.println("Win possiblities multiplication: " + winMultiplication);

        var input2 = """
                    Time:        40829166
                    Distance:   277133813491063
                    """;
        var timeAndDistances2 = parseRaces(input2);
        var winMultiplication2 = calculateWinMultiplication(timeAndDistances2);
        System.out.println("Win possiblities multiplication: " + winMultiplication2);

    }

    record TimeAndDistance(long time, long distance) {
    }


    static int calculateWinMultiplication(Collection<TimeAndDistance> timeAndDistances) {
        final List<Integer> winPossibilities = calculateWinPossibilities(timeAndDistances);
        return winPossibilities.stream().reduce(1, (x, y) -> x * y);
    }

    static List<Integer> calculateWinPossibilities(Collection<TimeAndDistance> timeAndDistances) {
        var winPossibilities = new ArrayList<Integer>();
        for (TimeAndDistance timeAndDistance : timeAndDistances) {
            int winPossibilityForRace = 0;
            for (long chargeTime = 0; chargeTime <= timeAndDistance.time; chargeTime++) {
                long leftTime = timeAndDistance.time - chargeTime;
                long distanceForCharge = leftTime * chargeTime;
                if (distanceForCharge > timeAndDistance.distance) {
                    winPossibilityForRace++;
                }
            }
            winPossibilities.add(winPossibilityForRace);
        }
        return winPossibilities;
    }

    static List<TimeAndDistance> parseRaces(String input) {
        final String[] timeAndDistance = input.split("\n");
        var times = Stream.of(timeAndDistance[0].split(" ")).filter(time -> !time.isEmpty()).toList();
        var distances = Stream.of(timeAndDistance[1].split(" ")).filter(distance -> !distance.isEmpty()).toList();

        var timeWithDistances = new ArrayList<TimeAndDistance>();
        for (int index = 1; index < times.size(); index++) {
            timeWithDistances.add(new TimeAndDistance(Long.parseLong(times.get(index)), Long.parseLong(distances.get(index))));
        }
        return timeWithDistances;
    }
}
