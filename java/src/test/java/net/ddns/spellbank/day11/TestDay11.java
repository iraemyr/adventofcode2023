package net.ddns.spellbank.day11;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

class TestDay11 {
    static Day11.ParseResult sample;

    @BeforeAll
    static void init() {
        sample = Day11.parse(InputFile.getLines("day11/test1"));
    }

    @Test
    void part2_sample() {
        assertEquals(8410, Day11.part1(sample, 100));
    }
}
