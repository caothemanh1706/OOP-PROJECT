package Levels;

import Game.Renderer;
import Objects.*;
import java.util.List;

/**
 * Base class for all game levels.
 */
public abstract class Level {
    protected int startX;
    protected int startY;
    protected int brickWidth;
    protected int brickHeight;
    protected int spacing;
    int screenHeight = 600;
    int screenWidth = 800;

    /**
     * Creates a new level with given layout settings.
     */
    public Level(int startX, int startY, int brickWidth, int brickHeight, int spacing) {
        this.startX = startX;
        this.startY = startY;
        this.brickWidth = brickWidth;
        this.brickHeight = brickHeight;
        this.spacing = spacing;
    }

    /** Level can drop power-ups. */
    public boolean hasPowerUp() {
        return false;
    }

    public abstract void loadLevel(Renderer renderer, List<Brick> bricks);
}
