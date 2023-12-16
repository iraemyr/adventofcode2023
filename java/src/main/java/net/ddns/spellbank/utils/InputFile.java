package net.ddns.spellbank.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class InputFile {
    public static String[] getLines(String fileName) {

        InputFile io = new InputFile();
        String[] lines = null;
        ClassLoader classLoader = io.getClass().getClassLoader();
        URL url = classLoader.getResource(fileName);
        if (url == null) {
            System.out.println("Could not find file: " + fileName);
            return lines;
        }

        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            lines = br.lines().toArray(String[]::new);
        } catch (IOException e) {
            System.out.println("Can't open file");
        }

        return lines;
    }

}
