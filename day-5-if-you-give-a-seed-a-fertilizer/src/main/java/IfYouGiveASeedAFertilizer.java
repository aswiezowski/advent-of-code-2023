import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class IfYouGiveASeedAFertilizer {


    public static void main(String[] args) throws URISyntaxException, IOException {
        final String lines = Files.readString(Path.of(ClassLoader.getSystemResource("input").toURI()));

        final Data data = parseLines(lines);
        var lowestLocationNumber = getLowestLocationNumber(data);
        System.out.println("Lowest location number: " + lowestLocationNumber);

        final DataWithSeedRanges dataWithSeedRanges = parseLinesWithSeedRange(lines);
        var lowestLocationNumberForRanges = getLowestLocationNumber(dataWithSeedRanges);
        System.out.println("Lowest location number for seed ranges: " + lowestLocationNumberForRanges);
    }

    record Data(List<Long> seeds, List<MappingRange> seedToSoil, List<MappingRange> soilToFertilizer, List<MappingRange> fertilizerToWater,
                List<MappingRange> waterToLight, List<MappingRange> lightToTemperature, List<MappingRange> temperatureToHumidity,
                List<MappingRange> humidityToLocation) {

    }

    record DataWithSeedRanges(List<Range> seedRanges, List<MappingRange> seedToSoil, List<MappingRange> soilToFertilizer, List<MappingRange> fertilizerToWater,
                              List<MappingRange> waterToLight, List<MappingRange> lightToTemperature, List<MappingRange> temperatureToHumidity,
                              List<MappingRange> humidityToLocation) {

    }

    record MappingRange(Range source, Range destination) {
    }

    record Range(long start, long end) {

        Range {
            if (start == end) {
                throw new IllegalStateException();
            }
        }

        Range getIntersection(Range range) {
            final long newStart = Math.max(start, range.start);
            final long newEnd = Math.min(end, range.end);
            if (newStart < newEnd) {
                return new Range(newStart, newEnd);
            } else {
                return null;
            }
        }

    }

    static Data parseLines(String lines) {
        var seeds = new ArrayList<Long>();
        List<MappingRange> seedToSoil = null, soilToFertilizer = null, fertilizerToWater = null, waterToLight = null, lightToTemperature = null, temperatureToHumidity = null,
                humidityToLocation = null;

        try (Scanner scanner = new Scanner(lines)) {
            scanner.skip("seeds:");
            while (scanner.hasNextLong()) {
                seeds.add(scanner.nextLong());
            }

            while (scanner.hasNext()) {
                final String next = scanner.next();
                switch (next) {
                    case "seed-to-soil" -> seedToSoil = parseRanges(scanner);
                    case "soil-to-fertilizer" -> soilToFertilizer = parseRanges(scanner);
                    case "fertilizer-to-water" -> fertilizerToWater = parseRanges(scanner);
                    case "water-to-light" -> waterToLight = parseRanges(scanner);
                    case "light-to-temperature" -> lightToTemperature = parseRanges(scanner);
                    case "temperature-to-humidity" -> temperatureToHumidity = parseRanges(scanner);
                    case "humidity-to-location" -> humidityToLocation = parseRanges(scanner);
                }
            }
        }
        return new Data(seeds, seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation);
    }

    static DataWithSeedRanges parseLinesWithSeedRange(String lines) {
        var seedsRanges = new ArrayList<Range>();
        List<MappingRange> seedToSoil = null, soilToFertilizer = null, fertilizerToWater = null, waterToLight = null, lightToTemperature = null, temperatureToHumidity = null,
                humidityToLocation = null;

        try (Scanner scanner = new Scanner(lines)) {
            scanner.skip("seeds:");
            while (scanner.hasNextLong()) {
                var startOfRange = scanner.nextLong();
                var length = scanner.nextLong();
                seedsRanges.add(new Range(startOfRange, startOfRange + length));
            }

            while (scanner.hasNext()) {
                final String next = scanner.next();
                switch (next) {
                    case "seed-to-soil" -> seedToSoil = parseRanges(scanner);
                    case "soil-to-fertilizer" -> soilToFertilizer = parseRanges(scanner);
                    case "fertilizer-to-water" -> fertilizerToWater = parseRanges(scanner);
                    case "water-to-light" -> waterToLight = parseRanges(scanner);
                    case "light-to-temperature" -> lightToTemperature = parseRanges(scanner);
                    case "temperature-to-humidity" -> temperatureToHumidity = parseRanges(scanner);
                    case "humidity-to-location" -> humidityToLocation = parseRanges(scanner);
                }
            }
        }
        return new DataWithSeedRanges(seedsRanges, seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation);
    }

    private static List<MappingRange> parseRanges(final Scanner scanner) {
        scanner.skip(" map:\n");
        var ranges = new ArrayList<MappingRange>();
        while (scanner.hasNextLine()) {
            var nextLine = scanner.nextLine();
            if (nextLine.isEmpty()) {
                break;
            }
            final String[] split = nextLine.split(" ");
            var startOfDestination = Long.parseLong(split[0]);
            var startOfSource = Long.parseLong(split[1]);
            var length = Long.parseLong(split[2]);

            ranges.add(new MappingRange(new Range(startOfSource, startOfSource + length), new Range(startOfDestination, startOfDestination + length)));
        }
        return ranges;
    }


    static long getLowestLocationNumber(Data data) {
        return Collections.min(getLocationNumbers(data));
    }


    static List<Long> getLocationNumbers(Data data) {
        var locations = new ArrayList<Long>();
        for (Long seed : data.seeds) {
            var soil = getDestinationFromRanges(seed, data.seedToSoil);
            var fertilizer = getDestinationFromRanges(soil, data.soilToFertilizer);
            var water = getDestinationFromRanges(fertilizer, data.fertilizerToWater);
            var light = getDestinationFromRanges(water, data.waterToLight);
            var temperature = getDestinationFromRanges(light, data.lightToTemperature);
            var humidity = getDestinationFromRanges(temperature, data.temperatureToHumidity);
            var location = getDestinationFromRanges(humidity, data.humidityToLocation);
            locations.add(location);
        }
        return locations;
    }


    static long getDestinationFromRanges(long source, Collection<MappingRange> mappingRanges) {
        long destination = source;
        for (MappingRange mappingRange : mappingRanges) {
            long sourceLocation = source - mappingRange.source.start;
            if (source <= mappingRange.source.end && sourceLocation >= 0) {
                destination = mappingRange.destination.start + sourceLocation;
            }
        }
        return destination;
    }

    static long getLowestLocationNumber(DataWithSeedRanges data) {
        final List<Range> list = getLocationNumbers(data);
        Collections.sort(list, Comparator.comparing(Range::start));
        return list.get(0).start;
    }

    static List<Range> getLocationNumbers(DataWithSeedRanges data) {
        var soil = getDestinationFromRanges(data.seedRanges, data.seedToSoil);
        var fertilizer = getDestinationFromRanges(soil, data.soilToFertilizer);
        var water = getDestinationFromRanges(fertilizer, data.fertilizerToWater);
        var light = getDestinationFromRanges(water, data.waterToLight);
        var temperature = getDestinationFromRanges(light, data.lightToTemperature);
        var humidity = getDestinationFromRanges(temperature, data.temperatureToHumidity);
        var location = getDestinationFromRanges(humidity, data.humidityToLocation);
        return location;
    }

    static List<Range> getDestinationFromRanges(Collection<Range> sourceRanges, Collection<MappingRange> mappingRanges) {
        var destinationRanges = new ArrayList<Range>();
        var alreadyMatchedSourceRanges = new ArrayList<Range>();
        for (Range sourceRange : sourceRanges) {
            boolean anyMatched = false;
            var notMatchedRanges = new ArrayList<Range>();

            for (MappingRange mappingRange : mappingRanges) {
                final Range intersection = sourceRange.getIntersection(mappingRange.source);

                if (intersection != null) {
                    anyMatched = true;
                    long startDistance = intersection.start - mappingRange.source.start;
                    long endDistance = mappingRange.source.end - intersection.end;
                    long destinationStart = mappingRange.destination.start + startDistance;
                    long destinationEnd = mappingRange.destination.end - endDistance;
                    destinationRanges.add(new Range(destinationStart, destinationEnd));

                    final List<Range> list = notMatchedRanges.stream().filter(range -> range.getIntersection(intersection) != null).toList();
                    notMatchedRanges.removeAll(list);

                    alreadyMatchedSourceRanges.add(intersection);

                    if (intersection.start > sourceRange.start) {
                        final Range rangeToAdd = new Range(sourceRange.start, intersection.start);
                        var alreadyMatched = alreadyMatchedSourceRanges.stream().anyMatch(range -> range.getIntersection(rangeToAdd) != null);
                        if(!alreadyMatched) {
                            notMatchedRanges.add(rangeToAdd);
                        }
                    }
                    if (intersection.end < sourceRange.end) {
                        final Range rangeToAdd = new Range(intersection.end, sourceRange.end);
                        var alreadyMatched =  alreadyMatchedSourceRanges.stream().anyMatch(range -> range.getIntersection(rangeToAdd)!= null);
                        if(!alreadyMatched){
                            notMatchedRanges.add(rangeToAdd);
                        }
                    }
                }
            }

            destinationRanges.addAll(notMatchedRanges);
            if (!anyMatched) {
                destinationRanges.add(sourceRange);
            }
        }
        return destinationRanges;
    }

}
