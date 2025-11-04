package Objects;

import java.awt.*;
import javax.swing.*;

/**
 * Handles the explosion visual effect when an ExplosiveBrick is destroyed.
 */
public class ExplosionEffect {
    private static Image explosionImage;

    private float x, y;
    private int width, height;

    private int lifetime = 90;
    private int frameCounter = 0;

    /**
     * Creates an explosion effect at the given position.
     */
    public ExplosionEffect(float x, float y, int width, int height, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        if (explosionImage == null) {
            loadImage(imagePath);
        }
    }

    /**
     * Loads the explosion image from the specified resource path.
     */
    private void loadImage(String path) {
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                Image img = Toolkit.getDefaultToolkit().getImage(imgURL);
                MediaTracker tracker = new MediaTracker(new JPanel());
                tracker.addImage(img, 0);
                tracker.waitForID(0);
                explosionImage = img;
            } else {
                System.out.println("⚠ Explosion image not found: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the effect’s frame counter.
     */
    public void update() {
        frameCounter++;
    }

    /**
     * Renders the explosion image on the screen.
     */
    public void render(Graphics g) {
        if (explosionImage != null) {
            g.drawImage(explosionImage, (int)x, (int)y, width, height, null);
        }
    }

    /**
     * Checks if the explosion effect has finished.
     */
    public boolean isFinished() {
        return frameCounter >= lifetime;
    }
}
