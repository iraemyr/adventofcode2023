package net.ddns.spellbank.utils;

public class MyUtils {
    @SuppressWarnings("unused")
    public static void printMap(char[][] map) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++)
                System.out.print(map[row][col]);
            System.out.println();
        }
    }

    public static long euclideanMod(long a, long n) {
        return n < 0 ? euclideanMod(a, -n) : mod(a, n);
    }

    public static long mod(long a, long n) {
        return a < 0 ? (a % n + n) % n : a % n;
    }

    public static long gcd(long a, long b) {
        if (a == 0)
            return b;
        return gcd(b % a, a);
    }

    public static long lcm(long a, long b) {
        return (a / gcd(a, b)) * b;
    }
}
