package net.ddns.spellbank.day18;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

import java.util.List;

class TestDay18 {
    static List<Day18.Instruction> instructions, testInstructions;

    @BeforeAll
    static void init() {
        String[] lines = InputFile.getLines("day18/input1");
        instructions = Day18.getInstructions(lines);
        lines = InputFile.getLines("day18/test1");
        testInstructions = Day18.getInstructions(lines);
    }

    @Test
    void part1() {
        String[] lines = InputFile.getLines("day18/input1");
        assertEquals(62573, Day18.part1(instructions));
    }

    @Test
    void part1_sample() {
        String[] lines = InputFile.getLines("day18/input1");
        assertEquals(62, Day18.part1(testInstructions));
    }

    @Test
    void part2() {
        String[] lines = InputFile.getLines("day18/input1");
        assertEquals(54662804037719L, Day18.part2(instructions));
    }

    @Test
    void part2_sample() {
        String[] lines = InputFile.getLines("day18/input1");
        assertEquals(952408144115L, Day18.part2(testInstructions));
    }
}