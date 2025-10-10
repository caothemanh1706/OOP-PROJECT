package Objects;
import types.BrickType;

/**
 * Represents a strong brick with 2 hit point.
 */
public class StrongBrick extends Brick {
    private final String strongImagePath = "/assets/block02.png";
    /** Constructor */
    public StrongBrick(float x, float y, int width, int height) {
        super(x, y, width, height, 2,
                new BrickType("Strong", 2), "/assets/block02.png");
    }

    /**
     * Replace cracked bricks after 1 hit.
     */
    @Override
    public void takeHit() {
        super.takeHit();
        if (getHitPoints() == 1) {
            String crackedImagePath = "/assets/block06.png";
            changeImage(crackedImagePath);
        }
    }
}
