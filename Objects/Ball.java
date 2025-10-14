package Objects;

import Game.GameManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Ball extends MovableObject {
    private float speed;
    private float directionX;
    private float directionY;
    private int screenWidth;
    private int screenHeight;
    private BufferedImage ballImage;

    public Ball(float x, float y, int width, int height, float dx, float dy, float directionX, float directionY, float speed,
                int screenWidth, int screenHeight, String imagePath) {
        super(x, y, width, height, dx, dy);
        this.directionX = directionX;
        this.directionY = directionY;
        this.speed = speed;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        loadImage(imagePath);
    }

    private void loadImage(String path) {
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                ballImage = ImageIO.read(imgURL);
            } else {
                System.out.println("Ball image not found: " + path);
                ballImage = null;
            }
        } catch (IOException e) {
            System.out.println("Cannot load ball image: " + path);
            ballImage = null;
        }
    }

    @Override
    public void update() {
        if (directionX != 0 || directionY != 0) {
            x += directionX * speed;
            y += directionY * speed;

            //Wall Collision
            if (x <= 0 || x + width >= screenWidth) {
                directionX = -directionX;
            }

            //Celling Collision
            if (y <= 0) {
                directionY = -directionY;
            }

            if (y + height >= screenHeight) {
                // reset or loselife
                resetPosition();
            }
        }
    }

    public void resetPosition() {
        x = GameManager.getInstance().getPaddle().getX() + GameManager.getInstance().getPaddle().getWidth() / 2 - width / 2;
        y = GameManager.getInstance().getPaddle().getY() - height - 5;
        directionX = 0f;
        directionY = 0f;
        speed = 5f;
    }

    public void bounceX() {
        directionX = -directionX;
    }

    public void bounceY() {
        directionY = -directionY;
    }

    @Override
    public void render(Graphics g) {
        if (ballImage != null) {
            g.drawImage(ballImage, (int)x, (int)y, width, height, null);
        } else {
            g.setColor(Color.WHITE);
            g.fillOval((int)x, (int)y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDirectionX() {
        return directionX;
    }

    public void setDirectionX(float directionX) {
        this.directionX = directionX;
    }

    public float getDirectionY() {
        return directionY;
    }

    public void setDirectionY(float directionY) {
        this.directionY = directionY;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
