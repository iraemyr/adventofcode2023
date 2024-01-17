package net.ddns.spellbank.day11;

import net.ddns.spellbank.utils.InputFile;

import java.util.ArrayList;

public class Day11 {

    public record Point(int row, int col) {}

    public static void main(String[] args) {
        String file = "day11/input1";
        String[] lines = InputFile.getLines(file);

        System.out.println(part1(lines, 2)); // 9591768
        System.out.println(part1(lines,1_000_000)); // 746962097860
    }

    public static long part1(String[] lines, long mult) {
        long sum = 0;
        var points = new ArrayList<Point>();
        var emptyRows = new int[lines.length];
        var emptyCols = new int[lines[0].length()];
        for (int row = 0; row < lines.length; row++) {
            boolean emptyRow = true;
            for (int col = 0; col < lines[row].length(); col++) {
                var c = lines[row].charAt(col);
                if (c != '.') {
                    points.add(new Point(row, col));
                    emptyRow = false;
                }
            }
            if (emptyRow) {
                if (row == 0) emptyRows[0]++;
                else emptyRows[row] = 1 + emptyRows[row - 1];
            } else {
                if (row > 0) emptyRows[row] = emptyRows[row - 1];
            }
        }

        for (int col = 0; col < lines[0].length(); col++) {
            boolean emptyCol = true;
            for (var row : lines) {
                if (row.charAt(col) != '.') {
                    emptyCol = false;
                    break;
                }
            }
            if (emptyCol) {
                if (col == 0) emptyCols[0]++;
                else emptyCols[col] = 1 + emptyCols[col - 1];
            } else if (col > 0) emptyCols[col] = emptyCols[col - 1];
        }

        for (int i = 0; i < points.size() - 1; i++) {
            var p1 = points.get(i);
            for (int j = i + 1; j < points.size(); j++) {
                var p2 = points.get(j);
                var manhattan = Math.abs(p2.row - p1.row) + Math.abs(p2.col - p1.col);
                var empty = emptyRows[Math.max(p1.row, p2.row)] -
                        emptyRows[Math.min(p1.row, p2.row)];
                empty += emptyCols[Math.max(p1.col, p2.col)] -
                        emptyCols[Math.min(p1.col, p2.col)];
                sum += manhattan + empty * (mult - 1);
            }
        }
        return sum;
    }
}