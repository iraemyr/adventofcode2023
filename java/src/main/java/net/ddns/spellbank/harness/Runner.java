package net.ddns.spellbank.harness;

import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public final class Runner {
    private Runner() {}

    public static void main(String[] args) {
        int status = run(args, System.out, System.err);
        if (status != 0) {
            System.exit(status);
        }
    }

    static int run(String[] args, PrintStream out, PrintStream err) {
        try {
            var options = Options.parse(args);
            if (options.help()) {
                out.println("""
                        Usage: run --day <1-25|all> [--part <1|2|both>] [--resource <name> | --input <path>]
                               run --list
                        Defaults: both parts, dayNN/input1. Prints each answer, elapsed milliseconds, and a total.
                        Examples: ./gradlew run --args='--day 14'
                                  ./gradlew run --args='--day 14 --part 2 --resource test1'
                        JMH:      ./gradlew jmh -Pday=14 -Ppart=2
                        """);
                return 0;
            }
            if (options.list()) {
                out.println("Available days: " + Solutions.days());
                return 0;
            }

            List<Integer> days = options.day().equals("all")
                    ? Solutions.days() : List.of(Integer.parseInt(options.day()));
            long totalElapsed = 0;

            for (int day : days) {
                var solution = Solutions.forDay(day);
                var text = Inputs.loadString(day, options.resource(), options.file());
                var lines = text.lines().toArray(String[]::new);

                for (int part = 1; part <= 2; part++) {
                    if (options.part() != 0 && options.part() != part) {
                        continue;
                    }

                    var method = Solutions.partMethod(solution, part);
                    boolean textAnswer = method.getReturnType() == String.class;
                    boolean stringInput = method.getParameterTypes()[0] == String.class;
                    var input = stringInput ? null : lines.clone();
                    String answer;
                    long elapsed;

                    if (textAnswer) {
                        long start = System.nanoTime();
                        answer = stringInput ? solution.solve(part, text) : solution.solve(part, input);
                        elapsed = System.nanoTime() - start;
                    } else {
                        long start = System.nanoTime();
                        long numeric = stringInput
                                ? solution.solveNumeric(part, text) : solution.solveNumeric(part, input);
                        elapsed = System.nanoTime() - start;

                        answer = Long.toString(numeric);
                    }

                    totalElapsed += elapsed;

                    out.printf(Locale.ROOT, "Day %02d part %d: %s (%.3f ms)%n",
                            day, part, answer, elapsed / 1_000_000.0);
                }
            }

            out.printf(Locale.ROOT, "Total time: %.3f ms%n", totalElapsed / 1_000_000.0);

            return 0;
        } catch (IllegalArgumentException | UncheckedIOException e) {
            err.println("Error: " + e.getMessage());
            return 2;
        }
    }

    private record Options(String day, int part, String resource, Path file, boolean list, boolean help) {
        static Options parse(String[] args) {
            String day = null;
            int part = 0;
            String resource = null;
            Path file = null;
            boolean resourceSet = false;
            boolean list = false;
            boolean help = args.length == 0;

            for (int i = 0; i < args.length; i++) {
                String option = args[i];
                if (option.equals("--help") || option.equals("-h")) {
                    help = true;
                    continue;
                }
                if (option.equals("--list")) {
                    list = true;
                    continue;
                }
                if (!List.of("--day", "--part", "--resource", "--input").contains(option)) {
                    throw new IllegalArgumentException("Unknown option: " + option);
                }
                if (++i == args.length || args[i].startsWith("--")) {
                    throw new IllegalArgumentException("Missing value for " + option);
                }

                String value = args[i];
                switch (option) {
                    case "--day" -> day = value;
                    case "--part" -> part = switch (value) {
                        case "1" -> 1;
                        case "2" -> 2;
                        case "both" -> 0;
                        default -> throw new IllegalArgumentException("Part must be 1, 2, or both.");
                    };
                    case "--resource" -> {
                        resource = value;
                        resourceSet = true;
                    }
                    case "--input" -> file = Path.of(value);
                }
            }

            if (!help && !list && day == null) {
                throw new IllegalArgumentException("Specify --day <1-25|all> or --list.");
            }
            if (day != null && !day.equals("all")) {
                int number;
                try {
                    number = Integer.parseInt(day);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Day must be between 1 and 25, or all.");
                }
                if (number < 1 || number > 25) {
                    throw new IllegalArgumentException("Day must be between 1 and 25, or all.");
                }
            }
            if (file != null && (resourceSet || "all".equals(day))) {
                throw new IllegalArgumentException("--input requires a single day and cannot be combined with --resource.");
            }

            return new Options(day, part, resource, file, list, help);
        }
    }
}
