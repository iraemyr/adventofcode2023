package net.ddns.spellbank.day13;

import net.ddns.spellbank.utils.InputFile;

import java.util.Arrays;

public class Day13 {

    private record SliceBounds(int start, int end) {}

    public static void main(String[] args) {
        String file = "day13/input1";
        String[] lines = InputFile.getLines(file);

        System.out.println(reflectionSum(lines, 0)); // 32035
        System.out.println(reflectionSum(lines, 1)); // 24847
    }

    public static long reflectionSum(String[] lines, int smudges) {
        long sum = 0;
        int start = 0;
        int end = 1;
        while (start < lines.length) {
            if (end == lines.length || lines[end].trim().isEmpty()) {
                var slice = new SliceBounds(start, end);
                var result = hMirrorIndex(lines, slice, smudges);
                if (result != 0) sum += 100L * result;
                else sum += vMirrorIndex(lines, slice, smudges);
                start = end + 1;
                end = start + 1;
            }
            else end++;
        }
        return sum;
    }

    private static int hMirrorIndex(String[] lines, SliceBounds slice, int smudges) {
        boolean found;
        for (int i = 1; i < slice.end - slice.start; i++) {
            int row = slice.start + i;
            found = true;
            int differences = 0;
            for (int j = 0; j + row < slice.end && row - j - 1 >= slice.start && found; j++) {
                for (int k = 0; k < lines[row + j].length(); k++) {
                    if (lines[row + j].charAt(k) != lines[row - j - 1].charAt(k)) {
                        if (++differences > smudges) {
                            found = false;
                            break;
                        }
                    }
                }
            }
            if (found && differences == smudges) return i;
        }
        return 0;
    }

    private static int vMirrorIndex(String[] lines, SliceBounds slice, int smudges) {
        boolean found;
        for (int i = 1; i < lines[slice.start].length(); i++) {
            found = true;
            int differences = 0;
            for (int j = 0; j  + i < lines[slice.start].length() && i - j - 1 >= 0; j++) {
                differences += columnCompare(lines, i + j, i - j - 1, slice, smudges - differences);
                if (differences > smudges) {
                    found = false;
                    break;
                }
            }
            if (found && differences == smudges) return i;
        }
        return 0;
    }

    private static int columnCompare(String[] lines, int col1, int col2, SliceBounds slice, int diffLimit) {
        int count = 0;
        for (int row = slice.start; row < slice.end; row++) {
            var line = lines[row];
            if (line.charAt(col1) != line.charAt(col2)) count++;
            if (count > diffLimit) break;
        }
        return count;
    }
}