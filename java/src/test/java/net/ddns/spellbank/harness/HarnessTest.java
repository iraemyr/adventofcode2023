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

    @ParameterizedTest(name = "day {0} keeps both answers")
    @CsvSource(textBlock = """
            1, 54644, 53348
            2, 2913, 55593
            3, 528819, 80403602
            4, 24706, 13114317
            5, 1181555926, 37806486
            6, 4568778, 28973936
            7, 249483956, 252137472
            8, 19783, 9177460370549
            9, 1987402313, 900
            10, 7005, 417
            11, 9591768, 746962097860
            12, 7350, 200097286528151
            13, 32035, 24847
            14, 109661, 90176
            15, 514394, 236358
            16, 7870, 8143
            17, 866, 1010
            18, 62573, 54662804037719
            19, 406849, 138625360533574
            20, 747304011, 220366255099387
            21, 3651, 607334325965751
            22, 432, 63166
            23, 2018, 6406
            """)
    void adaptersPreserveAnswers(int day, long part1, long part2) {
        var solution = Solutions.forDay(day);
        var lines = Inputs.load(day, "input1", null);

        long first = solution.solve(1, lines.clone());
        long second = solution.solve(2, lines.clone());

        assertEquals(part1, first);
        assertEquals(part2, second);
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

        var lines = Inputs.load(1, "input1", file);

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
