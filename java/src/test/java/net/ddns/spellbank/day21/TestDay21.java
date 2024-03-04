package net.ddns.spellbank.day21;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

class TestDay21 {
    static String[] lines;
    static String[] sampleLines;
    static Day21.ParseResult input;
    static Day21.ParseResult sample;

    @BeforeAll
    static void init() {
        lines = InputFile.getLines("day21/input1");
        sampleLines = InputFile.getLines("day21/test1");
        input = Day21.parse(lines);
        sample = Day21.parse(sampleLines);
    }

    @Test
    void part1() {
        assertEquals(3651, Day21.part1(input, 64));
    }

    @Test
    void part1_sample() {
        assertEquals(16, Day21.part1(sample, 6));
    }

    @Test
    void part1_sample2() {
        assertEquals(6, Day21.part1(sample, 3));
    }

    @Test
    void part2() {
        String[] lines = InputFile.getLines("day21/input1");
        assertEquals(607334325965751L, Day21.part2(input));
    }
}