package net.ddns.spellbank.day14;

import net.ddns.spellbank.utils.InputFile;

import java.util.*;

public class Day14 {

    public record Point(int row, int col) {}

    public static void main(String[] args) {
        String file = "day14/input1";
        String[] lines = InputFile.getLines(file);

        var grid = getGrid(lines);

        System.out.println(part1(grid)); // 109661
        grid = getGrid(lines);
        System.out.println(part2(grid)); // 90176
    }

    public static long part1(char[][] grid) {
        long sum = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 'O') {
                    var index = findEmptyNorth(grid, i, j);
                    if (index != i) {
                        grid[index][j] = 'O';
                        grid[i][j] = '.';
                    }
                    sum += grid[0].length - index;
                }
            }
        }
        return sum;
    }

    public static long part2(char[][] grid) {
        long sum = 0;
        var points = new ArrayList<Point>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 'O') {
                    points.add(new Point(i, j));
                }
            }
        }
        var points2 = new ArrayList<Point>(points.size());

        long cycle = 0;
        long cycleStart, cycleLength;
        var map = new HashMap<List<Point>, Long>();
        map.put(Collections.unmodifiableList(points), cycle);
        while (true) {
            doCycle(grid, points, points2);
            cycle++;
            points.sort(Comparator.comparingInt(p -> p.row));
            var r = map.get(points);
            if (r != null) {
                cycleStart = r;
                cycleLength = cycle - cycleStart;
                break;
            }
            map.put(Collections.unmodifiableList(points), cycle);
        }

        long remaining = 1000000000L - cycleStart;
        remaining %= cycleLength;

        for (int i = 0; i < remaining; i++) {
            points.sort(Comparator.comparingInt(p -> p.row));
            doCycle(grid, points, points2);
        }

        for (var p : points) sum += grid[0].length - p.row;
        return sum;
    }

    public static char[][] getGrid(String[] lines) {
        var map = new char[lines.length][lines[0].length()];
        for (int i = 0; i < lines.length; i++)
            for (int j = 0; j < lines[0].length(); j++)
                map[i][j] = lines[i].charAt(j);
        return map;
    }

    private static void doCycle(char[][] grid, List<Point> points, List<Point> points2) {
        // Points already sorted for North
        for (var p : points) {
            var index = findEmptyNorth(grid, p.row, p.col);
            points2.add(new Point(index, p.col));
            grid[p.row][p.col] = '.';
            grid[index][p.col] = 'O';
        }
        var tmp = points;
        points = points2;
        points2 = tmp;
        points2.clear();

        points.sort(Comparator.comparingInt(p -> p.col));
        for (var p : points) {
            var index = findEmptyWest(grid, p.row, p.col);
            points2.add(new Point(p.row, index));
            grid[p.row][p.col] = '.';
            grid[p.row][index] = 'O';
        }
        tmp = points;
        points = points2;
        points2 = tmp;
        points2.clear();

        points.sort((p1, p2) -> p2.row - p1.row);
        for (var p : points) {
            var index = findEmptySouth(grid, p.row, p.col);
            points2.add(new Point(index, p.col));
            grid[p.row][p.col] = '.';
            grid[index][p.col] = 'O';
        }
        tmp = points;
        points = points2;
        points2 = tmp;
        points2.clear();

        points.sort((p1, p2) -> p2.col - p1.col);
        for (var p : points) {
            var index = findEmptyEast(grid, p.row, p.col);
            points2.add(new Point(p.row, index));
            grid[p.row][p.col] = '.';
            grid[p.row][index] = 'O';
        }
        tmp = points;
        points = points2;
        points2 = tmp;
        points2.clear();
    }

    private static int findEmptyNorth(char[][] grid, int row, int col) {
        int index = row;
        while (index > 0 && grid[index - 1][col] == '.') index--;
        return index;
    }

    private static int findEmptyEast(char[][] grid, int row, int col) {
        int index = col;
        while (index < grid[row].length - 1 && grid[row][index + 1] == '.') index++;
        return index;
    }

    private static int findEmptySouth(char[][] grid, int row, int col) {
        int index = row;
        while (index < grid.length - 1 && grid[index + 1][col] == '.') index++;
        return index;
    }

    private static int findEmptyWest(char[][] grid, int row, int col) {
        int index = col;
        while (index > 0 && grid[row][index - 1] == '.') index--;
        return index;
    }

    private static void printGrid(char[][] grid) {
        for (var row : grid) {
            for (char c : row) System.out.print(c);
            System.out.println();
        }
    }
}