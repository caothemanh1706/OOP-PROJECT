package Game;

import java.io.*;
import java.util.Scanner;

public class HighScoreManager {
    private static final String FILE_PATH = "data/highscore.txt";

    public static int loadHighScore() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return 0;
        }

        try (Scanner sc = new Scanner(file)) {
            if (sc.hasNextInt()) {
                return sc.nextInt();
            } else {
                return 0;
            }
        } catch (Exception e) {
            System.err.println("Lỗi File: " + e.getMessage());
            return 0;
        }
    }

    public static void saveHighScore(int score) {
        try {
            File file = new File(FILE_PATH);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(String.valueOf(score));
            }

        } catch (IOException e) {
            System.err.println("Lỗi Lưu File: " + e.getMessage());
        }
    }
    public static void resetHighScore() {
        saveHighScore(0);
    }

    public static void updateHighScore(int score) {
        int currentHigh = loadHighScore();
        if (score > currentHigh) {
            saveHighScore(score);
        }
    }
}
