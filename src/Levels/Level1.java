package Levels;

import Game.Renderer;
import Objects.*;
import java.util.List;

public class Level1 extends Level {
    public Level1() {
        super(100, 80, 60, 25, 0);
    }

    @Override
    public boolean hasPowerUp() {
        return false;
    }

    @Override
    public void loadLevel(Renderer renderer, List<Brick> bricks) {
        int rows = 4;
        int cols = 10;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = startX + col * (brickWidth + spacing);
                int y = startY + row * (brickHeight + spacing);

                Brick brick;
                brick = new NormalBrick(x, y, brickWidth, brickHeight);

                renderer.addGameObject(brick);
                bricks.add(brick);
            }
        }
    }
}
