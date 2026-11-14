package net.ddns.spellbank.utils;

import net.ddns.spellbank.harness.Inputs;

public class InputFile {
    public static String[] getLines(String fileName) {
        return Inputs.resource(fileName);
    }
}
