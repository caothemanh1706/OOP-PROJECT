package Objects;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Paddle extends MovableObject {
    private float speed;
    private BufferedImage paddleImage;

    public Paddle(int x, int y, int width, int height, float dx, float dy, float speed, String imagePath) {
        super(x, y, width, height, dx, dy);
        this.speed = speed;
        loadImage(imagePath);
    }

    private void loadImage(String path) {
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                paddleImage = ImageIO.read(imgURL);
            } else {
                System.out.println("Paddle image not found: " + path);
                paddleImage = null;
            }
        } catch (IOException e) {
            System.out.println("Cannot load paddle image: " + path);
            paddleImage = null;
        }
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
        if (paddleImage != null) {
            g.drawImage(paddleImage, (int)x, (int)y, width, height, null);
        } else {
            g.setColor(Color.WHITE);
            g.fillRect((int)x, (int)y, width, height);
        }
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

    public void setX(float x) {
        this.x = x;
    }

    public float getY() { return y; }

    public void setWidth(int newWidth) {
        this.width = newWidth;
    }

    public int getWidth() {
        return width;
    }
}
