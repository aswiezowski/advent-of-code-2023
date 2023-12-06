import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Trebuchet {

    public static void main(String[] args) throws IOException {
        final List<String> strings = Files.readAllLines(Path.of("./src/main/resources/input.txt"));
        final int sum = strings.stream().mapToInt(Trebuchet::convertToNumber).sum();
        System.out.println(sum);

    }

    static int convertToNumber(String line) {
        final String lineWithNumbers = getFirstAndLastNumber(line);
        var joinedNumber = lineWithNumbers.chars().filter(Character::isDigit)
                                          .mapToObj(Character::toString)
                                          .collect(Collectors.joining());
        if (joinedNumber.length() == 1) {
            joinedNumber = joinedNumber + joinedNumber;
        }
        if (joinedNumber.length() > 2) {
            joinedNumber = joinedNumber.charAt(0) + joinedNumber.substring(joinedNumber.length() - 1);
        }
        return Integer.parseInt(joinedNumber);
    }

   static Set<String> lookupStrings = Set.of("one", "1",
            "two", "2",
            "three", "3",
            "four", "4",
            "five", "5",
            "six", "6",
            "seven", "7",
            "eight", "8",
            "nine", "9");

    static String getFirstAndLastNumber(String line) {
        var firstLookupPosition = Integer.MAX_VALUE;
        var firstLookupString = "";
        var lastLookupPosition = Integer.MIN_VALUE;
        var lastLookupString = "";

        for (String lookup: lookupStrings){
            final int index = line.indexOf(lookup);
            if(index < firstLookupPosition && index >= 0){
                firstLookupPosition = index;
                firstLookupString = lookup;
            }
        }
        for (String lookup: lookupStrings){
            final int index = line.lastIndexOf(lookup);
            if(index > lastLookupPosition && index != firstLookupPosition && index != -1){
                lastLookupPosition = index;
                lastLookupString = lookup;
            }
        }

        return replaceTextToDigit(firstLookupString) + replaceTextToDigit(lastLookupString);
    }

    private static String replaceTextToDigit(String text){
       return text.replace("one", "1")
               .replace("two", "2")
               .replace("three", "3")
               .replace("four", "4")
               .replace("five", "5")
               .replace("six", "6")
               .replace("seven", "7")
               .replace("eight", "8")
               .replace("nine", "9");
    }
}
