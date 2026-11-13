package net.ddns.spellbank.day09;

import net.ddns.spellbank.harness.ExpectedAnswer;
import net.ddns.spellbank.harness.Solution;

import java.util.ArrayList;
import java.util.List;

public class Day09 implements Solution {

    @Override
    @ExpectedAnswer("1987402313")
    @ExpectedAnswer(value = "114", input = "test1")
    public long solvePart1(String[] lines) {
        return solve(parse(lines), true);
    }

    @Override
    @ExpectedAnswer("900")
    @ExpectedAnswer(value = "2", input = "test1")
    public long solvePart2(String[] lines) {
        return solve(parse(lines), false);
    }

    public static long solve(List<List<Long>> sequences, boolean part1) {
        long sum = 0;
        for (var sequence: sequences) {
            sum += getNext(sequence, part1);
        }
        return sum;
    }

    private static long getNext(List<Long> sequence, boolean future) {
        if (sequence.size() < 3) throw new IllegalArgumentException("Sequence not long enough");
        boolean done = true;
        var extrapolation = new ArrayList<Long>();
        long prev = sequence.get(1) - sequence.get(0);
        extrapolation.add(prev);
        for (int i = 2; i < sequence.size(); i++) {
            var next = sequence.get(i) - sequence.get(i - 1);
            if (next != prev) done = false;
            extrapolation.add(next);
        }
        if (done) return future ? sequence.getLast() + prev : sequence.getFirst() - prev;
        return future ? sequence.getLast() + getNext(extrapolation, true) :
                sequence.getFirst() - getNext(extrapolation, false);
    }

    public static List<List<Long>> parse(String[] lines) {
        var values = new ArrayList<List<Long>>();
        for (var line : lines) {
            var fields = line.trim().split(" ");
            var numbers = new ArrayList<Long>();
            for (var field : fields) numbers.add(Long.parseLong(field));
            values.add(numbers);
        }
        return values;
    }

}