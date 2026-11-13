package net.ddns.spellbank.harness;

import java.lang.reflect.Method;
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
            var solution = (Solution) type.getDeclaredConstructor().newInstance();
            partMethod(solution, 1);
            partMethod(solution, 2);

            return solution;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot create solution for day " + day, e);
        }
    }

    public static Method partMethod(Solution solution, int part) {
        if (part != 1 && part != 2) {
            throw new IllegalArgumentException("Part must be 1 or 2.");
        }

        String name = "solvePart" + part;
        try {
            var numeric = solution.getClass().getMethod(name, String[].class);
            var text = solution.getClass().getMethod(name + "Str", String[].class);
            boolean hasNumeric = numeric.getDeclaringClass() != Solution.class;
            boolean hasText = text.getDeclaringClass() != Solution.class;
            if (hasNumeric == hasText) {
                throw new IllegalArgumentException(solution.getClass().getSimpleName()
                        + " must implement exactly one of " + name + " or " + name + "Str.");
            }

            return hasNumeric ? numeric : text;
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Cannot resolve solution part " + part, e);
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

    private static Class<?> find(int day) {
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
