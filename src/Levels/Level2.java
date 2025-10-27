package Levels;

import Game.Renderer;
import Objects.Brick;
import Objects.StrongBrick;
import Objects.NormalBrick;
import java.util.List;

public class Level2 extends Level {
    public Level2() {
        super(100, 80, 60, 25, 0);
    }

    @Override
    public boolean hasPowerUp() {
        return false;
    }

    @Override
    public void loadLevel(Renderer renderer, List<Brick> bricks) {
        bricks.clear();

        int totalRows = 8;

        for (int row = 0; row < totalRows; row++) {
            int bricksInRow = row + 1;
            int totalWidth = bricksInRow * brickWidth;
            int startX = (screenWidth - totalWidth) / 2;

            for (int col = 0; col < bricksInRow; col++) {
                int x = startX + col * (brickWidth + spacing);
                int y = startY + row * (brickHeight + spacing);

                Brick brick;

                boolean isEdge = (row == 0 || row == totalRows - 1 || col == 0 || col == bricksInRow - 1);

                if (isEdge) {
                    brick = new StrongBrick(x, y, brickWidth, brickHeight);
                } else {
                    brick = new NormalBrick(x, y, brickWidth, brickHeight);
                }

                bricks.add(brick);
                renderer.addGameObject(brick);
            }
        }
    }
}
