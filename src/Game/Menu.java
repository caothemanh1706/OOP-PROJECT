package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Menu {
    private final String[] menuItems = {"HIGH SCORE", "PLAY", "EXIT"};
    private final BufferedImage[] buttonImages = new BufferedImage[menuItems.length];

    private final int buttonWidth = 150;
    private final int buttonHeight = 50;

    public Menu() {
        try {
            buttonImages[0] = ImageIO.read(getClass().getResourceAsStream("/assets/HighScore.png"));
            buttonImages[1] = ImageIO.read(getClass().getResourceAsStream("/assets/Play.png"));
            buttonImages[2] = ImageIO.read(getClass().getResourceAsStream("/assets/Exit.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Menu button image not found.");
        }
    }

    public Rectangle[] getButtonBounds(int screenWidth, int screenHeight) {
        Rectangle[] bounds = new Rectangle[menuItems.length];

        int totalButtonsWidth = menuItems.length * buttonWidth;
        int numSpacing = menuItems.length + 1;
        int spacing = (screenWidth - totalButtonsWidth) / numSpacing;

        int startY = screenHeight / 2 + 150;

        for (int i = 0; i < menuItems.length; i++) {
            int x = spacing * (i + 1) + buttonWidth * i;
            int y = startY;
            bounds[i] = new Rectangle(x, y, buttonWidth, buttonHeight);
        }
        return bounds;
    }

    public void render(Graphics g, int screenWidth, int screenHeight) {
        Rectangle[] bounds = getButtonBounds(screenWidth, screenHeight);

        for (int i = 0; i < menuItems.length; i++) {
            Rectangle rect = bounds[i];

            if (buttonImages[i] != null) {
                g.drawImage(buttonImages[i], rect.x, rect.y, buttonWidth, buttonHeight, null);
            } else {
                g.setColor(Color.WHITE);
                g.fillRect(rect.x, rect.y, buttonWidth, buttonHeight);
                FontMetrics fm = g.getFontMetrics();
                int textX = rect.x + (rect.width - fm.stringWidth(menuItems[i])) / 2;
                int textY = rect.y + (rect.height - fm.getHeight()) / 2 + fm.getAscent();
                g.setColor(Color.BLACK);
                g.drawString(menuItems[i], textX + 10, textY + buttonHeight - 15);
            }
        }
    }
}