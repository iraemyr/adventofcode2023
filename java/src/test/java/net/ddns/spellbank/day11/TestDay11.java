package net.ddns.spellbank.day11;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

class TestDay11 {
    static Day11.ParseResult input;
    static Day11.ParseResult sample;

    @BeforeAll
    public static void init() {
        sample = Day11.parse(InputFile.getLines("day11/test1"));
        input = Day11.parse(InputFile.getLines("day11/input1"));
    }

    @Test
    void part1() {
        assertEquals(9591768, Day11.part1(input, 2));
    }

    @Test
    void part1_sample() {
        assertEquals(374, Day11.part1(sample, 2));
    }

    @Test
    void part2() {
        assertEquals(746962097860L, Day11.part1(input, 1_000_000));
    }

    @Test
    void part2_sample() {
        assertEquals(8410, Day11.part1(sample, 100));
    }
}