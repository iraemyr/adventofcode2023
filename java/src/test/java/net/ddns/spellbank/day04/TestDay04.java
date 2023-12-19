package net.ddns.spellbank.day04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

class TestDay04 {

    @Test
    void part1() {
        String[] lines = InputFile.getLines("day04/input1");
        assertEquals(24706, Day04.part1(lines));
    }

    @Test
    void part1_sample() {
        String[] lines = InputFile.getLines("day04/test1");
        assertEquals(13, Day04.part1(lines));
    }

    @Test
    void part2() {
        String[] lines = InputFile.getLines("day04/input1");
        assertEquals(13114317, Day04.part2(lines));
    }

    @Test
    void part2_sample() {
        String[] lines = InputFile.getLines("day04/test1");
        assertEquals(30, Day04.part2(lines));
    }
}