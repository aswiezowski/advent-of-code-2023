import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Aplenty {

    public static void main(String[] args) throws URISyntaxException, IOException {
        final String input = Files.readString(Path.of(ClassLoader.getSystemResource("input").toURI()));

        final Board board = parseInput(input);

        final long featuresSumOfAcceptedParts = getFeaturesSumOfAcceptedParts(board);
        System.out.println("Features sum of accepted parts: " + featuresSumOfAcceptedParts);

        final long numberOfCombinationsForAcceptedWorkflows = getNumberOfCombinationsForAcceptedWorkflows(board);
        System.out.println("Number of combinations: " + numberOfCombinationsForAcceptedWorkflows);

    }

    record Board(Map<String, Workflow> workflows, List<Part> parts) {
    }

    record Part(Map<String, Integer> features) {
    }

    record Rule(String feature, String operation, Integer value, String nextWorkflow) {
    }

    record Workflow(String workflow, List<Rule> rules) {
    }

    static Board parseInput(String input) {
        final String[] split = input.split("\n\n");
        final Map<String, Workflow> workflows = parseWorkflows(split[0].trim());
        final List<Part> parts = parseParts(split[1].trim());
        return new Board(workflows, parts);
    }

    static Map<String, Workflow> parseWorkflows(String workflows) {
        var workflowsMap = new HashMap<String, Workflow>();
        final String[] workflowsSplit = workflows.split("\n");
        Arrays.stream(workflowsSplit).forEach(workflow -> {
            var name = workflow.substring(0, workflow.indexOf('{'));
            var rules = workflow.substring(workflow.indexOf('{') + 1, workflow.indexOf('}'));
            final String[] rulesSplit = rules.split(",");
            final List<Rule> parsedRules = Arrays.stream(rulesSplit).map(rule -> {
                var operatorIndex = Math.max(rule.indexOf("<"), rule.indexOf(">"));
                if (operatorIndex > 0) {
                    var feature = rule.substring(0, operatorIndex);
                    var operator = rule.substring(operatorIndex, operatorIndex + 1);
                    var value = rule.substring(operatorIndex + 1, rule.indexOf(":"));
                    var nextWorkflow = rule.substring(rule.indexOf(":") + 1);
                    return new Rule(feature, operator, Integer.valueOf(value), nextWorkflow);
                } else {
                    return new Rule(null, null, null, rule);
                }
            }).toList();
            workflowsMap.put(name, new Workflow(name, parsedRules));
        });
        return workflowsMap;
    }

    static List<Part> parseParts(String parts) {
        final String[] splitParts = parts.split("\n");
        return Arrays.stream(splitParts).map(part -> {
                    var features = new HashMap<String, Integer>();
                    Arrays.stream(part.substring(1, part.length() - 1).split(","))
                          .forEach(feature -> {
                              final String[] split = feature.split("=");
                              features.put(split[0], Integer.valueOf(split[1]));
                          });
                    return new Part(features);
                }
        ).toList();
    }

    static long getFeaturesSumOfAcceptedParts(Board board) {
        return getAcceptedParts(board).stream()
                                      .mapToLong(part -> part.features.values().stream()
                                                                      .mapToLong(Long::valueOf)
                                                                      .sum())
                                      .sum();
    }

    static List<Part> getAcceptedParts(Board board) {
        return board.parts.stream().filter(part -> isAccepted(part, board.workflows)).toList();
    }

    private static boolean isAccepted(final Part part, final Map<String, Workflow> workflows) {
        var nextWorkflowName = "in";

        while (!nextWorkflowName.equals("A") && !nextWorkflowName.equals("R")) {
            var workflow = workflows.get(nextWorkflowName);
            nextWorkflowName = getNextWorkflow(part, workflow);
        }

        return nextWorkflowName.equals("A");
    }

    static String getNextWorkflow(Part part, Workflow workflow) {
        for (Rule rule : workflow.rules) {
            if (rule.operation != null && rule.feature != null && rule.value != null) {
                final Integer featureValue = part.features.get(rule.feature);
                if (rule.operation.equals("<")) {
                    if (featureValue < rule.value) {
                        return rule.nextWorkflow;
                    }
                } else if (rule.operation.equals(">")) {
                    if (featureValue > rule.value) {
                        return rule.nextWorkflow;
                    }
                }
            } else {
                return rule.nextWorkflow;
            }
        }
        throw new IllegalStateException("Didn't find default next workflow in workflow " + workflow);
    }


    static long getNumberOfCombinationsForAcceptedWorkflows(Board board) {
        final List<List<String>> acceptedWorkflows = getAllAcceptedWorkflows("in", board.workflows);

        return acceptedWorkflows.stream().mapToLong(
                workflow -> getNumberOfCombinationsForGivenWorkflow(workflow, board.workflows)
        ).sum();
    }

    static List<List<String>> getAllAcceptedWorkflows(String workflow, Map<String, Workflow> workflows) {
        var nextWorkflows = new ArrayList<List<String>>();
        for (Rule rule : workflows.get(workflow).rules) {
            if (rule.nextWorkflow.equals("A")) {
                final List<String> workflow1 = new ArrayList<String>();
                workflow1.add(workflow);
                workflow1.add("A");
                nextWorkflows.add(workflow1);
            } else if (!rule.nextWorkflow.equals("R")) {
                final List<List<String>> allNextWorkflow = getAllAcceptedWorkflows(rule.nextWorkflow, workflows);
                nextWorkflows.addAll(allNextWorkflow.stream().map(allNextWorkflows -> {
                    final List<String> newWorflow = new ArrayList<>();
                    newWorflow.add(workflow);
                    newWorflow.addAll(allNextWorkflows);
                    return newWorflow;
                }).toList());
            }
        }
        return nextWorkflows;
    }


    static long getNumberOfCombinationsForGivenWorkflow(List<String> workflowPath, Map<String, Workflow> workflows) {
        Map<String, Integer> featureToMinValue = new HashMap<>();
        Map<String, Integer> featureToMaxValue = new HashMap<>();
        final Iterator<String> iterator = workflowPath.iterator();
        String nextWorkflow = iterator.next();
        while (!nextWorkflow.equals("A")) {
            final Workflow workflow = workflows.get(nextWorkflow);
            nextWorkflow = iterator.next();

            for (Rule rule : workflow.rules) {
                if (rule.operation != null) {
                    final Rule finalRule = rule;
                    if (rule.operation.equals(">")) {
                        featureToMinValue.compute(rule.feature, (key, oldValue) -> oldValue != null ? Math.max(oldValue, finalRule.value) : finalRule.value);
                    } else if (rule.operation.equals("<")) {
                        featureToMaxValue.compute(rule.feature, (key, oldValue) -> oldValue != null ? Math.min(oldValue, finalRule.value) : finalRule.value);
                    }
                }
                if (rule.nextWorkflow.equals(nextWorkflow)) {
                    break;
                }
            }
        }

        long combinations = 1;
        for (String feature : List.of("x", "m", "s", "a")) {
            final int numberOfPossibilities = featureToMaxValue.getOrDefault(feature, 4000) - featureToMinValue.getOrDefault(feature, 0);
            if (numberOfPossibilities < 0) {
                return 0;
            }
            combinations = combinations * numberOfPossibilities;
        }
        return combinations;
    }

}
