package net.ddns.spellbank.day12;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

class TestDay12 {

    static Day12.ParseResult input;
    static Day12.ParseResult sample;

    @BeforeAll
    static void init() {
        String[] lines = InputFile.getLines("day12/input1");
        input = Day12.parse(lines);
        lines = InputFile.getLines("day12/test1");
        sample = Day12.parse(lines);
    }

    @Test
    void part1() {
        assertEquals(7350, Day12.part1(input.rows(), input.groups()));
    }

    @Test
    void part1_sample() {
        assertEquals(21, Day12.part1(sample.rows(), sample.groups()));
    }

    @Test
    void part2() {
        String[] lines = InputFile.getLines("day12/input1");
        assertEquals(200097286528151L, Day12.part2(input.rows(), input.groups()));
    }

    @Test
    void part2_sample() {
        String[] lines = InputFile.getLines("day12/input1");
        assertEquals(525152, Day12.part2(sample.rows(), sample.groups()));
    }
}