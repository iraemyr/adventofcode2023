package net.ddns.spellbank.day18;

import net.ddns.spellbank.harness.ExpectedAnswer;
import net.ddns.spellbank.harness.Solution;

import java.util.ArrayList;
import java.util.List;

public class Day18 implements Solution {

    private record Point(long row, long col) {}
    public record Instruction(char direction, int dist, String color) {}

    @Override
    @ExpectedAnswer("62573")
    @ExpectedAnswer(value = "62", input = "test1")
    public long solvePart1(String[] lines) {
        return part1(getInstructions(lines));
    }

    @Override
    @ExpectedAnswer("54662804037719")
    @ExpectedAnswer(value = "952408144115", input = "test1")
    public long solvePart2(String[] lines) {
        return part2(getInstructions(lines));
    }

    public static long part1(List<Instruction> instructions) {
        return shoeString(getPoints(instructions, false));
    }

    public static long part2(List<Instruction> instructions) {
        return shoeString(getPoints(instructions, true));
    }

    private static List<Point> getPoints(List<Instruction> instructions, boolean color) {
        var points = new ArrayList<Point>();
        long row = 0, col = 0;
        points.add(new Point(row, col));
        for (var i : instructions) {
            if (color) {
                var dist = Long.parseLong(i.color.substring(1, i.color.length() - 1), 16);
                switch (i.color.charAt(i.color.length() - 1)) {
                    case '0':
                        col += dist;
                        break;
                    case '1':
                        row += dist;
                        break;
                    case '2':
                        col -= dist;
                        break;
                    case '3':
                        row -= dist;
                        break;
                    default:
                        throw new IllegalStateException("Invalid direction");
                }
            } else {
                switch (i.direction) {
                    case 'U':
                        row -= i.dist;
                        break;
                    case 'D':
                        row += i.dist;
                        break;
                    case 'L':
                        col -= i.dist;
                        break;
                    case 'R':
                        col += i.dist;
                        break;
                    default:
                        throw new IllegalStateException("Invalid direction");
                }
            }
            points.add(new Point(row, col));
        }
        return points;
    }

    public static List<Instruction> getInstructions(String[] lines) {
        var instructions = new ArrayList<Instruction>();
        for (var line : lines) {
            var fields = line.split(" ");
            instructions.add(new Instruction(fields[0].charAt(0),
                    Integer.parseInt(fields[1]),
                    fields[2].substring(1, fields[2].length() - 1)));
        }
        return instructions;
    }

    private static long shoeString(List<Point> points) {
        long sum1 = 0, sum2 = 0, perimeter = 0;
        for (int i = 0; i < points.size() - 1; i++) {
            var p1 = points.get(i);
            var p2 = points.get(i + 1);
            sum1 += p1.col * p2.row;
            sum2 += p1.row * p2.col;
            perimeter += Math.abs(p1.row - p2.row) + Math.abs(p1.col - p2.col);
        }
        // Adjust for border squares being cut in half by the vectors
        return (sum1 - sum2) / 2 + perimeter / 2 + 1;
    }
}