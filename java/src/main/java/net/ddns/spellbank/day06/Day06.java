package net.ddns.spellbank.day06;

import net.ddns.spellbank.utils.InputFile;

public class Day06 {

    public static void main(String[] args) {
        String file = "day06/input1";
        String[] lines = InputFile.getLines(file);

        System.out.println(part1(lines)); // 4568778
        System.out.println(part2(lines)); // 28973936
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