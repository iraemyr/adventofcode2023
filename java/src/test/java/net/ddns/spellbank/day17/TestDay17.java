package net.ddns.spellbank.day17;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

class TestDay17 {
    static int[][] grid;
    static int[][] testGrid;

    @BeforeAll
    static void init() {
        String[] lines = InputFile.getLines("day17/input1");
        grid = Day17.getGrid(lines);
        lines = InputFile.getLines("day17/test1");
        testGrid = Day17.getGrid(lines);
    }

    @Test
    void part1() {
        assertEquals(866, Day17.part1(grid));
    }

    @Test
    void part1_sample() {
        assertEquals(102, Day17.part1(testGrid));
    }

    @Test
    void part2() {
        assertEquals(1010, Day17.part2(grid));
    }

    @Test
    void part2_sample() {
        assertEquals(94, Day17.part2(testGrid));
    }
}