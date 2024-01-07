package net.ddns.spellbank.day07;

import net.ddns.spellbank.utils.InputFile;

import java.util.ArrayList;
import java.util.Collections;

public class Day07 {

    public static void main(String[] args) {
        String file = "day07/input1";
        String[] lines = InputFile.getLines(file);

        System.out.println(solution(lines, false)); // 249483956
        System.out.println(solution(lines, true)); // 252137472
    }

    public static long solution(String[] lines, boolean part2) {
        var hands = new ArrayList<Hand>();
        for (var line : lines) {
            var fields = line.trim().split(" ");
            hands.add(new Hand(fields[0], Long.parseLong(fields[1]), part2));
        }
        Collections.sort(hands);
        long sum = 0;
        for (int i = 0; i < hands.size(); i++) sum += (i + 1) * hands.get(i).getBid();
        return sum;
    }
}