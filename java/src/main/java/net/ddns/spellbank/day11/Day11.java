package net.ddns.spellbank.day11;

import net.ddns.spellbank.utils.InputFile;

import java.util.ArrayList;
import java.util.List;

public class Day11 {

    public record Point(int row, int col) {}
    public record ParseResult(List<Point> points, int[] emptyRows, int[] emptyCols) {}

    public static void main(String[] args) {
        String file = "day11/input1";
        String[] lines = InputFile.getLines(file);

        var r = parse(lines);

        System.out.println(part1(r, 2)); // 9591768
        System.out.println(part1(r,1_000_000)); // 746962097860
    }

    public static long part1(ParseResult r, long mult) {
        long sum = 0;
        var points = r.points;
        var emptyRows = r.emptyRows;
        var emptyCols = r.emptyCols;

        for (int i = 0; i < points.size() - 1; i++) {
            var p1 = points.get(i);
            for (int j = i + 1; j < points.size(); j++) {
                var p2 = points.get(j);
                var manhattan = Math.abs(p2.row - p1.row) + Math.abs(p2.col - p1.col);
                var empty = Math.abs(emptyRows[p1.row] - emptyRows[p2.row]);
                empty += Math.abs(emptyCols[p1.col] - emptyCols[p2.col]);
                sum += manhattan + empty * (mult - 1);
            }
        }
        return sum;
    }

    public static ParseResult parse(String[] lines) {
        var points = new ArrayList<Point>();
        var emptyRows = new int[lines.length];
        var emptyCols = new int[lines[0].length()];
        int sumEmptyRows = 0;
        int sumEmptyCols = 0;
        for (int row = 0; row < lines.length; row++) {
            boolean emptyRow = true;
            for (int col = 0; col < lines[row].length(); col++) {
                var c = lines[row].charAt(col);
                if (c != '.') {
                    points.add(new Point(row, col));
                    emptyRow = false;
                }
            }
            if (emptyRow) sumEmptyRows++;
            emptyRows[row] = sumEmptyRows;
        }

        for (int col = 0; col < lines[0].length(); col++) {
            boolean emptyCol = true;
            for (var row : lines) {
                if (row.charAt(col) != '.') {
                    emptyCol = false;
                    break;
                }
            }
            if (emptyCol) sumEmptyCols++;
            emptyCols[col] = sumEmptyCols;
        }
        return new ParseResult(points, emptyRows, emptyCols);
    }
}