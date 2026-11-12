package net.ddns.spellbank.day07;

import net.ddns.spellbank.harness.Solution;

import java.util.ArrayList;
import java.util.Collections;

public class Day07 implements Solution {

    @Override
    public long solvePart1(String[] lines) {
        return solution(lines, false);
    }

    @Override
    public long solvePart2(String[] lines) {
        return solution(lines, true);
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