package net.ddns.spellbank.day10;

import net.ddns.spellbank.utils.InputFile;

import java.util.HashMap;
import java.util.Map;

public class Day10 {
    public record Point(int x, int y){}
    public record Pipe(char type, Point[] exits){}
    public record ParseResult(Point start, Map<Point, Pipe> map){}
    public record Part1Result(long farthest, Map<Point, Character> loop){}

    public static void main(String[] args) {
        String file = "day10/input1";
        String[] lines = InputFile.getLines(file);
        var r = part1(lines);
        System.out.println(r.farthest); // 7005
        System.out.println(part2(lines.length, lines[0].length(), r.loop)); // 417
    }

    public static Part1Result part1(String[] lines) {
        long dist = 1;
        var result = parse(lines);
        var prev = result.start;
        Point pos = null;
        var directions = new Point[4];
        directions[0] = new Point(prev.x, prev.y - 1);
        directions[1] = new Point(prev.x + 1, prev.y);
        directions[2] = new Point(prev.x, prev.y + 1);
        directions[3] = new Point(prev.x - 1, prev.y);
        int startDirection1 = -1;
        int startDirection2 = -1;
        for (int i = 0; i < directions.length && startDirection2 == -1; i++) {
            var d = directions[i];
            var p = result.map.getOrDefault(d, null);
            if (p != null && (p.exits[0].equals(prev) || p.exits[1].equals(prev))) {
                pos = d;
                if (startDirection1 == -1) startDirection1 = i;
                else startDirection2 = i;
            }
        }

        char c = switch(startDirection1) {
            case 0:
                if (startDirection2 == 1) yield 'L';
                else if (startDirection2 == 2) yield '|';
                yield 'J';
            case 1:
                if (startDirection2 == 2) yield 'F';
                yield '-';
            case 2:
                yield '7';
            default:
                throw new IllegalArgumentException("Invalid input");
        };

        var loop = new HashMap<Point, Character>();
        loop.put(prev, c);

        while (pos != null && !pos.equals(result.start)) {
            dist++;
            var v = result.map.get(pos);
            loop.put(pos, v.type);
            var next = v.exits[0].equals(prev) ? v.exits[1] : v.exits[0];
            prev = pos;
            pos = next;
        }

        return new Part1Result(++dist / 2, loop);
    }

    public static long part2(int len, int width, Map<Point,Character> map) {
        long count = 0;
        for (int y = 0; y < len; y++) {
            boolean inside = false;
            char prev = '.';
            for (int x = 0; x < width; x++) {
                var p = map.get(new Point(x, y));
                if (p != null) {
                    switch (p) {
                        case '|' -> inside = !inside;
                        case 'F', 'L' -> prev = p;
                        case 'J' -> { if (prev == 'F') inside = !inside; }
                        case '7' -> { if (prev == 'L') inside = !inside; }
                        default -> {}
                    }
                } else if (inside) count++;
            }
        }
        return count;
    }

    public static ParseResult parse(String[] lines) {
        var map = new HashMap<Point, Pipe>();
        Point start = new Point(0, 0);
        for (int y = 0; y < lines.length; y++) {
            var line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                boolean pipe = true;
                var exits = new Point[2];
                char c = line.charAt(x);
                switch (c) {
                    case '|' -> {
                        exits[0] = new Point(x, y - 1);
                        exits[1] = new Point(x, y + 1);
                    }
                    case '-' -> {
                        exits[0] = new Point(x - 1, y);
                        exits[1] = new Point(x + 1, y);
                    }
                    case 'L' -> {

                        exits[0] = new Point(x, y - 1);
                        exits[1] = new Point(x + 1, y);
                    }
                    case 'J' -> {
                        exits[0] = new Point(x, y - 1);
                        exits[1] = new Point(x - 1, y);
                    }
                    case '7' -> {
                        exits[0] = new Point(x - 1, y);
                        exits[1] = new Point(x, y + 1);
                    }
                    case 'F' -> {
                        exits[0] = new Point(x + 1, y);
                        exits[1] = new Point(x, y + 1);
                    }
                    case 'S' -> {
                        start = new Point(x, y);
                        pipe = false;
                    }
                    default -> pipe = false;
                }
                if (pipe) map.put(new Point(x, y), new Pipe(c, exits));
            }
        }
        return new ParseResult(start, map);
    }
}