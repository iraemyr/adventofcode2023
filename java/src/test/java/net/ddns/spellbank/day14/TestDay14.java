package net.ddns.spellbank.day14;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

class TestDay14 {

    @Test
    void part1() {
        String[] lines = InputFile.getLines("day14/input1");
        var grid = Day14.getGrid(lines);
        assertEquals(109661, Day14.part1(grid));
    }

    @Test
    void part1_sample() {
        String[] lines = InputFile.getLines("day14/test1");
        var grid = Day14.getGrid(lines);
        assertEquals(136, Day14.part1(grid));
    }

    @Test
    void part2() {
        String[] lines = InputFile.getLines("day14/input1");
        var grid = Day14.getGrid(lines);
        assertEquals(90176, Day14.part2(grid));
    }

    @Test
    void part2_sample() {
        String[] lines = InputFile.getLines("day14/test1");
        var grid = Day14.getGrid(lines);
        assertEquals(64, Day14.part2(grid));
    }
}