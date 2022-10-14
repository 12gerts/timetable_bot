package org.bot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Класс, реализующий чтение из файла
 */
public class Reader {
    /**
     * Метод, производящий чтение данных из файла
     *
     * @param fileName имя файла
     * @return содержимое файла
     */
    public String readFile(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        }
        int length = sb.length();
        return sb.substring(0, length - 1);
    }

}
