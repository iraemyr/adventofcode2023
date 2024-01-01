package net.ddns.spellbank.day05;

import net.ddns.spellbank.utils.InputFile;

import java.util.*;

public class Day05 {

    public record Mapping(long inclusiveStart, long exclusiveEnd, long dest) implements Comparable<Mapping> {
        @Override
        public int compareTo(Mapping o) {
            if (this.inclusiveStart < o.inclusiveStart) return -1;
            return this.inclusiveStart == o.inclusiveStart ? 0 : 1;
        }
    }

    public record Range(long start, long end) implements Comparable<Range> {
        @Override
        public int compareTo(Range o) {
            if (this.start < o.start) return -1;
            return this.start == o.start ? 0 : 1;
        }
    }

    public static void main(String[] args) {
        String file = "day05/input1";
        String[] lines = InputFile.getLines(file);

        System.out.println(part1(lines)); // 1181555926
        System.out.println(part2(lines)); // 37806486
    }

    public static long part1(String[] lines) {
        long min = Long.MAX_VALUE;
        var seeds = new ArrayList<Long>();
        var maps = new ArrayList<List<Mapping>>();
        parse(lines, seeds, maps);

        for (var seed : seeds) {
            for (var map: maps) {
                for (var m : map) {
                    if (seed >= m.inclusiveStart && seed < m.exclusiveEnd) {
                        var diff = seed - m.inclusiveStart;
                        seed = m.dest + diff;
                        break;
                    }
                }
            }
            min = Math.min(min, seed);
        }
        return min;
    }

    public static long part2(String[] lines) {
        var seedList = new ArrayList<Long>();
        var maps = new ArrayList<List<Mapping>>();
        parse(lines, seedList, maps);
        var seeds = getSeedRanges(seedList);
        var seeds2 = new PriorityQueue<Range>();
        for (var map : maps) {
            while (!seeds.isEmpty()) {
                var range = seeds.poll();
                boolean done = false;
                for (var m : map) {
                    if (range.end <= m.inclusiveStart) { // Range not mapped
                        seeds2.offer(range);
                        done = true;
                        break;
                    }
                    if (range.start < m.inclusiveStart) { // Start of range not mapped
                        seeds2.offer(new Range(range.start, m.inclusiveStart));
                        range = new Range(m.inclusiveStart, range.end);
                    }
                    if (range.start >= m.inclusiveStart && range.start < m.exclusiveEnd) {
                        if (range.end <= m.exclusiveEnd) { // Full range mapped
                            seeds2.offer(new Range(m.dest + range.start - m.inclusiveStart,
                                    m.dest + range.end - m.inclusiveStart));
                            done = true;
                            break;
                        }
                        // Front part of range mapped
                        seeds2.offer(new Range(m.dest + range.start - m.inclusiveStart,
                                m.dest + m.exclusiveEnd - m.inclusiveStart));
                        range = new Range(m.exclusiveEnd, range.end);
                    }
                }
                if (!done) seeds2.offer(range); // Unmapped leftover range
            }
            var tmp = seeds;
            seeds = seeds2;
            seeds2 = tmp;
        }
        return seeds.isEmpty() ? -1 : seeds.peek().start;
    }

    public static void parse(String[] lines, List<Long> seeds, List<List<Mapping>> maps) {
        int index = 0;
        var seedFields = lines[index++].split(": ");
        index++;
        for (var s : seedFields[1].split(" ")) {
            seeds.add(Long.parseLong(s.trim()));
        }
        while (index < lines.length) {
            index++;
            var mapping = new ArrayList<Mapping>();
            while (index < lines.length && !lines[index].trim().isEmpty()) {
                var vals = lines[index++].split(" ");
                long dest = Long.parseLong(vals[0]);
                long start = Long.parseLong(vals[1]);
                long len = Long.parseLong(vals[2]);
                mapping.add(new Mapping(start, start + len, dest));
            }
            Collections.sort(mapping);
            maps.add(mapping);
            index++;
        }
    }

    public static PriorityQueue<Range> getSeedRanges(List<Long> seedVals) {
        var q = new PriorityQueue<Range>();
        for (int i = 0; i < seedVals.size(); i += 2) {
            var start = seedVals.get(i);
            q.offer(new Range(start, start + seedVals.get(i + 1)));
        }
        return q;
    }
}