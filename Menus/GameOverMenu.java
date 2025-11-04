package Menus;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GameOverMenu implements MenuGame {
    private final String[] gameoverItems = {"REPLAY", "EXIT", "EXIT MENU"};
    private final BufferedImage[] buttonImages = new BufferedImage[gameoverItems.length];

    private final int buttonWidth = 180;
    private final int buttonHeight = 55;
    private int hoveredIndex = -1;


    public GameOverMenu() {
        try {
            buttonImages[0] = ImageIO.read(getClass().getResourceAsStream("/assets/Replay.png"));
            buttonImages[1] = ImageIO.read(getClass().getResourceAsStream("/assets/Exit.png"));
            buttonImages[2] = ImageIO.read(getClass().getResourceAsStream("/assets/ExitMenu.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Menu button image not found.");
        }
    }

    private Rectangle[] getButtonBounds(int screenWidth, int screenHeight) {
        Rectangle[] bounds = new Rectangle[gameoverItems.length];
        int startY = screenHeight / 2;
        for (int i = 0; i < gameoverItems.length; i++) {
            int x = screenWidth / 2 - buttonWidth / 2;
            int y = startY + i * (buttonHeight + 20);
            bounds[i] = new Rectangle(x, y, buttonWidth, buttonHeight);
        }
        return bounds;
    }

    @Override
    public void render(Graphics g, int screenWidth, int screenHeight) {
        Rectangle[] bounds = getButtonBounds(screenWidth, screenHeight);
        g.setFont(new Font("Arial", Font.BOLD, 26));
        for (int i = 0; i < gameoverItems.length; i++) {
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
                FontMetrics fm = g.getFontMetrics();
                int textX = drawX + (drawW - fm.stringWidth(gameoverItems[i])) / 2;
                int textY = drawY + (drawH - fm.getHeight()) / 2 + fm.getAscent();
                g.drawString(gameoverItems[i], textX, textY);
            }

            g.setColor(isHovered ? Color.ORANGE : Color.GRAY);
            g.drawRect(drawX, drawY, drawW, drawH);
        }
    }

    @Override
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
                return gameoverItems[i];
            }
        }
        return null;
    }
}
