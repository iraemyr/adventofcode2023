package net.ddns.spellbank.day10;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

class TestDay10 {

    @Test
    void part1() {
        String[] lines = InputFile.getLines("day10/input1");
        assertEquals(7005, Day10.part1(lines).farthest());
    }

    @Test
    void part1_sample() {
        String[] lines = InputFile.getLines("day10/test1");
        assertEquals(8, Day10.part1(lines).farthest());
    }

    @Test
    void part2() {
        String[] lines = InputFile.getLines("day10/input1");
        var r = Day10.part1(lines);
        assertEquals(417, Day10.part2(lines.length, lines[0].length(), r.loop()));
    }

    @Test
    void part2_sample1() {
        String[] lines = InputFile.getLines("day10/test1");
        var r = Day10.part1(lines);
        assertEquals(1, Day10.part2(lines.length, lines[0].length(), r.loop()));
    }

    @Test
    void part2_sample2() {
        String[] lines = InputFile.getLines("day10/test2");
        var r = Day10.part1(lines);
        assertEquals(8, Day10.part2(lines.length, lines[0].length(), r.loop()));
    }
}