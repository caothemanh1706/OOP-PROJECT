package Objects;
import types.BrickType;
import java.awt.Color;

/**
 * Represents a strong brick with 2 hit point.
 */
public class StrongBrick extends Brick {
    /** Constructor */
    public StrongBrick(float x, float y, int width, int height) {
        super(x, y, width, height, 2,new BrickType("Strong", 2));
        setColor(Color.BLUE);
    }

    /**
     * Handles a hit.
     * Changes color after first hit, destroyed after second */
    @Override
    public void takeHit() {
        super.takeHit();
        if (getHitPoints() == 1) {
            setColor(Color.CYAN);
        }
    }
}
