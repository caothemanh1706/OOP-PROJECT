package Objects;
import types.BrickType;

/**
 * Represents a normal brick with 1 hit point.
 */
public class NormalBrick extends Brick {
    /** Constructor */
    public NormalBrick(float x, float y, int width, int height) {
        super(x, y, width, height, 1,
                new BrickType(1, 1), "/assets/block01.png");
    }

    /**
     * Normal brick is destroyed after 1 hit.
     */
    @Override
    public void takeHit() {
        super.takeHit();
    }
}
