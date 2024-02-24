package net.ddns.spellbank.day20;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

class TestDay20 {

    static String[] lines;
    static String[] sample1Lines;
    static String[] sample2Lines;

    @BeforeAll
    static void init() {
        lines = InputFile.getLines("day20/input1");
        sample1Lines = InputFile.getLines("day20/test1");
        sample2Lines = InputFile.getLines("day20/test2");
    }

    @Test
    void part1() {
        assertEquals(747304011, Day20.part1(lines));
    }

    @Test
    void part1_sample1() {
        assertEquals(32000000, Day20.part1(sample1Lines));
    }

    @Test
    void part1_sample2() {
        assertEquals(11687500, Day20.part1(sample2Lines));
    }

    @Test
    void part2() {
        assertEquals(220366255099387L, Day20.part2(lines));
    }
}