package net.ddns.spellbank.harness;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class Solutions {
    private Solutions() {}

    public static Solution forDay(int day) {
        var type = find(day);
        if (type == null) {
            throw new IllegalArgumentException("No solution for day " + day + ".");
        }

        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot create solution for day " + day, e);
        }
    }

    public static List<Integer> days() {
        var days = new ArrayList<Integer>();
        for (int day = 1; day <= 25; day++) {
            if (find(day) != null) {
                days.add(day);
            }
        }

        return List.copyOf(days);
    }

    private static Class<? extends Solution> find(int day) {
        if (day < 1 || day > 25) {
            throw new IllegalArgumentException("Day must be between 1 and 25.");
        }

        String className = String.format(Locale.ROOT, "net.ddns.spellbank.day%02d.Day%02d", day, day);
        try {
            return Class.forName(className).asSubclass(Solution.class);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
