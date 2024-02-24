package net.ddns.spellbank.day20;

import net.ddns.spellbank.utils.InputFile;
import net.ddns.spellbank.utils.MyUtils;

import java.util.*;

public class Day20 {


    public static void main(String[] args) {
        String file = "day20/input1";
        String[] lines = InputFile.getLines(file);

        System.out.println(part1(lines)); // 747304011
        System.out.println(part2(lines)); // 220366255099387
    }

    public static long part1(String[] lines) {
        var dispatcher = parse(lines);
        for (int i = 0; i < 1000; i++) pressButton(dispatcher, null, null);
        return dispatcher.getHigh() * dispatcher.getLow();
    }

    public static long part2(String[] lines) {
        var dispatcher = parse(lines);
        int i = 0;
        var inputs = new ArrayList<Integer>();
        while (inputs.size() != 4) pressButton(dispatcher, ++i, inputs);
        long lcm = 1;
        for (var pressed : inputs) lcm = MyUtils.lcm(lcm, pressed);
        return lcm;
    }

    public static PulseDispatcher parse(String[] lines) {
        var map = new HashMap<String, Module>();
        var dispatcher = new PulseDispatcher(map);
        for (var line : lines) {
            var nameOutputs = line.split(" -> ");
            var type = nameOutputs[0].charAt(0);
            var name = nameOutputs[0].substring(1);
            var module = switch (type) {
                case '%' -> new FlipFlop(name, dispatcher);
                case '&' -> new Conjunction(name, dispatcher);
                case 'b' -> {
                    name = "broadcaster";
                    yield new Broadcaster(dispatcher);
                }
                default -> throw new IllegalArgumentException("Invalid module");
            };
            for (var s : nameOutputs[1].split(", ")) {
                module.addOutput(s);
            }
            map.put(name, module);
        }
        for (var m : map.values()) {
            for (var d : m.outputs)
                map.getOrDefault(d, new FlipFlop("output", dispatcher)).addInput(m.getName());
        }
        return dispatcher;
    }

    private static void pressButton(PulseDispatcher d, Integer pressed, List<Integer> inputs) {
        d.queueDispatch(new Pulse(PulseType.LOW, "button", "broadcaster"));
        d.dispatch(false, pressed, inputs);
    }

    public enum PulseType {
        HIGH, LOW
    }

    public record Pulse(PulseType type, String src, String dest) {}

    public static abstract class Module {
        List<String> outputs;
        private final String name;
        public final PulseDispatcher dispatcher;

        public String getName() { return name; }

        public Module(String name, PulseDispatcher dispatcher) {
            this.name = name;
            this.dispatcher = dispatcher;
            outputs = new ArrayList<>();
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o instanceof Module m) {
                return this.name.equals(m.getName());
            }
            return false;
        }

        public int hashCode() {
            return name.hashCode();
        }

        public void addOutput(String m) {
            outputs.add(m);
        }

        public void addInput(String name) {}

        public abstract void receivePulse(Pulse p) ;
    }

    public static class FlipFlop extends Module {
        private boolean on = false;
        public FlipFlop(String name, PulseDispatcher d) {
            super(name, d);
        }

        @Override
        public void receivePulse(Pulse p) {
            if (p.type == PulseType.LOW) {
                var send = on ? PulseType.LOW : PulseType.HIGH;
                on = !on;
                for (var dest : outputs)
                    dispatcher.queueDispatch(new Pulse(send, getName(), dest));
            }
        }
    }

    public static class Conjunction extends Module {
        private final Map<String, PulseType> recent;

        public Conjunction(String name, PulseDispatcher d) {
            super(name, d);
            recent = new HashMap<>();
        }

        @Override
        public void addInput(String name) {
            recent.put(name, PulseType.LOW);
        }

        @Override
        public void receivePulse(Pulse p) {
            recent.put(p.src, p.type);
            PulseType send = PulseType.LOW;
            for (var e : recent.values()) {
                if (e == PulseType.LOW) {
                    send = PulseType.HIGH;
                    break;
                }
            }

            for (var dest : outputs)
                dispatcher.queueDispatch(new Pulse(send, getName(), dest));
        }
    }

    public static class Broadcaster extends Module {
        public Broadcaster(PulseDispatcher d) {
            super("broadcaster", d);
        }

        public void receivePulse(Pulse p) {
            for (var output : outputs)
                dispatcher.queueDispatch(new Pulse(p.type, getName(), output));
        }
    }

    public static class PulseDispatcher {
        private final Deque<Pulse> queue;
        private long high;
        private long low;
        private final Map<String, Module> map;

        public PulseDispatcher(Map<String, Module> map) {
            this.map = map;
            queue = new ArrayDeque<>();
        }

        public void queueDispatch(Pulse p) {
            if (p.type == PulseType.LOW) low++;
            else high++;
            queue.add(p);
        }

        public void dispatch(boolean debug, Integer pressed, List<Integer> inputs) {
            while (!queue.isEmpty()) {
                var p = queue.removeFirst();
                if (inputs != null && p.dest.equals("vr") && p.type == PulseType.HIGH)
                    inputs.add(pressed);
                var m = map.get(p.dest);
                if (m == null) {
                    if (debug) {
                        var pulse = p.type == PulseType.HIGH ? "high" : "low";
                        System.out.println(p.dest + " received " + pulse);
                    }
                } else m.receivePulse(p);
            }
        }

        public long getHigh() {
            return high;
        }

        public long getLow() {
            return low;
        }
    }
}