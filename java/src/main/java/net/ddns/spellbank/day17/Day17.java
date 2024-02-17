package net.ddns.spellbank.day17;

import net.ddns.spellbank.utils.InputFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class Day17 {

    public static void main(String[] args) {
        String file = "day17/input1";
        String[] lines = InputFile.getLines(file);
        var grid = getGrid(lines);

        System.out.println(part1(grid)); //866
        System.out.println(part2(grid)); //1010
    }

    public static long part1(int[][] grid) {
        return dijkstra(grid, false);
    }

    public static long part2(int[][] grid) {
        return dijkstra(grid, true);
    }

    private enum Direction {
        NORTH, EAST, SOUTH, WEST;
    }

    private record HashState(int row, int col, Direction d, int consecutive) {}

    private record State(int row, int col, Direction d, int consecutive, long loss) {
        List<State> neighbors(int[][] grid) {
            var n = new ArrayList<State>();
            switch (d) {
                case NORTH:
                    if (consecutive < 3 && row > 0) {
                        n.add(new State(row - 1, col, d, consecutive + 1, loss + grid[row - 1][col]));
                    }
                    if (col > 0) {
                        n.add(new State(row, col - 1, Direction.WEST, 1, loss + grid[row][col - 1]));
                    }
                    if (col < grid[row].length - 1) {
                        n.add(new State(row, col + 1, Direction.EAST, 1, loss + grid[row][col + 1]));
                    }
                    break;
                case EAST:
                    if (consecutive < 3 && col < grid[row].length - 1) {
                        n.add(new State(row, col + 1, d, consecutive + 1, loss + grid[row][col + 1]));
                    }
                    if (row > 0) {
                        n.add(new State(row - 1, col, Direction.NORTH, 1, loss + grid[row - 1][col]));
                    }
                    if (row < grid.length - 1) {
                        n.add(new State(row + 1, col, Direction.SOUTH, 1, loss + grid[row + 1][col]));
                    }
                    break;
                case SOUTH:
                    if (consecutive < 3 && row < grid.length - 1) {
                        n.add(new State(row + 1, col, d, consecutive + 1, loss + grid[row + 1][col]));
                    }
                    if (col > 0) {
                        n.add(new State(row, col - 1, Direction.WEST, 1, loss + grid[row][col - 1]));
                    }
                    if (col < grid[row].length - 1) {
                        n.add(new State(row, col + 1, Direction.EAST, 1, loss + grid[row][col + 1]));
                    }
                    break;
                case WEST:
                    if (consecutive < 3 && col > 0) {
                        n.add(new State(row, col - 1, d, consecutive + 1, loss + grid[row][col - 1]));
                    }
                    if (row > 0) {
                        n.add(new State(row - 1, col, Direction.NORTH, 1, loss + grid[row - 1][col]));
                    }
                    if (row < grid.length - 1) {
                        n.add(new State(row + 1, col, Direction.SOUTH, 1, loss + grid[row + 1][col]));
                    }
                    break;

            }
            return n;
        }

        List<State> neighborsUltra(int[][] grid) {
            var n = new ArrayList<State>();
            switch (d) {
                case NORTH:
                    if (consecutive < 10 && row > 0) {
                        n.add(new State(row - 1, col, d, consecutive + 1, loss + grid[row - 1][col]));
                    }
                    if (col - 4 >= 0) {
                        n.add(new State(row, col - 4, Direction.WEST, 4,
                                loss + grid[row][col - 1] + grid[row][col -2] + grid[row][col -3] + grid[row][col - 4]));
                    }
                    if (col + 4 <= grid[row].length - 1) {
                        n.add(new State(row, col + 4, Direction.EAST, 4,
                                loss + grid[row][col + 1] + grid[row][col + 2] + grid[row][col + 3] + grid[row][col + 4]));
                    }
                    break;
                case EAST:
                    if (consecutive < 10 && col < grid[row].length - 1) {
                        n.add(new State(row, col + 1, d, consecutive + 1, loss + grid[row][col + 1]));
                    }
                    if (row  - 4 >= 0) {
                        n.add(new State(row - 4, col, Direction.NORTH, 4,
                                loss + grid[row - 1][col]  + grid[row - 2][col]  + grid[row - 3][col] + grid[row - 4][col]));
                    }
                    if (row  + 4 <= grid.length - 1) {
                        n.add(new State(row + 4, col, Direction.SOUTH, 4,
                                loss + grid[row + 1][col] + grid[row + 2][col] + grid[row + 3][col] + grid[row + 4][col]));
                    }
                    break;
                case SOUTH:
                    if (consecutive < 10 && row < grid.length - 1) {
                        n.add(new State(row + 1, col, d, consecutive + 1, loss + grid[row + 1][col]));
                    }
                    if (col - 4 >= 0) {
                        n.add(new State(row, col - 4, Direction.WEST, 4,
                                loss + grid[row][col - 1] + grid[row][col - 2] + grid[row][col - 3] + grid[row][col - 4]));
                    }
                    if (col + 4 <= grid[row].length - 1) {
                        n.add(new State(row, col + 4, Direction.EAST, 4,
                                loss + grid[row][col + 1] + grid[row][col + 2] + grid[row][col + 3] + grid[row][col + 4]));
                    }
                    break;
                case WEST:
                    if (consecutive < 10 && col > 0) {
                        n.add(new State(row, col - 1, d, consecutive + 1, loss + grid[row][col - 1]));
                    }
                    if (row - 4 >= 0) {
                        n.add(new State(row - 4, col, Direction.NORTH, 4,
                                loss + grid[row - 1][col] + grid[row - 2][col] + grid[row - 3][col] + grid[row - 4][col]));
                    }
                    if (row + 4 <= grid.length - 1) {
                        n.add(new State(row + 4, col, Direction.SOUTH, 4,
                                loss + grid[row + 1][col] + grid[row + 2][col] + grid[row + 3][col] + grid[row + 4][col]));
                    }
                    break;
            }
            return n;
        }

        HashState getHash() {
            return new HashState(row, col, d, consecutive);
        }
    }

    public static int[][] getGrid(String[] lines) {
        var grid = new int[lines.length][lines[0].length()];
        for (int i = 0; i < lines.length; i++)
            for (int j = 0; j < lines[i].length(); j++)
                grid[i][j] = lines[i].charAt(j) - '0';
        return grid;
    }

    private static long dijkstra(int[][] grid, boolean ultra) {
        var q = new PriorityQueue<State>((s1, s2) -> (int) (s1.loss - s2.loss));
        var visited = new HashSet<HashState>();
        if (ultra) {
            q.add(new State(0, 4, Direction.EAST, 4,
                    grid[0][1] + grid[0][2] + grid[0][3] + grid[0][4]));
            q.add(new State(4, 0, Direction.SOUTH, 4,
                    grid[1][0] + grid[2][0] + grid[3][0] + grid[4][0]));
        } else {
            q.add(new State(0, 0, Direction.EAST, 0, 0));
            q.add(new State(0, 0, Direction.SOUTH, 0, 0));
        }
        long loss = 0;
        while (!q.isEmpty()) {
            var s = q.remove();
            if (visited.contains(s.getHash())) continue;
            if (s.row == grid.length - 1 && s.col == grid[0].length - 1 && (!ultra || s.consecutive > 3)) {
                loss = s.loss;
                break;
            }
            visited.add(s.getHash());
            var neighbors = ultra ? s.neighborsUltra(grid) : s.neighbors(grid);
            for (var n : neighbors) if (!visited.contains(n.getHash())) q.add(n);
        }
        return loss;
    }
}