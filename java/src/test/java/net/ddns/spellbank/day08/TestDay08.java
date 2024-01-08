package net.ddns.spellbank.day08;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

import java.util.HashMap;

class TestDay08 {

    @Test
    void part1() {
        String[] lines = InputFile.getLines("day08/input1");
        var map = new HashMap<String, String[]>();
        var directions = Day08.parse(lines, map);
        assertEquals(19783, Day08.part1(directions, map, "AAA"));
    }

    @Test
    void part1_sample1() {
        String[] lines = InputFile.getLines("day08/test1");
        var map = new HashMap<String, String[]>();
        var directions = Day08.parse(lines, map);
        assertEquals(2, Day08.part1(directions, map, "AAA"));
    }

    @Test
    void part1_sample2() {
        String[] lines = InputFile.getLines("day08/test2");
        var map = new HashMap<String, String[]>();
        var directions = Day08.parse(lines, map);
        assertEquals(6, Day08.part1(directions, map, "AAA"));
    }

    @Test
    void part2() {
        String[] lines = InputFile.getLines("day08/input1");
        var map = new HashMap<String, String[]>();
        var directions = Day08.parse(lines, map);
        assertEquals(9177460370549L, Day08.part2(directions, map));
    }

    @Test
    void part2_sample() {
        String[] lines = InputFile.getLines("day08/test3");
        var map = new HashMap<String, String[]>();
        var directions = Day08.parse(lines, map);
        assertEquals(6, Day08.part2(directions, map));
    }
}