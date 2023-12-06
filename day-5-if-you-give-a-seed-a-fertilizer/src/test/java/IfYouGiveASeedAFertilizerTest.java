import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class IfYouGiveASeedAFertilizerTest {


    @Test
    void parsingSeedsShouldReturnSeedsNumbers() {
        // given
        var line = "seeds: 79 14 55 13";

        // when
        var actual = IfYouGiveASeedAFertilizer.parseLines(line);

        // then
        assertEquals(List.of(79L, 14L, 55L, 13L), actual.seeds());
    }

    @Test
    void parsingRangesWorksCoorectly() {
        // given
        var lines = """
                    seeds: 79 14 55 13
                                       
                    seed-to-soil map:
                    50 98 2
                    """;

        // when
        var actual = IfYouGiveASeedAFertilizer.parseLines(lines);

        // then
        assertEquals(List.of(new IfYouGiveASeedAFertilizer.MappingRange(new IfYouGiveASeedAFertilizer.Range(98, 100), new IfYouGiveASeedAFertilizer.Range(50, 52))),
                actual.seedToSoil());
    }

    @Test
    void everyElementIsparased() {
        var lines = """
                    seeds: 79 14 55 13
                                        
                    seed-to-soil map:
                    50 98 2
                    52 50 48
                                        
                    soil-to-fertilizer map:
                    0 15 37
                    37 52 2
                    39 0 15
                                        
                    fertilizer-to-water map:
                    49 53 8
                    0 11 42
                    42 0 7
                    57 7 4
                                        
                    water-to-light map:
                    88 18 7
                    18 25 70
                                        
                    light-to-temperature map:
                    45 77 23
                    81 45 19
                    68 64 13
                                        
                    temperature-to-humidity map:
                    0 69 1
                    1 0 69
                                        
                    humidity-to-location map:
                    60 56 37
                    56 93 4
                    """;

        // when
        final IfYouGiveASeedAFertilizer.Data data = IfYouGiveASeedAFertilizer.parseLines(lines);

        // then

        assertNotNull(data.seedToSoil());
        assertNotNull(data.soilToFertilizer());
        assertNotNull(data.fertilizerToWater());
        assertNotNull(data.waterToLight());
        assertNotNull(data.lightToTemperature());
        assertNotNull(data.temperatureToHumidity());
        assertNotNull(data.humidityToLocation());
    }

    @Test
    void getLowestLocationNumberReturns35() {
        var lines = """
                    seeds: 79 14 55 13
                                        
                    seed-to-soil map:
                    50 98 2
                    52 50 48
                                        
                    soil-to-fertilizer map:
                    0 15 37
                    37 52 2
                    39 0 15
                                        
                    fertilizer-to-water map:
                    49 53 8
                    0 11 42
                    42 0 7
                    57 7 4
                                        
                    water-to-light map:
                    88 18 7
                    18 25 70
                                        
                    light-to-temperature map:
                    45 77 23
                    81 45 19
                    68 64 13
                                        
                    temperature-to-humidity map:
                    0 69 1
                    1 0 69
                                        
                    humidity-to-location map:
                    60 56 37
                    56 93 4
                    """;
        final IfYouGiveASeedAFertilizer.Data data = IfYouGiveASeedAFertilizer.parseLines(lines);

        // when
        var actual = IfYouGiveASeedAFertilizer.getLowestLocationNumber(data);

        // then
        assertEquals(35, actual);
    }

    @Test
    void getLowestLocationNumberReturns35WithSeedRanges() {
        var lines = """
                    seeds: 79 14 55 13
                                        
                    seed-to-soil map:
                    50 98 2
                    52 50 48
                                        
                    soil-to-fertilizer map:
                    0 15 37
                    37 52 2
                    39 0 15
                                        
                    fertilizer-to-water map:
                    49 53 8
                    0 11 42
                    42 0 7
                    57 7 4
                                        
                    water-to-light map:
                    88 18 7
                    18 25 70
                                        
                    light-to-temperature map:
                    45 77 23
                    81 45 19
                    68 64 13
                                        
                    temperature-to-humidity map:
                    0 69 1
                    1 0 69
                                        
                    humidity-to-location map:
                    60 56 37
                    56 93 4
                    """;
        final IfYouGiveASeedAFertilizer.DataWithSeedRanges data = IfYouGiveASeedAFertilizer.parseLinesWithSeedRange(lines);

        // when
        var actual = IfYouGiveASeedAFertilizer.getLowestLocationNumber(data);

        // then
        assertEquals(46, actual);
    }


    @ParameterizedTest
    @MethodSource("getDestinationRanges")
    void getDestinationFromRangesReturnsCorrectly(List<IfYouGiveASeedAFertilizer.Range> sourceRanges, List<IfYouGiveASeedAFertilizer.MappingRange> mappingRange,
                                                  List<IfYouGiveASeedAFertilizer.Range> expectedDestinationRange) {
        // given

        // when
        var actualDestinationRange = IfYouGiveASeedAFertilizer.getDestinationFromRanges(sourceRanges, mappingRange);

        // then
        assertEquals(expectedDestinationRange, actualDestinationRange);
    }

    static Stream<Arguments> getDestinationRanges() {
        return Stream.of(
                Arguments.of(List.of(newRange(0, 5)),
                        List.of(new IfYouGiveASeedAFertilizer.MappingRange(newRange(0, 5), newRange(10, 15))),
                        List.of(newRange(10, 15))),
                Arguments.of(List.of(newRange(0, 1), newRange(4, 5), newRange(9, 10)),
                        List.of(new IfYouGiveASeedAFertilizer.MappingRange(newRange(0, 5), newRange(10, 15))),
                        List.of(newRange(10, 11), newRange(14, 15), newRange(9, 10))),
                Arguments.of(List.of(newRange(0, 10)),
                        List.of(new IfYouGiveASeedAFertilizer.MappingRange(newRange(0, 5), newRange(10, 15))),
                        List.of(newRange(10, 15), newRange(5, 10))),
                Arguments.of(List.of(newRange(5, 10)),
                        List.of(new IfYouGiveASeedAFertilizer.MappingRange(newRange(4, 5), newRange(10, 15))),
                        List.of(newRange(5, 10))),
                Arguments.of(List.of(newRange(5, 10)),
                        List.of(new IfYouGiveASeedAFertilizer.MappingRange(newRange(0, 5), newRange(10, 15))),
                        List.of(newRange(5, 10))),
                Arguments.of(List.of(newRange(0, 5)),
                        List.of(new IfYouGiveASeedAFertilizer.MappingRange(newRange(0, 3), newRange(20, 23)),
                                new IfYouGiveASeedAFertilizer.MappingRange(newRange(3, 5), newRange(50, 52))
                        ),
                        List.of(newRange(20, 23), newRange(50, 52)))
        );
    }

    @Test
    void getDestinationFromRangesReturnsCorrectlyForMultipleMappings() {
        // given
        var sourceRanges = List.of(newRange(0, 5));
        var mappingRanges = List.of(new IfYouGiveASeedAFertilizer.MappingRange(newRange(0, 3), newRange(20, 23)),
                new IfYouGiveASeedAFertilizer.MappingRange(newRange(3, 5), newRange(50, 52))
        );

        // when
        var actualDestinationRange = IfYouGiveASeedAFertilizer.getDestinationFromRanges(sourceRanges, mappingRanges);

        // then
        assertEquals(List.of(newRange(20, 23), newRange(50, 52)), actualDestinationRange);
    }

    private static IfYouGiveASeedAFertilizer.Range newRange(long start, long end) {
        return new IfYouGiveASeedAFertilizer.Range(start, end);
    }

}