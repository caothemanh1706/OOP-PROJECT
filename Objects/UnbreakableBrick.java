package Objects;

import types.BrickType;

/**
 * Represents a brick that cannot be destroyed.
 */
public class UnbreakableBrick extends Brick {
    private final String unbreakableImagePath = "/assets/block04.png";

    /** Constructor */
    public UnbreakableBrick(float x, float y, int width, int height) {
        super(x, y, width, height, Integer.MAX_VALUE,
                new BrickType("Unbreakable", Integer.MAX_VALUE),"/assets/block04.png");
    }
}
