package net.ddns.spellbank.day16;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

class TestDay16 {

    private static char[][] grid;
    private static char[][] testGrid;

    @BeforeAll
    public static void init() {
        grid = Day16.getGrid(InputFile.getLines("day16/input1"));
        testGrid = Day16.getGrid(InputFile.getLines("day16/test1"));
    }

    @Test
    void part1() {
        assertEquals(7870, Day16.part1(grid));
    }

    @Test
    void part1_sample() {
        assertEquals(46, Day16.part1(testGrid));
    }

    @Test
    void part2() {
        assertEquals(8143, Day16.part2(grid));
    }

    @Test
    void part2_sample() {
        assertEquals(51, Day16.part2(testGrid));
    }
}