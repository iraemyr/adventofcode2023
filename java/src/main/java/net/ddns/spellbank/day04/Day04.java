package net.ddns.spellbank.day04;

import net.ddns.spellbank.utils.InputFile;

import java.util.Arrays;
import java.util.HashSet;

public class Day04 {

    public static void main(String[] args) {
        String file = "day04/input1";
        String[] lines = InputFile.getLines(file);

        System.out.println(part1(lines)); // 24706
        System.out.println(part2(lines)); // 13114317
    }

    public static long part1(String[] lines) {
        long sum = 0;
        var winners = new HashSet<Integer>();
        for (var line: lines) {
            winners.clear();
            var fields = line.split(": ");
            var cards = fields[1].split(" \\| ");
            var numbers = cards[0];
            var nums = numbers.split(" ");
            for (var n : nums) {
                n = n.trim();
                if (!n.isEmpty()) winners.add(Integer.parseInt(n));
            }
            numbers = cards[1];
            nums = numbers.split(" ");
            int matches = 0;
            for (var n : nums) {
                n = n.trim();
                if (!n.isEmpty() && winners.contains(Integer.parseInt(n))) matches++;
            }
            sum += matches > 0 ? (long) Math.pow(2, matches - 1) : 0;
        }
        return sum;
    }

    public static long part2(String[] lines) {
        long sum = 0;
        var winners = new HashSet<Integer>();
        long[] quantities = new long[lines.length];
        Arrays.fill(quantities, 1);
        for (int i = 0; i < lines.length; i++) {
            var line = lines[i];
            winners.clear();
            var fields = line.split(": ");
            var cards = fields[1].split(" \\| ");
            var numbers = cards[0];
            var nums = numbers.split(" ");
            for (var n : nums) {
                n = n.trim();
                if (!n.isEmpty()) winners.add(Integer.parseInt(n));
            }
            numbers = cards[1];
            nums = numbers.split(" ");
            int matches = 0;
            for (var n : nums) {
                n = n.trim();
                if (!n.isEmpty() && winners.contains(Integer.parseInt(n))) matches++;
            }
            sum += quantities[i];
            for (int j = 1; j <= matches; j++) {
                quantities[i + j] += quantities[i];
            }
        }
        return sum;
    }

}