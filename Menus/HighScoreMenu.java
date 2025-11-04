package Menus;

import Game.HighScoreManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class HighScoreMenu implements MenuGame {
    private final String[] items = {"EXIT MENU"};
    private final BufferedImage[] buttonImages = new BufferedImage[items.length];
    private int hoveredIndex = -1;

    private final int buttonWidth = 180;
    private final int buttonHeight = 55;
    private BufferedImage background;

    public HighScoreMenu() {
        try {
            background = ImageIO.read(getClass().getResourceAsStream("/assets/HighScoreBackground.png"));
            buttonImages[0] = ImageIO.read(getClass().getResourceAsStream("/assets/ExitMenu.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("HighScore background not found.");
            background = null;
        }
    }

    @Override
    public void render(Graphics g, int screenWidth, int screenHeight) {
        if (background != null) {
            g.drawImage(background, 0, 0, screenWidth, screenHeight, null);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, screenWidth, screenHeight);
        }

        int highScore = HighScoreManager.loadHighScore();
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        String scoreText = String.valueOf(highScore);


        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(scoreText);
        int textHeight = fm.getHeight();

        int x = (screenWidth - textWidth) / 2;
        int y = (screenHeight - textHeight) / 2 + fm.getAscent();

        g.drawString(scoreText, x, y);

        Rectangle[] bounds = getButtonBounds(screenWidth, screenHeight);
        g.setFont(new Font("Arial", Font.BOLD, 26));

        for (int i = 0; i < items.length; i++) {
            Rectangle r = bounds[i];
            boolean isHovered = (i == hoveredIndex);

            int drawW = buttonWidth + (isHovered ? 10 : 0);
            int drawH = buttonHeight + (isHovered ? 5 : 0);
            int drawX = r.x - (isHovered ? 5 : 0);
            int drawY = r.y - (isHovered ? 3 : 0);

            if (buttonImages[i] != null) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, isHovered ? 1.0f : 0.8f));
                g2d.drawImage(buttonImages[i], drawX, drawY, drawW, drawH, null);
                g2d.dispose();
            } else {
                g.setColor(isHovered ? Color.YELLOW : Color.WHITE);
                g.fillRect(drawX, drawY, drawW, drawH);

                g.setColor(Color.BLACK);
                int textX = drawX + (drawW - fm.stringWidth(items[i])) / 2;
                int textY = drawY + (drawH - fm.getHeight()) / 2 + fm.getAscent();
                g.drawString(items[i], textX, textY);
            }

            g.setColor(isHovered ? Color.ORANGE : Color.GRAY);
            g.drawRect(drawX, drawY, drawW, drawH);
        }
    }

    private Rectangle[] getButtonBounds(int screenWidth, int screenHeight) {
        Rectangle[] bounds = new Rectangle[items.length];
        int startY = screenHeight / 2 + 100;
        for (int i = 0; i < items.length; i++) {
            int x = screenWidth / 2 - buttonWidth / 2;
            int y = startY + i * (buttonHeight + 20);
            bounds[i] = new Rectangle(x, y, buttonWidth, buttonHeight);
        }
        return bounds;
    }

    public void handleMouseMove(Point p, int screenWidth, int screenHeight) {
        Rectangle[] bounds = getButtonBounds(screenWidth, screenHeight);
        hoveredIndex = -1;
        for (int i = 0; i < bounds.length; i++) {
            if (bounds[i].contains(p)) {
                hoveredIndex = i;
                break;
            }
        }
    }

    @Override
    public String handleMouseClick(Point p, int screenWidth, int screenHeight) {
        Rectangle[] bounds = getButtonBounds(screenWidth, screenHeight);
        for (int i = 0; i < bounds.length; i++) {
            if (bounds[i].contains(p)) {
                return items[i];
            }
        }
        return null;
    }
}
