import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LensLibrary {

    public static void main(String[] args) throws URISyntaxException, IOException {
        final String input = Files.readString(Path.of(ClassLoader.getSystemResource("input").toURI()));

        final List<String> texts = parseInput(input);
        final long sumOfHash = getSumOfHash(texts);
        System.out.println("Sum of hash: " + sumOfHash);

        final long sumOfBoxes = getSumOfBoxes(texts);
        System.out.println("Sum of boxes: " + sumOfBoxes);
    }

    static List<String> parseInput(String input) {
        return Arrays.stream(input.trim().split(",")).toList();
    }

    static long getSumOfHash(List<String> texts) {
        return texts.stream().mapToLong(LensLibrary::calculateHash).sum();
    }


    static long calculateHash(String text) {
        var currentValue = 0L;
        for (int character : text.chars().toArray()) {
            currentValue = (currentValue + character) * 17 % 256;
        }
        return currentValue;
    }

    record Lens(String label, int focalLength) {
    }

    static HashMap<Integer, List<Lens>> boxes = new HashMap<>();

    static long getSumOfBoxes(List<String> texts) {
        for (String text : texts) {
            if (text.endsWith("-")) {
                final String label = text.substring(0, text.length() - 1);
                final int boxNumber = (int) calculateHash(label);
                final List<Lens> boxLenses = boxes.getOrDefault(boxNumber, new ArrayList<>());
                boxLenses.removeIf(lens -> lens.label.equals(label));
                boxes.put(boxNumber, boxLenses);
            } else {
                final String[] labelAndFocalNumber = text.split("=");
                final String label = labelAndFocalNumber[0];
                final int boxNumber = (int) calculateHash(label);
                final List<Lens> boxLenses = boxes.getOrDefault(boxNumber, new ArrayList<>());
                final Integer index = getIndexOfLens(boxLenses, label);
                if (index!=null){
                    boxLenses.set(index, new Lens(label, Integer.parseInt(labelAndFocalNumber[1])));
                }
                else{
                    boxLenses.add(new Lens(label, Integer.parseInt(labelAndFocalNumber[1])));

                }
                boxes.put(boxNumber, boxLenses);
            }
        }
        var sum = 0L;
        for (var box : boxes.entrySet()) {
            var sumOfBox = 0;
            for (int index = 0; index < box.getValue().size(); index++) {
                sumOfBox += (box.getKey() + 1L) * (index + 1L) * box.getValue().get(index).focalLength;
            }
            sum += sumOfBox;
        }
        return sum;
    }

    private static Integer getIndexOfLens(final List<Lens> boxLenses, final String label) {
        for(int index = 0; index < boxLenses.size(); index++){
            if (boxLenses.get(index).label.equals(label)){
                return index;
            }
        }
        return null;
    }

}
