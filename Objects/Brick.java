package Objects;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import types.BrickType;

import javax.imageio.ImageIO;

/**
 * Represents a single brick in the game.
 */
public class Brick extends GameObjects {
    private int hitPoints;
    private final BrickType type;
    private boolean destroyed = false;
    private BufferedImage brickImage;

    /** Constructor */
    public Brick(float x, float y, int width, int height, int hitPoints, BrickType type, String imagePath) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.type = type;
        loadImage(imagePath);
    }

    /** Loads brick image from path. */
    private void loadImage(String path) {
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                brickImage = ImageIO.read(imgURL);
            } else {
                System.out.println("Brick image not found: " + path);
                brickImage = null;
            }
        } catch (IOException e) {
            System.out.println("Cannot load brick image: " + path);
            brickImage = null;
        }
    }

    /** Changes brick image. */
    public void changeImage(String newPath) {
        try {
            java.net.URL imgURL = getClass().getResource(newPath);
            if (imgURL != null) {
                brickImage = ImageIO.read(imgURL);
            } else {
                System.out.println("Brick image not found: " + newPath);
            }
        } catch (IOException e) {
            System.out.println("Cannot change the brick image: " + newPath);
        }
    }

    public int getHitPoints() {
        return hitPoints;
    }

    /**
     * Reduces hit points.
     * Destroys brick if zero.
     */
    public void takeHit() {
        if (!destroyed) {
            hitPoints--;
            if (hitPoints <= 0) {
                destroyed = true;
            }
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    /** Update logic */
    @Override
    public void update() {
    }

    /** Draws the brick */
    @Override
    public void render(Graphics g) {
        if (destroyed) {
            return;
        }

        if (brickImage != null) {
            g.drawImage(brickImage, (int) x, (int) y, width, height, null);
        }
    }
}
