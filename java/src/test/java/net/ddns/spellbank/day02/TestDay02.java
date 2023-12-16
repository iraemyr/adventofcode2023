package net.ddns.spellbank.day02;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

class TestDay02 {

    @Test
    void part1() {
        String[] lines = InputFile.getLines("day02/input1");
        assertEquals(2913, Day02.part1(lines));
    }

    @Test
    void part1_sample() {
        String[] lines = InputFile.getLines("day02/test1");
        assertEquals(8, Day02.part1(lines));
    }

    @Test
    void part2() {
        String[] lines = InputFile.getLines("day02/input1");
        assertEquals(55593, Day02.part2(lines));
    }

    @Test
    void part2_sample() {
        String[] lines = InputFile.getLines("day02/test1");
        assertEquals(2286, Day02.part2(lines));
    }
}