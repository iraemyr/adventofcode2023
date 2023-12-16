package net.ddns.spellbank.day01;

import net.ddns.spellbank.utils.InputFile;

public class Day01 {

    public static void main(String[] args) {
        String file = "day01/input1";
        String[] lines = InputFile.getLines(file);

        System.out.println(part1(lines)); // 54644
        System.out.println(part2(lines)); // 53348
    }

    public static long part1(String[] lines) {
        int sum = 0;
        for (var line : lines) {
            var chars = line.toCharArray();
            int first = -1;
            for (int i = 0; i < chars.length && first == -1; i++)
                first = getDigit(chars[i]);
            int second = -1;
            for (int i = chars.length - 1; i >= 0 && second == -1; i--)
                second = getDigit(chars[i]);
            sum += 10 * first + second;
        }
        return sum;
    }

    public static long part2(String[] lines) {
        int sum = 0;

        for (var line : lines) {
            sum += getFirstNumber(line) * 10 + getSecondNumber(line.toCharArray());
        }

        return sum;
    }

    private static int getDigit(char c) {
        return Character.isDigit(c) ? c - '0' : -1;
    }

    private static int getFirstNumber(String input) {

        for (int i = 0; i < input.length(); i++) {
            var s = input.substring(i);
            char c = s.charAt(0);
            if (Character.isDigit(c)) {
                return c - '0';
            }

            if (s.startsWith("one")) return 1;
            if (s.startsWith("two")) return 2;
            if (s.startsWith("three")) return 3;
            if (s.startsWith("four")) return 4;
            if (s.startsWith("five")) return 5;
            if (s.startsWith("six")) return 6;
            if (s.startsWith("seven")) return 7;
            if (s.startsWith("eight")) return 8;
            if (s.startsWith("nine")) return 9;
        }
        return 0;
    }

    private static int getSecondNumber(char[] input) {
        for (int i = input.length - 1; i >= 0; i--) {
            if (Character.isDigit(input[i])) return input[i] - '0';
            //2
            if (input[i] == 'o' && i > 1 && input[i - 1] == 'w' && input[i - 2] == 't') return 2;
            //4
            if (input[i] == 'r' && i > 1 && input[i - 1] == 'u' && input[i - 2] == 'o' && input[i - 3] == 'f') return 4;
            //6
            if (input[i] == 'x' && i > 1 && input[i - 1] == 'i' && input[i - 2] == 's') return 6;
            //7
            if (input[i] == 'n' && i > 3 && input[i - 1] == 'e' && input[i - 2] == 'v' && input[i - 3] == 'e' && input[i - 4] == 's') return 7;
            //8
            if (input[i] == 't' && i > 3 && input[i - 1] == 'h' && input[i - 2] == 'g' && input[i - 3] == 'i' && input[i - 4] == 'e') return 8;
            //1,3,5,9
            if (input[i] == 'e') {
                if (i > 1 && input[i - 1] == 'n' && input[i - 2] == 'o') return 1;
                if (i > 3 && input[i - 1] == 'e' && input[i - 2] == 'r' && input[i - 3] == 'h' && input[i - 4] == 't') return 3;
                if (i > 2 && input[i - 1] == 'v' && input[i - 2] == 'i' && input[i - 3] == 'f') return 5;
                if (i > 2 && input[i - 1] == 'n' && input[i - 2] == 'i' && input[i - 3] == 'n') return 9;
            }
        }
        return 0;
    }
}