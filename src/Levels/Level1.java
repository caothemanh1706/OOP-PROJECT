package Levels;

import Game.Renderer;
import Objects.*;
import java.util.List;

/**
 * Defines layout and settings for Level 1.
 */
public class Level1 extends Level {
    public Level1() {
        super(100, 80, 60, 25, 0);
    }

    @Override
    public boolean hasPowerUp() {
        return false;
    }

    /** Loads level 1 from file. */
    @Override
    public void loadLevel(Renderer renderer, List<Brick> bricks) {
        LevelLoader.loadFromFile("level1.txt", renderer, bricks, startX, startY, brickWidth, brickHeight, spacing);
    }
}
