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

        long first = solution.solve(2, lines.clone());
        long second = solution.solve(2, lines.clone());

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
