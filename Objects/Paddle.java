package Objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Rectangle;

public class Paddle extends MovableObject {
    private float speed;

    public Paddle(int x, int y, int width, int height, float dx, float dy, float speed) {
        super(x, y, width, height, dx, dy);
        this.speed = speed;
    }

    public void moveLeft() {
        dx = -speed;
    }

    public void moveRight() {
        dx = speed;
    }
    public void stop() {
        dx = 0;
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(java.awt.Color.WHITE);
        g2d.fill(new Rectangle2D.Float(x, y, width, height));
    }

    @Override
    public void update() {
        x += dx;
        if (x < 0) x = 0;
        if (x + width > 785) x = 785 - width;
        y += dy;
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }

    public float getX() {
        return x;
    }

    public float getY() { return y; }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}
