package net.ddns.spellbank.day23;

import net.ddns.spellbank.utils.InputFile;

import java.util.*;

public class Day23 {

    public record ParseMapResult(Point start, Point end, char[][] map) {}
    public record Node(Point p, List<Edge> paths) {}

    public record Edge(int dist, Node node) {}

    public record State(Point p, long cost, Set<Point> visited) {}

    enum Direction {NORTH, SOUTH, EAST, WEST}

    public record Point(int row, int col) {

        private boolean canWalk(Direction d, char c, boolean spikes) {
            if (c == '.') return true;
            if (c == '#') return false;
            if (spikes) return true;
            return switch(d) {
                case NORTH -> c == '^';
                case SOUTH -> c == 'v';
                case EAST -> c == '>';
                case WEST -> c == '<';
            };
        }
        public List<Point> neighbors(char[][] map, boolean spikes) {
            Point p;
            var neighbors = new ArrayList<Point>();
            if (row > 0) {
                p = new Point(row - 1, col);
                var c = map[p.row][p.col];
                if (canWalk(Direction.NORTH, c, spikes))
                    neighbors.add(p);
            }
            if (row < map.length - 1) {
                p = new Point(row + 1, col);
                var c = map[p.row][p.col];
                if (canWalk(Direction.SOUTH, c, spikes))
                    neighbors.add(p);
            }
            if (col > 0) {
                p = new Point(row, col - 1);
                var c = map[p.row][p.col];
                if (canWalk(Direction.WEST, c, spikes))
                    neighbors.add(p);
            }
            if (col < map[0].length - 1) {
                p = new Point(row, col + 1);
                var c = map[p.row][p.col];
                if (canWalk(Direction.EAST, c, spikes))
                    neighbors.add(p);
            }
            return neighbors;
        }
    }


    public static void main(String[] args) {
        String file = "day23/input1";
        String[] lines = InputFile.getLines(file);
        var result = parseMap(lines);

        System.out.println(part1(result)); // 2018
        System.out.println(part2(result)); // 6406
    }

    public static long part1(ParseMapResult info) {
        var nodes = getNodes(info.start, info.end, info.map, false);
        return search(info.start, info.end, nodes);
    }

    public static long part2(ParseMapResult info) {
        var nodes = getNodes(info.start, info.end, info.map, true);
        return search(info.start, info.end, nodes);
    }

    public static long search(Point start, Point end, Map<Point, Node> nodes) {
        long max = 0;
        var q = new ArrayList<State>();
        State state;
        state = new State(start, 0, new HashSet<>());
        q.add(state);
        while (!q.isEmpty()) {
            var s = q.removeLast();
            if (s.p == end) {
                max = Math.max(max, s.cost);
                continue;
            }

            var n = nodes.get(s.p);
            for (var next : n.paths) {
                var visited = new HashSet<>(s.visited);
                visited.add(s.p);
                if (!visited.contains(next.node.p))
                    q.add(new State(next.node.p, s.cost + next.dist, visited));
            }
        }
        return max;
    }

    public static ParseMapResult parseMap(String[] lines) {
        char[][] map = new char[lines.length][lines.length];

        Point start = null;
        Point end = null;
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                map[i][j] = lines[i].charAt(j);
                if (start == null && i == 0 && map[i][j] == '.')
                    start = new Point(i, j);
                if (end == null && i == lines[i].length() - 1 && map[i][j] == '.')
                    end = new Point(i, j);
            }
        }
        return new ParseMapResult(start, end, map);
    }

    public static Map<Point, Node> getNodes(Point start, Point end,
                                            char[][] map, boolean spikes) {
        var nodes = new HashMap<Point, Node>();
        var s = new Node(start, new ArrayList<>());
        nodes.put(start, s);
        nodes.put(end, new Node(end, new ArrayList<>()));
        for (int i = 1; i < map.length - 1; i++) {
            for (int j = 1; j < map[i].length - 1; j++) {
                var p = new Point(i, j);
                if (map[p.row][p.col] != '.') continue;
                var neighbors = p.neighbors(map, true);
                if (neighbors.size() > 2)
                    nodes.put(p, new Node(p, new ArrayList<>()));
            }
        }

        for (var n : nodes.values()) {
            if (n.p == end) continue;
            walk(n, map, nodes, spikes);
        }

        return nodes;
    }

    public static void walk(Node node, char[][] map,
                            Map<Point, Node> nodes, boolean spikes) {
        var visited = new HashSet<Point>();
        visited.add(node.p);
        var pos = node.p;
        var neighbors = pos.neighbors(map, spikes);
        for (var p : neighbors) {
            visited.clear();
            visited.add(node.p);
            int steps = 1;
            pos = p;
            do {
                var neighbors2 = pos.neighbors(map, spikes);
                neighbors2.removeAll(visited);
                if (neighbors2.isEmpty())
                    throw new IllegalStateException(node.p.toString());
                steps++;
                visited.add(pos);
                pos = neighbors2.getFirst();
            } while (!nodes.containsKey(pos));
            var n = nodes.get(pos);
            node.paths.add(new Edge(steps, n));
        }
    }
}