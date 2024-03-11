package net.ddns.spellbank.day22;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.ddns.spellbank.day21.Day21;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

class TestDay22 {
    static List<Day22.Slab> input;
    static List<Day22.Slab> sample;

    @BeforeAll
    static void init() {
        var lines = InputFile.getLines("day22/input1");
        var sampleLines = InputFile.getLines("day22/test1");
        input = Day22.parseSlabs(lines);
        Collections.sort(input);
        var map = new HashMap<Day22.Point, Day22.Slab>();
        for (var slab : input) slab.drop(map);
        sample = Day22.parseSlabs(sampleLines);
        map.clear();
        Collections.sort(sample);
        for (var slab : sample) slab.drop(map);
    }

    @Test
    void part1() {
        assertEquals(432, Day22.part1(input));
    }

    @Test
    void part1_sample() {
        assertEquals(5, Day22.part1(sample));
    }

    @Test
    void part2() {
        assertEquals(63166, Day22.part2(input));
    }

    @Test
    void part2_sample() {
        assertEquals(7, Day22.part2(sample));
    }
}