import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class CubeConundrum {
    private static final int MAX_RED_CUBES = 12;
    private static final int MAX_GREEN_CUBES = 13;
    private static final int MAX_BLUE_CUBES = 14;

    public static void main(String[] args) throws URISyntaxException, IOException {
        final List<String> lines = Files.readAllLines(Path.of(ClassLoader.getSystemResource("input").toURI()));

        final int possibleGameSubsetsFromLines = getSumOfIdsForPossibleGames(lines);
        System.out.println(possibleGameSubsetsFromLines);

        var sumOfPowerOfCubes = getSumOfPowerOfCubes(lines);
        System.out.println(sumOfPowerOfCubes);
    }


    record CubeSubset(int red, int green, int blue) {
    }

    record Game(int id, List<CubeSubset> subset) {
    }

    static int getSumOfIdsForInput(String input) {
        return getSumOfIdsForPossibleGames(input.lines().toList());
    }

    static int getSumOfIdsForPossibleGames(Collection<String> lines) {
        return lines.stream()
                    .map(CubeConundrum::getGameStatics)
                    .filter(CubeConundrum::isGamePossible)
                    .mapToInt(game -> game.id)
                    .sum();
    }

    static int getSumOfPowersOfCubesForInput(String input) {
        return getSumOfPowerOfCubes(input.lines().toList());
    }

    static int getSumOfPowerOfCubes(Collection<String> lines) {
        return lines.stream()
                    .map(CubeConundrum::getGameStatics)
                    .mapToInt(CubeConundrum::getPowerOfGameCubes)
                    .sum();
    }



    static int getPowerOfGameCubes(Game game) {
        int minimumRedCount = 0;
        int minimumGreenCount = 0;
        int minimumBlueCount = 0;


        for (CubeSubset cubeSubset : game.subset) {
            if (cubeSubset.red > minimumRedCount) {
                minimumRedCount = cubeSubset.red;
            }
            if (cubeSubset.green > minimumGreenCount) {
                minimumGreenCount = cubeSubset.green;
            }
            if (cubeSubset.blue > minimumBlueCount) {
                minimumBlueCount = cubeSubset.blue;
            }
        }
        return minimumRedCount * minimumGreenCount * minimumBlueCount;
    }

    static boolean isGamePossible(Game game) {
        return game.subset().stream()
                   .allMatch(cubeSubset -> cubeSubset.red <= MAX_RED_CUBES &&
                           cubeSubset.green <= MAX_GREEN_CUBES &&
                           cubeSubset.blue <= MAX_BLUE_CUBES);
    }

    static Game getGameStatics(String line) {
        var gameAndSubsetLine = line.split(":");
        var game = gameAndSubsetLine[0];
        var subsetLine = gameAndSubsetLine[1];

        var gameScanner = new Scanner(game);

        gameScanner.skip("Game ");
        var gameId = gameScanner.nextInt();


        final String[] subsets = subsetLine.split(";");
        final List<CubeSubset> cubeSubsets = Stream.of(subsets).map(CubeConundrum::parseSubset).toList();

        return new Game(gameId, cubeSubsets);
    }

    static CubeSubset parseSubset(String subsetLine) {
        var redCount = 0;
        var greenCount = 0;
        var blueCount = 0;

        var subsetsScanner = new Scanner(subsetLine);
        subsetsScanner.useDelimiter("[, ]");
        while (subsetsScanner.hasNext()) {

            if (!subsetsScanner.hasNextInt()) {
                subsetsScanner.next();
                continue;
            }

            var colorCount = subsetsScanner.nextInt();
            var color = subsetsScanner.next();
            switch (color) {
                case "blue" -> blueCount = colorCount;
                case "red" -> redCount = colorCount;
                case "green" -> greenCount = colorCount;
                default -> throw new IllegalStateException("Color " + color + " unrecognized");
            }
        }
        return new CubeSubset(redCount, greenCount, blueCount);
    }

}
