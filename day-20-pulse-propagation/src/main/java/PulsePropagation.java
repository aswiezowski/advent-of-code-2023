import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class PulsePropagation {

    public static void main(String[] args) throws URISyntaxException, IOException {
        final var input = Files.readString(Path.of(ClassLoader.getSystemClassLoader().getResource("input").toURI()));
//
//        final var modules = parseInput(input);
//        final var multipleLowAndHighPulsesAfterNumberOfRequests = multipleLowAndHighPulsesAfterNumberOfRequests(modules, 1000);
//
//        System.out.println("Multplication of low and high pulses: " + multipleLowAndHighPulsesAfterNumberOfRequests);
        final var modules2 = parseInput(input);

        final var lowestNumberOfButtonPressesToGetRx = lowestNumberOfButtonPressesToGetRx(modules2, Integer.MAX_VALUE);
        System.out.println("Lowest number to get to rx: " + lowestNumberOfButtonPressesToGetRx);
    }

    static class Module {
        String name;
        String type;
        List<String> nextModules;
        boolean state;

        Map<String, Boolean> lastInputStates;

        public Module(final String name, final String type, final List<String> nextModules, final boolean state) {
            this.name = name;
            this.type = type;
            this.nextModules = nextModules;
            this.state = state;
        }
    }

    record Pulse(String fromModule, String toModule, boolean isHighPulse) {
        @Override
        public String toString() {
            final var pulseType = isHighPulse ? "-high" : "-low";
            return fromModule + " " + pulseType + "->" + " " + toModule;
        }
    }

    static Map<String, Module> parseInput(String input) {
        var modulesMap = new HashMap<String, Module>();
        Arrays.stream(input.split("\n")).map(line -> {
            final String[] splitLine = line.split("->");
            var nextModules = Arrays.stream(splitLine[1].split(",")).map(String::trim).toList();
            if (splitLine[0].trim().equals("broadcaster")) {
                var name = "broadcaster";
                var type = "broadcaster";
                return new Module(name, type, nextModules, false);
            } else {
                var type = splitLine[0].substring(0, 1);
                var name = splitLine[0].substring(1).trim();
                return new Module(name, type, nextModules, false);
            }
        }).forEach(module -> modulesMap.put(module.name, module));
        final var conjunctionModules = modulesMap.values().stream().filter(module -> module.type.equals("&")).toList();
        conjunctionModules.forEach(module -> {
            final var inputs = modulesMap.values().stream().filter(module1 -> module1.nextModules.contains(module.name)).toList();
            final var lastInputStates = new HashMap<String, Boolean>();
            inputs.forEach(inputModule -> lastInputStates.put(inputModule.name, false));
            module.lastInputStates = lastInputStates;
        });
        return modulesMap;
    }

    static long multipleLowAndHighPulsesAfterNumberOfRequests(Map<String, Module> modules, int numberOfRequests) {
        long lowPulses = 0;
        long highPulses = 0;
        for (int index = 0; index < numberOfRequests; index++) {
            final var pulses = processPulses(modules);
            final var highPulsesInIteration = pulses.stream().filter(pulse -> pulse.isHighPulse).count();
            highPulses = highPulses + highPulsesInIteration;
            lowPulses = lowPulses + (pulses.size() - highPulsesInIteration);
        }
        return highPulses * lowPulses;
    }

    static long lowestNumberOfButtonPressesToGetRx(Map<String, Module> modules, int numberOfRequests) {
        for (int index = 0; index < numberOfRequests; index++) {
            final var pulses = processPulses(modules);
            if (pulses.stream().anyMatch(pulse -> !pulse.isHighPulse && pulse.toModule.equals("rx"))) {
                return index + 1;
            }
        }
        throw new IllegalStateException("Didn't find number of presses");
    }

    static ArrayList<Pulse> processPulses(Map<String, Module> modules) {
        var pulses = new ArrayList<Pulse>();
        pulses.add(new Pulse("button", "broadcaster", false));

        final var allPulses = new ArrayList<Pulse>();
        do {
            allPulses.addAll(pulses);
            var newPulses = new ArrayList<Pulse>();
            pulses.forEach(pulse -> {
                final var toModule = modules.get(pulse.toModule);
                if (toModule != null) {
                    if (toModule.type.equals("%")) {
                        if (!pulse.isHighPulse) {
                            toModule.nextModules.stream().map(nextModule -> new Pulse(toModule.name, nextModule, !toModule.state)).forEach(newPulses::add);
                            toModule.state = !toModule.state;
                        }
                    } else if (toModule.type.equals("&")) {
                        toModule.lastInputStates.put(pulse.fromModule, pulse.isHighPulse);
                        final var isHighPulse = !toModule.lastInputStates.values().stream().allMatch(state -> state);
                        toModule.nextModules.stream().map(nextModule -> new Pulse(toModule.name, nextModule, isHighPulse)).forEach(newPulses::add);
                    } else if (toModule.type.equals("broadcaster")) {
                        toModule.nextModules.stream().map(nextModule -> new Pulse(toModule.name, nextModule, pulse.isHighPulse)).forEach(newPulses::add);
                    }
                }
            });
            pulses = newPulses;
        } while (!pulses.isEmpty());

        return allPulses;
    }

}
