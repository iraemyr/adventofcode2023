package net.ddns.spellbank.day16;

import net.ddns.spellbank.utils.InputFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day16 {
    private static final int NORTH = 1;
    private static final int EAST = 2;
    private static final int SOUTH = 4;
    private static final int WEST = 8;
    public record Beam(int row, int col, int dir) {}

    public static void main(String[] args) {
        String file = "day16/input1";
        String[] lines = InputFile.getLines(file);
        var grid = getGrid(lines);

        System.out.println(part1(grid)); //7870
        System.out.println(part2(grid)); //8143
    }

    public static long part1(char[][] grid) {
        var beamMap = new int[grid.length][grid[0].length];
        var beams = new ArrayList<Beam>();
        var beams2 = new ArrayList<Beam>();
        beams.add(new Beam(0, -1, EAST));
        simBeam(grid, beamMap, beams, beams2);
        return energized(beamMap);
    }

    public static long part2(char[][] grid) {
        long max = 0;
        var beams = new ArrayList<Beam>();
        var beams2 = new ArrayList<Beam>();
        int[][] beamMap = new int[grid.length][grid[0].length];
        for (int col = 0; col < grid[0].length; col++) {
            clearEnergized(beamMap);
            beams.add(new Beam(-1, col, SOUTH));
            simBeam(grid, beamMap, beams, beams2);
            max = Math.max(max, energized(beamMap));
            clearEnergized(beamMap);
            beams.add(new Beam(grid.length, col, NORTH));
            simBeam(grid, beamMap, beams, beams2);
            max = Math.max(max, energized(beamMap));
        }

        for (int row = 0; row < grid.length; row++) {
            clearEnergized(beamMap);
            beams.add(new Beam(row, -1, EAST));
            simBeam(grid, beamMap, beams, beams2);
            max = Math.max(max, energized(beamMap));
            clearEnergized(beamMap);
            beams.add(new Beam(row, grid[0].length, WEST));
            simBeam(grid, beamMap, beams, beams2);
            max = Math.max(max, energized(beamMap));
        }
        return max;
    }

    private static void simBeam(char[][] grid, int[][] beamMap, List<Beam> beams, List<Beam> beams2) {
        while (!beams.isEmpty()) {
            for (var beam : beams) {
                stepBeam(grid, beamMap, beams2, beam);
            }
            var tmp = beams;
            beams = beams2;
            beams2 = tmp;
            beams2.clear();
        }
    }
    
    private static void stepBeam(char[][] grid, int[][]beamMap, List<Beam> beams, Beam beam) {
        int row = beam.row;
        int col = beam.col;
        switch (beam.dir) {
            case NORTH:
                row--;
                if (row >= 0 && (beamMap[row][col] & NORTH) == 0) {
                    char c = grid[row][col];
                    if (c == '\\') beams.add(new Beam(row, col, WEST));
                    else if (c == '/') beams.add(new Beam(row, col, EAST));
                    else if (c == '-') {
                        beams.add(new Beam(row, col, EAST));
                        beams.add(new Beam(row, col, WEST));
                    }
                    else beams.add(new Beam(row, col, NORTH));
                    beamMap[row][col] |= NORTH;
                }
                break;
            case EAST:
                col++;
                if (col < grid[0].length && (beamMap[row][col] & EAST) == 0) {
                    char c = grid[row][col];
                    if (c == '\\') beams.add(new Beam(row, col, SOUTH));
                    else if (c == '/') beams.add(new Beam(row, col, NORTH));
                    else if (c == '|') {
                        beams.add(new Beam(row, col, NORTH));
                        beams.add(new Beam(row, col, SOUTH));
                    } else beams.add(new Beam(row, col, EAST));
                    beamMap[row][col] |= EAST;
                }
                break;
            case SOUTH:
                row++;
                if (row < grid.length && (beamMap[row][col] & SOUTH) == 0) {
                    char c = grid[row][col];
                    if (c == '\\') beams.add(new Beam(row, col, EAST));
                    else if (c == '/') beams.add(new Beam(row, col, WEST));
                    else if (c == '-') {
                        beams.add(new Beam(row, col, EAST));
                        beams.add(new Beam(row, col, WEST));
                    } else beams.add(new Beam(row, col, SOUTH));
                    beamMap[row][col] |= SOUTH;
                }
                break;
            case WEST:
                col--;
                if (col >= 0 && (beamMap[row][col] & WEST) == 0) {
                    char c = grid[row][col];
                    if (c == '\\') beams.add(new Beam(row, col, NORTH));
                    else if (c == '/') beams.add(new Beam(row, col, SOUTH));
                    else if (c == '|') {
                        beams.add(new Beam(row, col, NORTH));
                        beams.add(new Beam(row, col, SOUTH));
                    } else beams.add(new Beam(row, col, WEST));
                    beamMap[row][col] |= WEST;
                }
                break;
            default:
                throw new IllegalStateException("Unknown direction");
        }
    }

    private static long energized(int[][] beamMap) {
        long energized = 0;
        for (var row : beamMap)
            for (var tile : row) if (tile != 0) energized++;
        return energized;
    }

    public static char[][] getGrid(String[] lines) {
        var grid = new char[lines.length][lines[0].length()];
        for (int i = 0; i < lines.length; i++)
            for (int j = 0; j < lines[0].length(); j++)
                grid[i][j] = lines[i].charAt(j);
        return grid;
    }

    private static void clearEnergized(int[][] grid) {
        for (var row : grid) Arrays.fill(row, 0);
    }

    @SuppressWarnings("unused")
    public static void printEnergy(int[][] grid) {
        for (var row : grid) {
            for (var i : row) {
                System.out.print(i == 0 ? '.' : '#');
            }
            System.out.println();
        }
    }
}