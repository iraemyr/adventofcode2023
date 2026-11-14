package net.ddns.spellbank.harness;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class HarnessTest {
    @TempDir
    Path tempDir;

    @Test
    void supportsNumericAndTextAnswers() {
        Solution solution = new Solution() {
            @Override
            public String solvePart1Str(String[] lines) {
                return String.join("\n", lines);
            }

            @Override
            public long solvePart2(String[] lines) {
                return 1_234_567_890_123_456_789L;
            }
        };
        String[] lines = {"00123", "café", " trailing "};

        long numeric = solution.solvePart2(lines);
        String text = solution.solvePart1Str(lines);

        assertEquals(1_234_567_890_123_456_789L, numeric);
        assertEquals("00123\ncafé\n trailing ", text);
        assertEquals(text, solution.solve(1, lines));
        assertEquals(Long.toString(numeric), solution.solve(2, lines));
        assertEquals(String.class, Solutions.partMethod(solution, 1).getReturnType());
        assertEquals(long.class, Solutions.partMethod(solution, 2).getReturnType());
        assertThrows(UnsupportedOperationException.class, () -> solution.solvePart1(lines));
    }

    @Test
    void supportsStringInputWithNumericAndTextAnswers() {
        Solution solution = new Solution() {
            @Override
            public long solvePart1(String input) {
                return input.length();
            }

            @Override
            public String solvePart2Str(String input) {
                return input;
            }
        };
        String input = "00123\r\n\r\ncafé\n trailing \n";

        long numeric = solution.solveNumeric(1, input);
        String text = solution.solve(2, input);

        assertEquals(input.length(), numeric);
        assertEquals(Long.toString(numeric), solution.solve(1, input));
        assertEquals(input, text);
        assertEquals(String.class, Solutions.partMethod(solution, 1).getParameterTypes()[0]);
        assertEquals(String.class, Solutions.partMethod(solution, 2).getParameterTypes()[0]);
        assertEquals(long.class, Solutions.partMethod(solution, 1).getReturnType());
        assertEquals(String.class, Solutions.partMethod(solution, 2).getReturnType());
        assertThrows(UnsupportedOperationException.class, () -> solution.solveNumeric(2, input));
    }

    @Test
    void supportsDifferentInputTypesForEachPart() {
        Solution solution = new Solution() {
            @Override
            public String solvePart1Str(String input) {
                return input;
            }

            @Override
            public long solvePart2(String[] lines) {
                return lines.length;
            }
        };

        String text = solution.solve(1, "café\n\n");
        long numeric = solution.solveNumeric(2, new String[]{"café", ""});

        assertEquals("café\n\n", text);
        assertEquals(2, numeric);
        assertEquals(String.class, Solutions.partMethod(solution, 1).getParameterTypes()[0]);
        assertEquals(String[].class, Solutions.partMethod(solution, 2).getParameterTypes()[0]);
    }

    @Test
    void supportsNumericStringInputForPartTwo() {
        Solution solution = new Solution() {
            @Override
            public String solvePart1Str(String[] lines) {
                return lines[0];
            }

            @Override
            public long solvePart2(String input) {
                return input.length();
            }
        };

        long numeric = solution.solveNumeric(2, "café\n");

        assertEquals(5, numeric);
        assertEquals("5", solution.solve(2, "café\n"));
        assertEquals(String[].class, Solutions.partMethod(solution, 1).getParameterTypes()[0]);
        assertEquals(String.class, Solutions.partMethod(solution, 2).getParameterTypes()[0]);
    }

    @Test
    void rejectsMultipleInputTypesForOnePart() {
        Solution solution = new Solution() {
            @Override
            public long solvePart1(String[] lines) {
                return 42;
            }

            @Override
            public long solvePart1(String input) {
                return 42;
            }

            @Override
            public long solvePart2(String[] lines) {
                return 42;
            }

            @Override
            public String solvePart2Str(String input) {
                return "42";
            }
        };

        assertThrows(IllegalArgumentException.class, () -> Solutions.partMethod(solution, 1));
        assertThrows(IllegalArgumentException.class, () -> Solutions.partMethod(solution, 2));
    }

    @Test
    void rejectsAmbiguousOrMissingPartImplementations() {
        Solution solution = new Solution() {
            @Override
            public long solvePart1(String[] lines) {
                return 42;
            }

            @Override
            public String solvePart1Str(String[] lines) {
                return "42";
            }
        };

        var ambiguous = assertThrows(IllegalArgumentException.class, () -> Solutions.partMethod(solution, 1));
        var missing = assertThrows(IllegalArgumentException.class, () -> Solutions.partMethod(solution, 2));

        assertTrue(ambiguous.getMessage().contains("exactly one of solvePart1 or solvePart1Str"));
        assertTrue(missing.getMessage().contains("exactly one of solvePart2 or solvePart2Str"));
    }

    @Test
    void discoversAllImplementedDays() {
        assertTrue(Solutions.days().containsAll(IntStream.rangeClosed(1, 23).boxed().toList()));
        assertThrows(IllegalArgumentException.class, () -> Solutions.forDay(0));
        assertThrows(IllegalArgumentException.class, () -> Solutions.forDay(26));
    }

    @ParameterizedTest
    @CsvSource({"14, 64", "22, 7"})
    void mutableParsedInputsAreFreshForEachInvocation(int day, long expected) {
        var solution = Solutions.forDay(day);
        var lines = Inputs.load(day, "test1", null);

        long first = solution.solveNumeric(2, lines.clone());
        long second = solution.solveNumeric(2, lines.clone());

        assertEquals(expected, first);
        assertEquals(expected, second);
    }

    @Test
    void runsOnePartWithSampleInputAndTiming() {
        var out = new ByteArrayOutputStream();
        var err = new ByteArrayOutputStream();

        int status = Runner.run(new String[]{"--day", "14", "--part", "2", "--resource", "test1"},
                new PrintStream(out), new PrintStream(err));

        assertEquals(0, status);
        assertTrue(out.toString(StandardCharsets.UTF_8).matches(
                "Day 14 part 2: 64 \\((\\d+\\.\\d{3}) ms\\)\\RTotal time: \\1 ms\\R"));
        assertEquals("", err.toString(StandardCharsets.UTF_8));
    }

    @Test
    void acceptsExternalInput() throws Exception {
        var file = tempDir.resolve("custom input.txt");
        Files.writeString(file, "1abc2\npqr3stu8vwx\n", StandardCharsets.UTF_8);
        var out = new ByteArrayOutputStream();
        var err = new ByteArrayOutputStream();

        int status = Runner.run(new String[]{"--day", "01", "--input", file.toString()},
                new PrintStream(out), new PrintStream(err));

        assertEquals(0, status);
        assertTrue(out.toString(StandardCharsets.UTF_8).contains("part 1: 50"));
        assertTrue(out.toString(StandardCharsets.UTF_8).contains("part 2: 50"));
        assertEquals("", err.toString(StandardCharsets.UTF_8));
    }

    @Test
    void runsStringInputFromAnExternalFile() throws Exception {
        var file = tempDir.resolve("sequence.txt");
        Files.writeString(file, "rn=1,cm-\r\n", StandardCharsets.UTF_8);
        var out = new ByteArrayOutputStream();
        var err = new ByteArrayOutputStream();

        int status = Runner.run(new String[]{"--day", "15", "--input", file.toString()},
                new PrintStream(out), new PrintStream(err));

        assertEquals(0, status);
        assertTrue(out.toString(StandardCharsets.UTF_8).contains("part 1: 283"));
        assertTrue(out.toString(StandardCharsets.UTF_8).contains("part 2: 1"));
        assertEquals("", err.toString(StandardCharsets.UTF_8));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "café\r\n\r\nlast\r\n", "\n", "\r", "\r\n", " trailing "})
    void stringInputLoadingPreservesFileContents(String expected) throws Exception {
        var file = tempDir.resolve("input.txt");
        Files.writeString(file, expected, StandardCharsets.UTF_8);

        String input = Inputs.loadString(1, null, file);

        assertEquals(expected, input);
    }

    @Test
    void stringInputLoadingPreservesResourceContents() {
        String input = Inputs.resourceString("harness/whole-input.txt");

        assertEquals("café\n\n trailing \n", input);
        assertArrayEquals(new String[]{"café", "", " trailing "}, Inputs.resource("harness/whole-input.txt"));
    }

    @Test
    void inputLoadingPreservesBlankLinesAndUtf8() throws Exception {
        var file = tempDir.resolve("input.txt");
        Files.writeString(file, "café\n\nlast\n", StandardCharsets.UTF_8);

        var lines = Inputs.load(1, null, file);

        assertArrayEquals(new String[]{"café", "", "last"}, lines);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "--day", "--day nope", "--day 0", "--day 26",
            "--day 1 --part 3", "--unknown", "--part 1",
            "--day all --input input.txt", "--day 1 --input input.txt --resource test1",
            "--day 1 --resource missing", "--day 1 --input missing"
    })
    void reportsInvalidArgumentsAndMissingInputs(String arguments) {
        var out = new ByteArrayOutputStream();
        var err = new ByteArrayOutputStream();

        int status = Runner.run(arguments.split(" "), new PrintStream(out), new PrintStream(err));

        assertEquals(2, status);
        assertTrue(err.toString(StandardCharsets.UTF_8).startsWith("Error: "));
    }

    @Test
    void showsHelpAndListsDaysWithoutRunningSolutions() {
        var out = new ByteArrayOutputStream();
        var err = new ByteArrayOutputStream();

        assertEquals(0, Runner.run(new String[0], new PrintStream(out), new PrintStream(err)));
        assertTrue(out.toString(StandardCharsets.UTF_8).contains("Usage:"));

        out.reset();

        assertEquals(0, Runner.run(new String[]{"--list"}, new PrintStream(out), new PrintStream(err)));
        assertTrue(out.toString(StandardCharsets.UTF_8).contains("Available days: [1, 2, 3"));
        assertEquals("", err.toString(StandardCharsets.UTF_8));
    }
}
