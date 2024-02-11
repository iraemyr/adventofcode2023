package net.ddns.spellbank.day15;

import net.ddns.spellbank.utils.InputFile;

import java.util.ArrayList;
import java.util.List;

public class Day15 {

    public record Entry(String key, int value) {}

    public static void main(String[] args) {
        String file = "day15/input1";
        String[] lines = InputFile.getLines(file);

        System.out.println(part1(lines)); //514394
        System.out.println(part2(lines)); //236358
    }

    public static long part1(String[] lines) {
        long sum = 0;
        for (var field : lines[0].split(",")) sum += hash(field);
        return sum;
    }

    public static long part2(String[] lines) {
        long sum = 0;
        List<Entry>[] map = new ArrayList[256];
        for (int i = 0; i < 256; i++) map[i] = new ArrayList<Entry>();
        for (var input : lines[0].split(",")) {
            var fields = input.split("=");
            if (fields.length == 2) {
                var key = fields[0];
                var val = Integer.parseInt(fields[1]);
                addOrReplace(map, key, val);
            } else {
                remove(map, fields[0].substring(0, fields[0].length() - 1));
            }
        }

        for (int i = 0; i < map.length; i++) {
            var bucket = map[i];
            for (int j = 0; j < bucket.size(); j++)
                sum += (long) (i + 1) * (j + 1) * bucket.get(j).value;
        }
        return sum;
    }

    public static int hash(String s) {
        int sum = 0;
        for (char c : s.toCharArray()) {
            sum += c;
            sum *= 17;
            sum %= 256;
        }
        return sum;
    }

    private static void addOrReplace(List<Entry>[] map, String key, int val) {
        var bucket = map[hash(key)];
        int index = -1;
        for (int i = 0; i < bucket.size() && index == -1; i++)
            if (bucket.get(i).key.equals(key)) index = i;
        if (index != -1) {
            bucket.remove(index);
            bucket.add(index, new Entry(key, val));
        } else bucket.add(new Entry(key, val));
    }

    private static void remove(List<Entry>[] map, String key) {
        var bucket = map[hash(key)];
        int index = -1;
        for (int i = 0; i < bucket.size() && index == -1; i++)
            if (bucket.get(i).key.equals(key)) index = i;
        if (index != -1) bucket.remove(index);
    }
}