package net.ddns.spellbank.day06;

import net.ddns.spellbank.harness.ExpectedAnswer;
import net.ddns.spellbank.harness.Solution;

public class Day06 implements Solution {

    @Override
    @ExpectedAnswer(4568778L)
    @ExpectedAnswer(value = 288L, input = "test1")
    public long solvePart1(String[] lines) {
        return part1(lines);
    }

    @Override
    @ExpectedAnswer(28973936L)
    @ExpectedAnswer(value = 71503L, input = "test1")
    public long solvePart2(String[] lines) {
        return part2(lines);
    }

    public static long part1(String[] lines) {
        var data = parse(lines);
        long product = 1;
        for (var array : data) {
            long count = 0;
            var time = array[0];
            var dist = array[1];
            for (int i = 1; i < time; i++)  {
                if (i * time - i * i > dist) count++;
            }
            product *= count;
        }
        return product;
    }

    public static long part2(String[] lines) {
        var data = parse2(lines);
        var time = data[0];
        var dist = data[1];
        long count = 0;
        for (long i = 1; i < time; i++)  {
            if (i * time - i * i > dist) count++;
        }
        return count;
    }

    public static int[][] parse(String[] lines) {
        var timeFields = lines[0].trim().split("\\s+");
        var distFields = lines[1].trim().split("\\s+");
        var arr = new int[timeFields.length - 1][2];
        for (int i = 1; i < timeFields.length; i++) {
            arr[i - 1][0] = Integer.parseInt(timeFields[i]);
            arr[i - 1][1] = Integer.parseInt(distFields[i]);
        }
        return arr;
    }

    public static long[] parse2(String[] lines) {
        var result = new long[2];
        long val = 0;
        for (char c : lines[0].toCharArray())
            if (Character.isDigit(c))
                val = 10 * val + (c - '0');
        result[0] = val;
        val = 0;
        for (char c : lines[1].toCharArray())
            if (Character.isDigit(c))
                val = 10 * val + (c - '0');
        result[1] = val;
        return result;
    }

}