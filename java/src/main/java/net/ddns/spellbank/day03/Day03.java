package net.ddns.spellbank.day03;

import net.ddns.spellbank.utils.InputFile;

import java.util.*;

public class Day03 {

    public static record Coord(int row, int col) {};

    public static void main(String[] args) {
        String file = "day03/input1";
        String[] lines = InputFile.getLines(file);

        System.out.println(part1(lines)); // 528819
        System.out.println(part2(lines)); // 80403602
    }

    public static long part1(String[] lines) {
        var map = new HashMap<Coord, Character>();
        var numbers = new ArrayList<PartNumber>();
        parseMaps(lines, map, numbers);

        long sum = 0;
        for (var n : numbers) if (n.isAdjacent(map)) {
            sum += n.getVal();
        }
        return sum;
    }

    public static long part2(String[] lines) {
        var map = new HashMap<Coord, Character>();
        var numbers = new ArrayList<PartNumber>();
        parseMaps(lines, map, numbers);
        var numMap = getNumberMap(numbers);

        long sum = 0;
        for (var e : map.entrySet()) {
            if (e.getValue() == '*') {
                var s = new HashSet<Integer>();
                for (int r = e.getKey().row - 1; r <= e.getKey().row + 1; r++) {
                    for (int c = e.getKey().col - 1; c <= e.getKey().col + 1; c++) {
                        var coord = new Coord(r, c);
                        if (numMap.containsKey(coord)) s.add(numMap.get(coord));
                    }
                }
                if (s.size() == 2) {
                    long val = 1;
                    for (var num : s) val *= num;
                    sum += val;
                }
            }
        }
        return sum;
    }

    private static void parseMaps(String[] lines, Map<Coord, Character> map, List<PartNumber> numbers) {
        int currentVal = -1;
        int colFirst = -1;

        for (int r = 0; r < lines.length; r++) {
            var line = lines[r].toCharArray();
            for (int c = 0; c < line.length; c++) {
                var ch = line[c];
                if (Character.isDigit(ch)) {
                    if (currentVal == -1) {
                        colFirst = c;
                        currentVal = ch - '0';
                    } else {
                        currentVal = currentVal * 10 + (ch - '0');
                    }
                } else {
                    if (currentVal != -1) {
                        numbers.add(new PartNumber(colFirst, c - 1, r, currentVal));
                        currentVal = -1;
                        colFirst = -1;
                    }
                    if (ch != '.') map.put(new Coord(r, c), ch);
                }
                if (c == line.length - 1 && currentVal != -1) {
                    numbers.add(new PartNumber(colFirst, c, r, currentVal));
                    currentVal = -1;
                    colFirst = -1;
                }
            }
        }
    }

    private static Map<Coord, Integer> getNumberMap(List<PartNumber> numbers) {
        var map = new HashMap<Coord, Integer>();
        for (var n : numbers) {
            for (int i = n.getColFirst(); i <= n.getColLast(); i++)
                map.put(new Coord(n.getRow(), i), n.getVal());
        }
        return map;
    }
}