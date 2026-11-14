package net.ddns.spellbank.harness;

/** Implement exactly one numeric or string answer method, taking String[] or String, for each part. */
public interface Solution {
    default long solvePart1(String input) {
        throw new UnsupportedOperationException("Part 1 does not implement a numeric answer for string input.");
    }

    default long solvePart2(String input) {
        throw new UnsupportedOperationException("Part 2 does not implement a numeric answer for string input.");
    }

    default String solvePart1Str(String input) {
        return Long.toString(solvePart1(input));
    }

    default String solvePart2Str(String input) {
        return Long.toString(solvePart2(input));
    }

    default long solvePart1(String[] lines) {
        throw new UnsupportedOperationException("Part 1 does not implement a numeric answer.");
    }

    default long solvePart2(String[] lines) {
        throw new UnsupportedOperationException("Part 2 does not implement a numeric answer.");
    }

    default String solvePart1Str(String[] lines) {
        return Long.toString(solvePart1(lines));
    }

    default String solvePart2Str(String[] lines) {
        return Long.toString(solvePart2(lines));
    }

    default String solve(int part, String input) {
        return switch (part) {
            case 1 -> solvePart1Str(input);
            case 2 -> solvePart2Str(input);
            default -> throw new IllegalArgumentException("Part must be 1 or 2.");
        };
    }

    default long solveNumeric(int part, String input) {
        return switch (part) {
            case 1 -> solvePart1(input);
            case 2 -> solvePart2(input);
            default -> throw new IllegalArgumentException("Part must be 1 or 2.");
        };
    }

    default String solve(int part, String[] lines) {
        return switch (part) {
            case 1 -> solvePart1Str(lines);
            case 2 -> solvePart2Str(lines);
            default -> throw new IllegalArgumentException("Part must be 1 or 2.");
        };
    }

    default long solveNumeric(int part, String[] lines) {
        return switch (part) {
            case 1 -> solvePart1(lines);
            case 2 -> solvePart2(lines);
            default -> throw new IllegalArgumentException("Part must be 1 or 2.");
        };
    }
}
