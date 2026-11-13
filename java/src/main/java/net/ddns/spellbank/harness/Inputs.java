package net.ddns.spellbank.harness;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        if (file != null) {
            try {
                return Files.readAllLines(file, StandardCharsets.UTF_8).toArray(String[]::new);
            } catch (IOException e) {
                throw new UncheckedIOException("Cannot read input file: " + file, e);
            }
        }

        String name = resource == null ? "input1" : resource;

        return resource(String.format(Locale.ROOT, "day%02d/%s", day, name));
    }

    public static String[] resource(String name) {
        var stream = Inputs.class.getClassLoader().getResourceAsStream(name);
        if (stream == null) {
            throw new IllegalArgumentException("Input resource not found: " + name);
        }

        try (var reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            return reader.lines().toArray(String[]::new);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read input resource: " + name, e);
        }
    }
}
