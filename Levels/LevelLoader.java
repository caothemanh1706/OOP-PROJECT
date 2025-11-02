package Levels;

import Objects.*;
import Game.Renderer;
import java.io.*;
import java.util.*;

/**
 * Load brick layouts from text files.
 */
public class LevelLoader {
    /** Loads a level from a text file and creates bricks */
    public static void loadFromFile(String filePath, Renderer renderer, List<Brick> bricks,
                                    int startX, int startY, int brickWidth, int brickHeight, int spacing) {
        try (InputStream input = LevelLoader.class.getResourceAsStream("/Levels/" + filePath)) {
            if (input == null) {
                System.out.println("Không tìm thấy file level: /Levels/" + filePath);
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line;
            int row = 0;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] values = line.split(" ");
                for (int col = 0; col < values.length; col++) {
                    int type = Integer.parseInt(values[col]);
                    if (type != 0) {
                        int x = startX + col * (brickWidth + spacing);
                        int y = startY + row * (brickHeight + spacing);

                        Brick brick;

                        switch (type) {
                            case 1 -> brick = new NormalBrick(x, y, brickWidth, brickHeight);
                            case 2 -> brick = new StrongBrick(x, y, brickWidth, brickHeight);
                            case 3 -> brick = new ExplosiveBrick(x, y, brickWidth, brickHeight);
                            default -> brick = new UnbreakableBrick(x, y, brickWidth, brickHeight);
                        }
                        bricks.add(brick);
                        renderer.addGameObject(brick);
                    }
                }
                row++;
            }

        } catch (IOException e) {
            System.out.println("Lỗi đọc file level: " + e.getMessage());
        }
    }
}
