package net.ddns.spellbank.day07;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

class TestDay07 {

    @Test
    void part1() {
        String[] lines = InputFile.getLines("day07/input1");
        assertEquals(249483956, Day07.solution(lines, false));
    }

    @Test
    void part1_sample() {
        String[] lines = InputFile.getLines("day07/test1");
        assertEquals(6440, Day07.solution(lines, false));
    }

    @Test
    void part2() {
        String[] lines = InputFile.getLines("day07/input1");
        assertEquals(252137472, Day07.solution(lines, true));
    }

    @Test
    void part2_sample() {
        String[] lines = InputFile.getLines("day07/test1");
        assertEquals(5905, Day07.solution(lines, true));
    }
}