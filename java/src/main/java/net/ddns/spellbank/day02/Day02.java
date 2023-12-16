package net.ddns.spellbank.day02;

import net.ddns.spellbank.utils.InputFile;

public class Day02 {

    public static void main(String[] args) {
        String file = "day02/input1";
        String[] lines = InputFile.getLines(file);

        System.out.println(part1(lines)); // 2913
        System.out.println(part2(lines)); // 55593
    }

    public static long part1(String[] lines) {
        int sum = 0;
        final int REDMAX = 12;
        final int GREENMAX = 13;
        final int BLUEMAX = 14;

        for (var line : lines) {
            var fields = line.trim().split(": ");
            var gameFields = fields[0].split(" ");
            var id = Integer.parseInt(gameFields[1]);
            var draws = fields[1].split("; ");
            boolean possible = true;
            for (var draw : draws) {
                var colors = draw.split(", ");
                for (var colorFields : colors) {
                    var color = colorFields.split(" ");
                    switch (color[1].trim()) {
                        case "red":
                            if (Integer.parseInt(color[0]) > REDMAX) possible = false;
                            break;
                        case "green":
                            if (Integer.parseInt(color[0]) > GREENMAX) possible = false;
                            break;
                        case "blue":
                            if (Integer.parseInt(color[0]) > BLUEMAX) possible = false;
                            break;
                        default:
                            System.out.println("Invalid color");
                            break;
                    }
                }
            }
            //if (possible) System.out.println("Valid: " + id);
            if (possible) sum += id;
        }
        return sum;
    }

    public static long part2(String[] lines) {
        int sum = 0;

        for (var line : lines) {
            int red = 0;
            int green = 0;
            int blue = 0;
            var fields = line.trim().split(": ");
            var gameFields = fields[0].split(" ");
            var id = Integer.parseInt(gameFields[1]);
            var draws = fields[1].split("; ");
            for (var draw : draws) {
                var colors = draw.split(", ");
                for (var colorFields : colors) {
                    var color = colorFields.split(" ");
                    switch (color[1].trim()) {
                        case "red":
                            red = Math.max(Integer.parseInt(color[0]), red);
                            break;
                        case "green":
                            green = Math.max(Integer.parseInt(color[0]), green);
                            break;
                        case "blue":
                            blue = Math.max(Integer.parseInt(color[0]), blue);
                            break;
                        default:
                            System.out.println("Invalid color");
                            break;
                    }
                }
            }
            sum += red * green * blue;
        }
        return sum;
    }

}