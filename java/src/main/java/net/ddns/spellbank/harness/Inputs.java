package net.ddns.spellbank.harness;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public final class Inputs {
    private Inputs() {}

    public static String[] load(int day) {
        return load(day, null, null);
    }

    public static String[] load(int day, String resource, Path file) {
        return loadString(day, resource, file).lines().toArray(String[]::new);
    }

    public static String loadString(int day) {
        return loadString(day, null, null);
    }

    public static String loadString(int day, String resource, Path file) {
        if (file != null) {
            try {
                return Files.readString(file, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new UncheckedIOException("Cannot read input file: " + file, e);
            }
        }

        String name = resource == null ? "input1" : resource;

        return resourceString(String.format(Locale.ROOT, "day%02d/%s", day, name));
    }

    public static String[] resource(String name) {
        return resourceString(name).lines().toArray(String[]::new);
    }

    public static String resourceString(String name) {
        var stream = Inputs.class.getClassLoader().getResourceAsStream(name);
        if (stream == null) {
            throw new IllegalArgumentException("Input resource not found: " + name);
        }

        try (stream) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read input resource: " + name, e);
        }
    }
}
