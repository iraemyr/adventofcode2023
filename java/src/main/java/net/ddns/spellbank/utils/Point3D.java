package net.ddns.spellbank.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Point3D {
	public int x;
	public int y;
	public int z;
	public char c;
	private Set<Point3D> deltas;
	private Point3D[] transforms;
	
	public Point3D(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		deltas = new HashSet<>();
	}
	
	public Point3D(int x, int y, int z, char c) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.c = c;
	}
	
	public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof Point3D)) return false;
        Point3D a = (Point3D) obj;
        return this.x == a.x && this.y == a.y && this.z == a.z ? true : false;
    }
    
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
    
    public void print() {
        System.out.println(x + ":" + y + ":" + z);
    }
    
    public void addDelta(Point3D p) {
        deltas.add(getDelta(p));
    }
    
    public void addDeltas(Collection<Point3D> del) {
        for (var d : del) {
            if (d.equals(this)) continue;
            addDelta(d);
        }
    }
    
    public Set<Point3D> getDeltas() {
        Set<Point3D> s = new HashSet<>();
        s.addAll(deltas);
        return s;
    }
    
    private Point3D getDelta(Point3D d) {
        int[] p = new int[3];
        p[0] = Math.abs(x - d.x);
        p[1] = Math.abs(y - d.y);
        p[2] = Math.abs(z - d.z);
        Arrays.sort(p);
        return new Point3D(p[0], p[1], p[2]);
    }
    
    
    private Point3D rotateXY() {
        return new Point3D(y, -x, z);
    }
    
    private Point3D rotateXZ() {
        return new Point3D(z, y, -x);
    }
    
    public Point3D transform(int n) {
        if (transforms == null) {
            transforms = new Point3D[24];
            transforms[0] = this;
            transforms[1] = transforms[0].rotateXY();
            transforms[2] = transforms[1].rotateXY();
            transforms[3] = transforms[2].rotateXY();
        
            for (int i = 0; i < 5; i++) {
                transforms[i * 4 + 4] = transforms[i].rotateXZ();
                transforms[i * 4 + 5] = transforms[i * 4 + 4].rotateXY();
                transforms[i * 4 + 6] = transforms[i * 4 + 5].rotateXY();
                transforms[i * 4 + 7] = transforms[i * 4 + 6].rotateXY();
            }
        }
        return transforms[n];
    }
}
