package net.ddns.spellbank.day19;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.ddns.spellbank.utils.InputFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TestDay19 {
    static Map<String, Day19.Workflow> workflows;
    static List<Day19.Part> parts;
    static Map<String, Day19.Workflow> sampleWorkflows;
    static List<Day19.Part> sampleParts;

    @BeforeAll
    static void init() {
        workflows = new HashMap<>();
        sampleWorkflows = new HashMap<>();
        parts = new ArrayList<>();
        sampleParts = new ArrayList<>();
        String[] lines = InputFile.getLines("day19/input1");
        Day19.parse(lines, workflows, parts);
        lines = InputFile.getLines("day19/test1");
        Day19.parse(lines, sampleWorkflows, sampleParts);
    }

    @Test
    void part1() {
        assertEquals(406849, Day19.part1(workflows, parts));
    }

    @Test
    void part1_sample() {
        assertEquals(19114, Day19.part1(sampleWorkflows, sampleParts));
    }

    @Test
    void part2() {
        assertEquals(138625360533574L, Day19.part2(workflows));
    }

    @Test
    void part2_sample() {
        assertEquals(167409079868000L, Day19.part2(sampleWorkflows));
    }
}