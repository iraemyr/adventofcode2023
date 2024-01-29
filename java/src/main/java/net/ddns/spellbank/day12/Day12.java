package net.ddns.spellbank.day12;

import net.ddns.spellbank.utils.InputFile;

import java.util.*;

public class Day12 {

    public record ParseResult(List<char[]> rows, List<int[]> groups) {}
    public record State(int rowIndex, int groupIndex, long count) {}

    public static void main(String[] args) {
        String file = "day12/input1";
        String[] lines = InputFile.getLines(file);
        var result = parse(lines);

        System.out.println(part1(result.rows, result.groups)); // 7350
        System.out.println(part2(result.rows, result.groups)); // 200097286528151
    }

    public static long part1(List<char[]> rows, List<int[]> groups) {
        long sum = 0;
        var map = new HashMap<State, Long>();
        for (int i = 0; i < rows.size(); i++) {
            map.clear();
            var c = possible(rows.get(i), groups.get(i), new State(0, 0, 0), map);
            sum += c;
        }
        return sum;
    }

    public static long part2(List<char[]> rows, List<int[]> groups) {
        long sum = 0;
        var map = new HashMap<State, Long>();
        for (int i = 0; i < rows.size(); i++) {
            var row = rows.get(i);
            var group = groups.get(i);
            var sb = new StringBuilder();
            int[] expanded = new int[5 * group.length];
            int index = 0;
            for (int j = 0; j < 5; j++) {
                if (j != 0) sb.append('?');
                sb.append(row);
                for (int k : group) expanded[index++] = k;
            }
            map.clear();
            sum += possible(sb.toString().toCharArray(), expanded, new State(0, 0, 0), map);
        }
        return sum;
    }

    public static ParseResult parse(String[] lines) {
        var rows = new ArrayList<char[]>();
        var groups = new ArrayList<int[]>();

        for (var line : lines) {
            var fields = line.trim().split(" ");
            rows.add(fields[0].toCharArray());
            var nums = fields[1].split(",");
            var n = new int[nums.length];
            for (int i = 0; i < n.length; i++)
                n[i] = Integer.parseInt(nums[i]);
            groups.add(n);
        }
        return new ParseResult(rows, groups);
    }

    private static long possible(char[] row, int[] groups, State s, Map<State, Long> map) {
        if (s == null) throw new IllegalArgumentException("Invalid state");
        var result = map.get(s);
        if (result != null) return result;
        if (s.rowIndex == row.length)
            return s.groupIndex == groups.length ||
                    (s.groupIndex == groups.length - 1 && s.count == groups[s.groupIndex])
                    ? 1 : 0L;
        var c = row[s.rowIndex];
        result = 0L;
        if (c == '#' || c == '?') {
            if (s.groupIndex < groups.length && s.count + 1 <= groups[s.groupIndex])
                result += possible(row, groups, new State(s.rowIndex + 1, s.groupIndex, s.count + 1), map);
        }
        if (c == '.' || c == '?') {
            if (s.count > 0) {
                if (s.count == groups[s.groupIndex])
                    result +=  possible(row, groups, new State(s.rowIndex + 1, s.groupIndex + 1, 0), map);
            } else result += possible(row, groups, new State(s.rowIndex + 1, s.groupIndex, 0), map);
        }
        map.put(s, result);
        return result;
    }
}