package net.ddns.spellbank.day22;

import net.ddns.spellbank.utils.InputFile;

import java.util.*;

public class Day22 {

    public record Point(int x, int y, int z) {}

    public static class Slab implements Comparable<Slab>{
        public final int id, minX, maxX, minY, maxY;
        public int minZ, maxZ;
        private final Set<Slab> supports;
        private final Set<Slab> supportedBy;

        public Slab(int id, int minX, int maxX,
                    int minY, int maxY, int minZ, int maxZ) {
            this.id = id;
            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;
            this.minZ = minZ;
            this.maxZ = maxZ;
            supports = new HashSet<>();
            supportedBy = new HashSet<>();
        }

        public void drop(Map<Point, Slab> map) {
            int i = 0;
            while (supportedBy.isEmpty() && minZ - i - 1 > 0) {
                for (int x = minX; x <= maxX; x++) {
                    for (int y = minY; y <= maxY; y++) {
                        var p = new Point(x, y, minZ - i - 1);
                        var s = map.get(p);
                        if (s != null) s.addSupport(this);
                    }
                }
                if (supportedBy.isEmpty()) i++;
            }
            minZ -= i;
            maxZ -= i;
            placeSlab(this, map);
        }

        public boolean precarious() {
            return supportedBy.size() == 1;
        }

        public boolean isSafe() {
            if (supports.isEmpty()) return true;
            for (var s : supports) if (s.precarious()) return false;
            return true;
        }

        public int wouldFall() {
            if (supports.isEmpty()) return 0;
            var visited = new HashSet<Slab>();
            var q = new PriorityQueue<Slab>();
            q.offer(this);
            while (!q.isEmpty()) {
                var slab = q.poll();
                visited.add(slab);
                for (var s : slab.supports) {
                    if (visited.containsAll(s.supportedBy)) q.offer(s);
                }
            }
            return visited.size() - 1;
        }

        public void addSupport(Slab slab) {
            if (supports.add(slab)) slab.addSupportedBy(this);
        }

        public void addSupportedBy(Slab slab) {
            supportedBy.add(slab);
        }

        @Override
        public int compareTo(Slab o) {
            return minZ - o.minZ;
        }

        public int hashCode() {
            return id;
        }

        public boolean equals(Object o) {
            if (o == null) return false;
            if (o instanceof Slab s) return id == s.id;
            return false;
        }
    }

    public static void main(String[] args) {
        String file = "day22/input1";
        String[] lines = InputFile.getLines(file);
        var slabs = parseSlabs(lines);
        Collections.sort(slabs);
        var map = new HashMap<Point, Slab>();
        for (var slab : slabs) slab.drop(map);

        System.out.println(part1(slabs)); // 432
        System.out.println(part2(slabs)); // 63166
    }

    public static long part1(List<Slab> slabs) {
        long safe = 0;
        for (var slab : slabs) if (slab.isSafe()) safe++;
        return safe;
    }

    public static long part2(List<Slab> slabs) {
        long sum = 0;
        for (var slab : slabs) sum += slab.wouldFall();
        return sum;
    }

    public static List<Slab> parseSlabs(String[] lines) {
        var slabs = new ArrayList<Slab>();
        int id = 0;
        for (var line : lines) {
            var fields = line.split("~");
            var mins = fields[0].split(",");
            var maxs = fields[1].split(",");
            slabs.add(new Slab(id++, Integer.parseInt(mins[0]), Integer.parseInt(maxs[0]),
                    Integer.parseInt(mins[1]), Integer.parseInt(maxs[1]),
                            Integer.parseInt(mins[2]), Integer.parseInt(maxs[2])));
        }
        return slabs;
    }

    public static void placeSlab(Slab s, Map<Point, Slab> map) {
        for (int x = s.minX; x <= s.maxX; x++)
            for (int y = s.minY; y <= s.maxY; y++)
                for (int z = s.minZ; z <= s.maxZ; z++) {
                    var p = new Point(x, y, z);
                    if (map.containsKey(p))
                        throw new IllegalStateException("Error in drop");
                    map.put(new Point(x, y, z), s);
                }
    }
}