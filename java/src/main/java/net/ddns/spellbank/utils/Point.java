package net.ddns.spellbank.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Point {
	public int x;
	public int y;
	public char c;
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(int x, int y, char c) {
		this.x = x;
		this.y = y;
		this.c = c;
	}
	
	public String toString() {
		return x + ":" + y;
	}
	
	public static Point getPoint(String s) {
	    String[] fields = s.split(":");
	    return new Point(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]));
	}
	
	public boolean equals(Object obj) {
	    if (obj == null) return false;
	    if (this == obj) return true;
	    if (!(obj instanceof Point)) return false;
	    Point a = (Point) obj;
	    return this.x == a.x && this.y == a.y ? true : false;
	}
	
	public int hashCode() {
	    return Objects.hash(x, y);
	}
	
	public static int manhattan(Point p, Point dest) {
	    return Math.abs(p.x - dest.x) + Math.abs(p.y - dest.y);
	}
	
	public List<Point> getNeighbors() {
	    List<Point> neighbors = new ArrayList<>();
	    for (int i = -1; i < 2; i++) {
	        for (int j = -1; j < 2; j++) {
	            neighbors.add(new Point(x + i, y + j));
	        }
	    }
	    return neighbors;
	}
}
