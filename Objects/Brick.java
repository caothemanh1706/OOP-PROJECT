package Objects;
import java.awt.*;
import types.BrickType;

/**
 * Represents a single brick in the game.
 */
public class Brick extends GameObjects {
    private int hitPoints;
    private final BrickType type;
    private Color color;
    private boolean destroyed = false;

    /** Constructor */
    public Brick(float x, float y, int width, int height, int hitPoints, BrickType type) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.type = type;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setColor(Color newColor) {
        this.color = newColor;
    }

    /**
     * Reduces hit points.
     * Destroys brick if zero.
     */
    public void takeHit() {
        if (!destroyed) {
            hitPoints--;
            if (hitPoints <= 0) {
                destroyed = true;
            }
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    /** Update logic */
    @Override
    public void update() {
    }

    /** Draws the brick */
    @Override
    public void render(Graphics g) {
        if (!destroyed) {
            g.setColor(color);
            g.fillRect((int) x, (int) y, width, height);
        }
    }
}
