package net.ddns.spellbank.day19;

import net.ddns.spellbank.utils.InputFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 {
    public record Part(int x, int m, int a, int s) {
        public int getAtt(char c) {
            return switch (c) {
                case 'x' -> x;
                case 'm' -> m;
                case 'a' -> a;
                case 's' -> s;
                default -> throw new IllegalArgumentException("Invalid attribute");
            };
        }
    }

    public enum Type {LT, GT, SEND}

    public record Rule(Type type, Character attr, String dest, Integer limit) {}

    public static class Workflow {
        public List<Rule> rules;

        public Workflow() {
            rules = new ArrayList<>();
        }

        public void addRule(Rule r) {
            rules.add(r);
        }
    }

    public static void main(String[] args) {
        String file = "day19/input1";
        String[] lines = InputFile.getLines(file);
        var workflows = new HashMap<String, Workflow>();
        var parts = new ArrayList<Part>();
        parse(lines, workflows, parts);

        System.out.println(part1(workflows, parts)); // 406849
        System.out.println(part2(workflows)); // 138625360533574
    }

    public static long part1(Map<String, Workflow> workflows, List<Part> parts) {
        var accepted = new ArrayList<Part>();
        var rejected = new ArrayList<Part>();
        for (var part : parts) sortPart(part, "in", workflows, accepted, rejected);
        long sum = 0;
        for (var p : accepted) sum += p.x + p.m + p.a + p.s;
        return sum;
    }

    public static long part2(Map<String, Workflow> workflows) {
        return walk("in", workflows, 1, 4000,
                1, 4000, 1, 4000, 1, 4000);
    }

    private static void sortPart(Part part, String name,
                                 Map<String, Workflow> workflows,
                                 List<Part> accepted, List<Part> rejected) {
        var workflow = workflows.get(name);
        for (var rule : workflow.rules) {
            if (rule.type == Type.SEND) {
                send(part, rule.dest, workflows, accepted, rejected);
                break;
            } else if (rule.type == Type.LT) {
                if (part.getAtt(rule.attr) < rule.limit) {
                    send(part, rule.dest, workflows, accepted, rejected);
                    break;
                }
            } else {
                if (part.getAtt(rule.attr) > rule.limit) {
                    send(part, rule.dest, workflows, accepted, rejected);
                    break;
                }
            }
        }
    }

    private static void send(Part part, String name,
                             Map<String, Workflow> workflows,
                             List<Part> accepted,
                             List<Part> rejected) {
        switch (name) {
            case "A" -> accepted.add(part);
            case "R" -> rejected.add(part);
            default -> sortPart(part, name, workflows, accepted, rejected);
        }
    }

    public static void parse(String[] lines, Map<String, Workflow> flows, List<Part> parts) {
        boolean p = false;
        for (var line : lines) {
            if (line.isEmpty()) p = true;
            else if (p) {
                parts.add(parsePart(line));
            } else {
                var index = line.indexOf('{');
                var name = line.substring(0, index);
                flows.put(name, parseWorkflow(line.substring(index + 1, line.length() - 1)));
            }
        }
    }

    private static Part parsePart(String line) {
        var fields = line.substring(1, line.length() - 1).split(",");
        return new Part(Integer.parseInt(fields[0].substring(2)),
                Integer.parseInt(fields[1].substring(2)),
                Integer.parseInt(fields[2].substring(2)),
                Integer.parseInt(fields[3].substring(2)));
    }

    private static Workflow parseWorkflow(String line) {
        var wf = new Workflow();
        var fields = line.split(",");
        for (var field : fields) {
            Rule rule;
            var f = field.split(":");
            if (f.length == 1) {
                rule = new Rule(Type.SEND, null, field, null);
            } else {
                var dest = f[1];
                var attr = f[0].charAt(0);
                var type = f[0].charAt(1) == '<' ? Type.LT : Type.GT;
                rule = new Rule(type, attr, dest, Integer.parseInt(f[0].substring(2)));
            }
            wf.addRule(rule);
        }
        return wf;
    }

    private static long walk(String name, Map<String, Workflow> workflows,
                             int minX, int maxX, int minM, int maxM,
                             int minA, int maxA, int minS, int maxS) {
        if (minX > maxX || minM > maxM || minA > maxA || minS > maxS) return 0;
        if (name.equals("A")) return diff(maxX, minX) *
                diff(maxM, minM) * diff(maxA, minA) * diff(maxS, minS);
        if (name.equals("R")) return 0;
        var flow = workflows.get(name);
        long sum = 0;
        for (var rule : flow.rules) {
            sum += switch (rule.type) {
                case Type.SEND -> walk(rule.dest, workflows,
                        minX, maxX, minM, maxM, minA, maxA, minS, maxS);
                case Type.GT -> {
                    switch (rule.attr) {
                        case 'x' -> {
                            var oldMaxX = maxX;
                            maxX = Math.min(maxX, rule.limit);
                            yield walk(rule.dest, workflows,
                                    rule.limit + 1, oldMaxX, minM, maxM,
                                    minA, maxA, minS, maxS);
                        }
                        case 'm' -> {
                            var oldMaxM = maxM;
                            maxM = Math.min(maxM, rule.limit);
                            yield walk(rule.dest, workflows,
                                    minX, maxX, rule.limit + 1, oldMaxM,
                                    minA, maxA, minS, maxS);
                        }
                        case 'a' -> {
                            var oldMaxA = maxA;
                            maxA = Math.min(maxA, rule.limit);
                            yield walk(rule.dest, workflows,
                                    minX, maxX, minM, maxM,
                                    rule.limit + 1, oldMaxA, minS, maxS);
                        }
                        case 's' -> {
                            var oldMaxS = maxS;
                            maxS = Math.min(maxS, rule.limit);
                            yield walk(rule.dest, workflows,
                                    minX, maxX, minM, maxM,
                                    minA, maxA, rule.limit + 1, oldMaxS);
                        }
                        default -> throw new IllegalStateException("Invalid attribute");
                    }
                }
                case Type.LT -> {
                    switch (rule.attr) {
                        case 'x' -> {
                            var oldMinX = minX;
                            minX = Math.max(minX, rule.limit);
                            yield walk(rule.dest, workflows,
                                    oldMinX, rule.limit - 1, minM, maxM,
                                    minA, maxA, minS, maxS);
                        }
                        case 'm' -> {
                            var oldMinM = minM;
                            minM = Math.max(minM, rule.limit);
                            yield walk(rule.dest, workflows,
                                    minX, maxX, oldMinM, rule.limit - 1,
                                    minA, maxA, minS, maxS);
                        }
                        case 'a' -> {
                            var oldMinA = minA;
                            minA = Math.max(minA, rule.limit);
                            yield walk(rule.dest, workflows,
                                    minX, maxX, minM, maxM,
                                    oldMinA, rule.limit - 1, minS, maxS);
                        }
                        case 's' -> {
                            var oldMinS = minS;
                            minS = Math.max(minS, rule.limit);
                            yield walk(rule.dest, workflows,
                                    minX, maxX, minM, maxM,
                                    minA, maxA, oldMinS, rule.limit - 1);
                        }
                        default -> throw new IllegalStateException("Invalid attribute");
                    }
                }
            };
        }
        return sum;
    }

    private static long diff(int max, int min) { return max - min + 1L; }
}