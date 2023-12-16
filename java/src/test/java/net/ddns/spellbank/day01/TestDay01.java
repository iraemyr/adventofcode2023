package net.ddns.spellbank.day01;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

class TestDay01 {

    @Test
    void part1() {
        String[] lines = InputFile.getLines("day01/input1");
        assertEquals(54644, Day01.part1(lines));
    }

    @Test
    void part1_sample() {
        String[] lines = InputFile.getLines("day01/test1");
        assertEquals(142, Day01.part1(lines));
    }

    @Test
    void part2() {
        String[] lines = InputFile.getLines("day01/input1");
        assertEquals(53348, Day01.part2(lines));
    }

    @Test
    void part2_sample() {
        String[] lines = InputFile.getLines("day01/test2");
        assertEquals(281, Day01.part2(lines));
    }
}