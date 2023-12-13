import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CamelCards {

    public static void main(String[] args) throws URISyntaxException, IOException {
        final String input = Files.readString(Path.of(ClassLoader.getSystemResource("input").toURI()));

        var hands = parseCards(input);
        final long points = getPoints(hands);
        System.out.println("Total winnings: " + points);

        var handsWithJoker = parseCardsWithJoker(input);
        final long pointsWithJoker = getPointsWithJoker(handsWithJoker);
        System.out.println("Total winnings with Joker: " + pointsWithJoker);
    }

    enum HandType {
        FIVE_OF_KIND, FOUR_OF_KIND, FULL_HOUSE, THREE_OF_KIND, TWO_PAIR, ONE_PAIR, HIGH_CARD
    }


    record Hand(String cards, long bid, HandType handType) {
    }

    static List<Hand> parseCards(String input) {
        final String[] lines = input.split("\n");
        return Stream.of(lines).map(CamelCards::parseLine).toList();
    }

    static Hand parseLine(String line) {
        final String[] cardsAndBid = line.split(" ");
        if (line.isEmpty() || cardsAndBid[0].isEmpty() || cardsAndBid[1].isEmpty()) {
            throw new IllegalStateException();
        }
        return new Hand(cardsAndBid[0], Long.parseLong(cardsAndBid[1]), getHandType(cardsAndBid[0]));
    }

    static List<Hand> parseCardsWithJoker(String input) {
        final String[] lines = input.split("\n");
        return Stream.of(lines).map(CamelCards::parseLineWithJoker).toList();
    }

    static Hand parseLineWithJoker(String line) {
        final String[] cardsAndBid = line.split(" ");
        if (line.isEmpty() || cardsAndBid[0].isEmpty() || cardsAndBid[1].isEmpty()) {
            throw new IllegalStateException();
        }
        return new Hand(cardsAndBid[0], Long.parseLong(cardsAndBid[1]), getHandTypeWithJokers(cardsAndBid[0]));
    }

    static List<Character> cardImportance = List.of('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2');
    static List<Character> cardImportanceWithJoker = List.of('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J');

    static class HandsComparator implements Comparator<Hand> {

        @Override
        public int compare(final Hand o1, final Hand o2) {
            if (o1.handType.ordinal() < o2.handType.ordinal()) {
                return 1;
            }
            if (o1.handType.ordinal() > o2.handType.ordinal()) {
                return -1;
            }
            for (int index = 0; index < o1.cards.length(); index++) {
                var o1Card = o1.cards.charAt(index);
                var o2Card = o2.cards.charAt(index);
                if (cardImportance.indexOf(o1Card) < cardImportance.indexOf(o2Card)) {
                    return 1;
                }
                if (cardImportance.indexOf(o1Card) > cardImportance.indexOf(o2Card)) {
                    return -1;
                }
            }
            return 0;
        }
    }

    static class HandsComparatorWithJoker implements Comparator<Hand> {

        @Override
        public int compare(final Hand o1, final Hand o2) {
            if (o1.handType.ordinal() < o2.handType.ordinal()) {
                return 1;
            }
            if (o1.handType.ordinal() > o2.handType.ordinal()) {
                return -1;
            }
            for (int index = 0; index < o1.cards.length(); index++) {
                var o1Card = o1.cards.charAt(index);
                var o2Card = o2.cards.charAt(index);
                if (cardImportanceWithJoker.indexOf(o1Card) < cardImportanceWithJoker.indexOf(o2Card)) {
                    return 1;
                }
                if (cardImportanceWithJoker.indexOf(o1Card) > cardImportanceWithJoker.indexOf(o2Card)) {
                    return -1;
                }
            }
            return 0;
        }
    }

    static long getPoints(Collection<Hand> hands) {
        final List<Hand> sortedHands = sortCordsByImportance(hands);
        long points = 0;
        for (int index = 0; index < sortedHands.size(); index++) {
            points = points + (index + 1) * sortedHands.get(index).bid;
        }
        return points;
    }

    static long getPointsWithJoker(Collection<Hand> hands) {
        final List<Hand> sortedHands = sortCordsByImportanceWithJokers(hands);
        long points = 0;
        for (int index = 0; index < sortedHands.size(); index++) {
            points = points + (index + 1) * sortedHands.get(index).bid;
        }
        return points;
    }

    static List<Hand> sortCordsByImportance(Collection<Hand> hands) {
        final ArrayList<Hand> sortedHands = new ArrayList<>(hands);
        final HandsComparator handsComparator = new HandsComparator();
        sortedHands.sort(handsComparator);
        return sortedHands;
    }

    static List<Hand> sortCordsByImportanceWithJokers(Collection<Hand> hands) {
        final ArrayList<Hand> sortedHands = new ArrayList<>(hands);
        final HandsComparatorWithJoker handsComparator = new HandsComparatorWithJoker();
        sortedHands.sort(handsComparator);
        return sortedHands;
    }

    static HandType getHandType(String cards) {
        final Map<Character, Long> cardCount = cards.chars().mapToObj(card -> (char) card).collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        if (cardCount.containsValue(5L)) {
            return HandType.FIVE_OF_KIND;
        }
        if (cardCount.containsValue(4L)) {
            return HandType.FOUR_OF_KIND;
        }
        if (cardCount.containsValue(3L) && cardCount.containsValue(2L)) {
            return HandType.FULL_HOUSE;
        }
        if (cardCount.containsValue(3L)) {
            return HandType.THREE_OF_KIND;
        }
        final long pairCounts = cardCount.values().stream().filter(count -> count == 2).count();
        if (pairCounts == 2) {
            return HandType.TWO_PAIR;
        }
        if (pairCounts == 1) {
            return HandType.ONE_PAIR;
        }
        if (cardCount.values().stream().allMatch(count -> count == 1)) {
            return HandType.HIGH_CARD;
        }
        throw new IllegalStateException("Incorrectly recognised hand of cards: " + cards);
    }

    static HandType getHandTypeWithJokers(String cards) {
        HandType mostImportantHandType = HandType.HIGH_CARD;
        for (char changedCard : cardImportance) {
            final String newCards = cards.replaceAll("J", String.valueOf(changedCard));
            final HandType handType = getHandType(newCards);
            if (handType.ordinal() < mostImportantHandType.ordinal()) {
                mostImportantHandType = handType;
            }
        }
        return mostImportantHandType;
    }


}
