package net.ddns.spellbank.harness;

public interface Solution {
    long solvePart1(String[] lines);

    long solvePart2(String[] lines);

    default long solve(int part, String[] lines) {
        return switch (part) {
            case 1 -> solvePart1(lines);
            case 2 -> solvePart2(lines);
            default -> throw new IllegalArgumentException("Part must be 1 or 2.");
        };
    }
}
