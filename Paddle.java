package Objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

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

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(java.awt.Color.WHITE);
        g2d.fill(new Rectangle2D.Float(x, y, width, height));
    }

    @Override
    public void update() {
        x += dx;
        y += dy;
    }
}
