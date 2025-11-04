package powerups;

import Objects.Paddle;
import java.awt.*;

public abstract class PowerUp {
    protected float x, y;
    protected int width = 20, height = 20;
    protected float speedY = 3f;
    protected boolean active = true;

    public PowerUp(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y += speedY;
    }

    public void render(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillOval((int)x, (int)y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public abstract void activate(Paddle paddle);
}
