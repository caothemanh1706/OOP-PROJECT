package Levels;

import Game.Renderer;
import Objects.*;

import java.util.List;

/**
 * Defines layout and settings for Level 2.
 */
public class Level2 extends Level {
    public Level2() {
        super(100, 80, 60, 25, 0);
    }

    @Override
    public boolean hasPowerUp() {
        return false;
    }

    /** Loads level 2 from file. */
    @Override
    public void loadLevel(Renderer renderer, List<Brick> bricks) {
        LevelLoader.loadFromFile("level2.txt", renderer, bricks, startX, startY, brickWidth, brickHeight, spacing);

    }
}