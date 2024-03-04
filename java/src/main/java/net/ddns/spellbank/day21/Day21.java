package net.ddns.spellbank.day21;

import net.ddns.spellbank.utils.InputFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day21 {

    public record ParseResult(Point start, char[][] grid) {}
    public record Point(int row, int col) {

        public List<Point> neighborsTile(char[][] grid) {
            var neighbors = new ArrayList<Point>();
            if (grid[wrap(row + 1, grid.length)][wrap(col, grid.length)] == '.')
                neighbors.add(new Point(row + 1, col));
            if (grid[wrap(row - 1, grid.length)][wrap(col, grid.length)] == '.')
                neighbors.add(new Point(row - 1, col));
            if (grid[wrap(row, grid.length)][wrap(col + 1, grid.length)] == '.')
                neighbors.add(new Point(row, col + 1));
            if (grid[wrap(row, grid.length)][wrap(col - 1, grid.length)] == '.')
                neighbors.add(new Point(row, col - 1));
            return neighbors;
        }

        public int wrap(int x, int len) {
            int remainder = x % len;
            return remainder < 0 ? remainder + len : remainder;
        }
    }

    public static void main(String[] args) {
        String file = "day21/input1";
        String[] lines = InputFile.getLines(file);
        var info = parse(lines);

        System.out.println(part1(info, 64)); // 3651
        System.out.println(part2(info)); // 607334325965751
    }

    public static long part1(ParseResult info, int steps) {
        var even = new HashSet<Point>();
        var odd = new HashSet<Point>();
        sim(steps, odd, even, info.start, info.grid);
        return steps % 2 == 0 ? even.size() : odd.size();
    }

    public static long part2(ParseResult info) {
        var even = new HashSet<Point>();
        var odd = new HashSet<Point>();
        var result = sim(info.grid.length * 2 + info.grid.length / 2, odd, even, info.start, info.grid);
        var c = result[0];
        var a = (result[2] - 2 * result[1] + c) / 2;
        var b = result[1] - a - c;
        long x = (26501365 - (info.grid.length / 2)) / info.grid.length;
        return a * x * x + b * x + c;
    }

    public static ParseResult parse(String[] lines) {
        var grid = new char[lines.length][lines[0].length()];
        var start = new Point(0, 0);
        for (int i = 0; i < lines.length; i++)
            for (int j = 0; j < lines[i].length(); j++) {
                var c = lines[i].charAt(j);
                if (c != 'S') grid[i][j] = c;
                else {
                    start = new Point(i, j);
                    grid[i][j] = '.';
                }
            }
        return new ParseResult(start, grid);
    }

    private static int[] sim(int steps, Set<Point> odd, Set<Point> even, Point start, char[][] grid) {
        var plots = new HashSet<Point>();
        var plots2= new HashSet<Point>();
        int[] samples = new int[3];
        plots.add(start);
        boolean isEven = true;
        for (int i = 1; i <= steps; i++) {
            isEven = !isEven;
            var s = isEven ? even : odd;
            for (var p : plots) {
                var neighbors = p.neighborsTile(grid);
                for (var pl : neighbors)
                    if (!s.contains(pl)) plots2.add(pl);
                s.addAll(plots2);
            }
            var tmp = plots;
            plots = plots2;
            plots2 = tmp;
            plots2.clear();

            if (i % grid.length == grid.length / 2)
                samples[i / grid.length] = s.size();
        }
        return samples;
    }
}