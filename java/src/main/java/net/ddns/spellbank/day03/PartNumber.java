package net.ddns.spellbank.day03;

import java.util.Map;

public class PartNumber {
    private final int colFirst;
    private final int colLast;
    private final int row;

    private final int val;

    private Boolean adjacent;

    public PartNumber(int colFirst, int colLast, int row, int val) {
        this.colFirst = colFirst;
        this.colLast = colLast;
        this.row = row;
        this.val = val;
        this.adjacent = null;
    }

    public int getVal() {
        return val;
    }
    public int getColFirst() {
        return colFirst;
    }

    public int getColLast() {
        return colLast;
    }

    public int getRow() {
        return row;
    }

    public boolean isAdjacent(Map<Day03.Coord, Character> map) {
        if (adjacent != null) return adjacent;
        for (int r = row - 1; r <= row + 1; r++) {
            if (r == row) {
                if (map.containsKey(new Day03.Coord(r,colFirst - 1)) || map.containsKey(new Day03.Coord(r, colLast + 1))) {
                    adjacent = true;
                    return true;
                }
            } else {
                for (int c = colFirst - 1; c <= colLast + 1; c++) {
                    if (map.containsKey(new Day03.Coord(r, c))) {
                        adjacent = true;
                        return true;
                    }
                }
            }
        }
        adjacent = false;
        return false;
    }

    public void print() {
        System.out.println(val + " " + colFirst + "-" + colLast + " " + row);
    }
}
