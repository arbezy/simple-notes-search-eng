package org.example;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Tokenizer {
    public static String[] tokenize(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            sb.append(line);
        }

        return sb.toString()
                .toLowerCase()
                .replaceAll("-", " ")
                .split(" ", 0);
    }
}
