package Objects;

import java.awt.*;

public class Ball extends MovableObject {
    private float speed;
    private float directionX;
    private float directionY;

    public Ball(float x, float y, int width, int height, float dx, float dy, float directionX, float directionY, float speed) {
        super(x, y, width, height, dx, dy);
        this.directionX = directionX;
        this.directionY = directionY;
        this.speed = speed;
    }

    @Override
    public void update() {
        x += directionX * speed;
        y += directionY * speed;
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(java.awt.Color.WHITE);
        g2d.fillOval((int) x, (int) y, width * 2, height * 2);
    }

}
