package net.ddns.spellbank.utils;

import java.util.Objects;

public class Point4D {
	public int w;
	public int x;
	public int y;
	public int z;
	public char c;
	
	public Point4D(int w, int x, int y, int z) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point4D(int w, int x, int y, int z, char c) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
		this.c = c;
	}
	
	public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof Point4D)) return false;
        Point4D a = (Point4D) obj;
        return this.w == a.w && 
               this.x == a.x && 
               this.y == a.y &&
               this.z == a.z ? true : false;
    }
    
    public int hashCode() {
        return Objects.hash(w, x, y, z);
    }
}
