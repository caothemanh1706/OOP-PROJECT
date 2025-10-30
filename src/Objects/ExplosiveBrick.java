package Objects;

import types.BrickType;
import java.util.List;

/**
 * Represents a brick that can explode.
 */
public class ExplosiveBrick extends Brick {
    private final String explosiveImagePath = "/assets/block05.png";

    /** Constructor */
    public ExplosiveBrick(float x, float y, int width, int height) {
        super(x, y, width, height, 1, new BrickType(3, 1), "/assets/block05.png");
    }

    /**
     * When destroyed, it will explode around.
     */
    public void takeHit(List<Brick> bricks) {
        if (!isDestroyed()) {
            super.takeHit();

            if (isDestroyed()) {
                explode(bricks);
            }
        }
    }

    /**
     * Destroy the nearby bricks.
     */
    private void explode(List<Brick> bricks) {
        for (Brick other : bricks) {
            if (other != this && !other.isDestroyed()) {
                float dx = Math.abs(other.getX() - this.getX());
                float dy = Math.abs(other.getY() - this.getY());

                if (dx <= getWidth() * 1.2f && dy <= getHeight() * 1.2f) {
                    other.takeHit();
                }
            }
        }
    }
}
