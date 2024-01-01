package net.ddns.spellbank.day05;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

class TestDay05 {

    @Test
    void part1() {
        String[] lines = InputFile.getLines("day05/input1");
        assertEquals(1181555926, Day05.part1(lines));
    }

    @Test
    void part1_sample() {
        String[] lines = InputFile.getLines("day05/test1");
        assertEquals(35, Day05.part1(lines));
    }

    @Test
    void part2() {
        String[] lines = InputFile.getLines("day05/input1");
        assertEquals(37806486, Day05.part2(lines));
    }

    @Test
    void part2_sample() {
        String[] lines = InputFile.getLines("day05/test1");
        assertEquals(46, Day05.part2(lines));
    }
}