package net.ddns.spellbank.day06;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

class TestDay06 {

    @Test
    void part1() {
        String[] lines = InputFile.getLines("day06/input1");
        assertEquals(4568778, Day06.part1(lines));
    }

    @Test
    void part1_sample() {
        String[] lines = InputFile.getLines("day06/test1");
        assertEquals(288, Day06.part1(lines));
    }

    @Test
    void part2() {
        String[] lines = InputFile.getLines("day06/input1");
        assertEquals(28973936, Day06.part2(lines));
    }

    @Test
    void part2_sample() {
        String[] lines = InputFile.getLines("day06/test1");
        assertEquals(71503, Day06.part2(lines));
    }
}