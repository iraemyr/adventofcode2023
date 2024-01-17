package net.ddns.spellbank.day11;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

class TestDay11 {

    @Test
    void part1() {
        String[] lines = InputFile.getLines("day11/input1");
        assertEquals(9591768, Day11.part1(lines, 2));
    }

    @Test
    void part1_sample() {
        String[] lines = InputFile.getLines("day11/test1");
        assertEquals(374, Day11.part1(lines, 2));
    }

    @Test
    void part2() {
        String[] lines = InputFile.getLines("day11/input1");
        assertEquals(746962097860L, Day11.part1(lines, 1_000_000));
    }

    @Test
    void part2_sample() {
        String[] lines = InputFile.getLines("day11/test1");
        assertEquals(8410, Day11.part1(lines, 100));
    }
}