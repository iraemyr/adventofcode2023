package net.ddns.spellbank.day15;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

class TestDay15 {

    @Test
    void part1() {
        String[] lines = InputFile.getLines("day15/input1");
        assertEquals(514394, Day15.part1(lines));
    }

    @Test
    void part1_sample() {
        String[] lines = InputFile.getLines("day15/test1");
        assertEquals(1320, Day15.part1(lines));
    }

    @Test
    void part2() {
        String[] lines = InputFile.getLines("day15/input1");
        assertEquals(236358, Day15.part2(lines));
    }

    @Test
    void part2_sample() {
        String[] lines = InputFile.getLines("day15/test1");
        assertEquals(145, Day15.part2(lines));
    }
}