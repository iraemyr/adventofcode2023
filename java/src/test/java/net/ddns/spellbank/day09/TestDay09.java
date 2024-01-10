package net.ddns.spellbank.day09;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import net.ddns.spellbank.utils.InputFile;

class TestDay09 {

    private static List<List<Long>>  sequences;
    private static List<List<Long>>  sample_sequences;

    @BeforeAll
    static void init() {
        String[] lines = InputFile.getLines("day09/input1");
        sequences = Day09.parse(lines);
        lines = InputFile.getLines("day09/test1");
        sample_sequences = Day09.parse(lines);
    }

    @Test
    void part1() {
        assertEquals(1987402313, Day09.part1(sequences));
    }

    @Test
    void part1_sample() {
        String[] lines = InputFile.getLines("day09/test1");
        assertEquals(114, Day09.part1(sample_sequences));
    }

    @Test
    void part2() {
        String[] lines = InputFile.getLines("day09/input1");
        assertEquals(900, Day09.part2(sequences));
    }

    @Test
    void part2_sample() {
        String[] lines = InputFile.getLines("day09/test1");
        assertEquals(2, Day09.part2(sample_sequences));
    }
}