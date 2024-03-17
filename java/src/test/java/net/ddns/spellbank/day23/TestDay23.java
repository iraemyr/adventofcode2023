package net.ddns.spellbank.day23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

class TestDay23 {

    static Day23.ParseMapResult map;
    static Day23.ParseMapResult sampleMap;

    @BeforeAll
    static void init() {
        var lines = InputFile.getLines("day23/input1");
        map = Day23.parseMap(lines);
        lines = InputFile.getLines("day23/test1");
        sampleMap = Day23.parseMap(lines);
    }

    @Test
    void part1() {
        assertEquals(2018, Day23.part1(map));
    }

    @Test
    void part1_sample() {
        assertEquals(94, Day23.part1(sampleMap));
    }

    @Test
    void part2() {
        assertEquals(6406, Day23.part2(map));
    }

    @Test
    void part2_sample() {
        assertEquals(154, Day23.part2(sampleMap));
    }
}