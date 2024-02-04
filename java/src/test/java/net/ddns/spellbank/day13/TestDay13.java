package net.ddns.spellbank.day13;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

class TestDay13 {

    @Test
    void part1() {
        String[] lines = InputFile.getLines("day13/input1");
        assertEquals(32035, Day13.reflectionSum(lines, 0));
    }

    @Test
    void part1_sample() {
        String[] lines = InputFile.getLines("day13/test1");
        assertEquals(405, Day13.reflectionSum(lines, 0));
    }

    @Test
    void part2() {
        String[] lines = InputFile.getLines("day13/input1");
        assertEquals(24847, Day13.reflectionSum(lines, 1));
    }

    @Test
    void part2_sample() {
        String[] lines = InputFile.getLines("day13/test1");
        assertEquals(400, Day13.reflectionSum(lines, 1));
    }
}