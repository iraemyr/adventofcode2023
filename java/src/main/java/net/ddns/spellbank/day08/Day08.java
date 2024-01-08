package net.ddns.spellbank.day08;

import net.ddns.spellbank.utils.InputFile;
import net.ddns.spellbank.utils.MyUtils;

import java.util.Map;
import java.util.HashMap;

public class Day08 {

    public static void main(String[] args) {
        String file = "day08/input1";
        String[] lines = InputFile.getLines(file);

        var map = new HashMap<String, String[]>();
        var directions = parse(lines, map);

        System.out.println(part1(directions, map, "AAA")); // 19783
        System.out.println(part2(directions, map)); // 9177460370549
    }

    public static long part1(char[] directions, Map<String, String[]> map,String start) {
        var location = start;
        long steps = 0;
        int index = 0;
        while (location.charAt(location.length() - 1) != 'Z') {
            steps++;
            location = switch(directions[index++]) {
                case 'L':
                    yield map.get(location)[0];
                case 'R':
                    yield map.get(location)[1];
                default: throw new IllegalArgumentException("Invalid direction");
            };
            if (index == directions.length) index = 0;
        }
        return steps;
    }

    public static long part2(char[] directions, Map<String, String[]> map) {
        long cycle = 1;
        for (var loc : map.keySet())
            if (loc.charAt(loc.length() - 1) == 'A')
                cycle = MyUtils.lcm(cycle, part1(directions, map, loc));

        return cycle;
    }

    public static char[] parse(String[] lines, Map<String, String[]> map) {
        var directions = lines[0].trim().toCharArray();
        for (int i = 2; i < lines.length; i++) {
            var fields = lines[i].split(" = ");
            var node = fields[0].trim();
            var dests = fields[1].substring(1, fields[1].length() - 1).split(", ");
            map.put(node, dests);
        }
        return directions;
    }
}