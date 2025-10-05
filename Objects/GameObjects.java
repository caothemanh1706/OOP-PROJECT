package Objects;

import java.awt.*;

public abstract class GameObjects {
    protected float x;
    protected float y;
    protected int width;
    protected int height;

    public GameObjects(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public abstract void update();
    public abstract void render(Graphics g);
}
