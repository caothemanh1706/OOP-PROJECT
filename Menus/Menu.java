package Menus;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Menu implements MenuGame {
    private final String[] menuItems = {
            "HIGH SCORE",
            "PLAY",
            "LEVELS",
            "HOW TO PLAY",
            "EXIT"
    };

    private final BufferedImage[] buttonImages = new BufferedImage[menuItems.length];
    private final int buttonWidth = 150;
    private final int buttonHeight = 50;
    private int hoveredIndex = -1;

    private boolean showingHighScore = false;

    public Menu() {
        try {
            buttonImages[0] = ImageIO.read(getClass().getResourceAsStream("/assets/HighScore.png"));
            buttonImages[1] = ImageIO.read(getClass().getResourceAsStream("/assets/Play.png"));
            buttonImages[2] = ImageIO.read(getClass().getResourceAsStream("/assets/levels.png"));
            buttonImages[3] = ImageIO.read(getClass().getResourceAsStream("/assets/howtoplay.png"));
            buttonImages[4] = ImageIO.read(getClass().getResourceAsStream("/assets/Exit.png"));
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

    @Override
    public void render(Graphics g, int screenWidth, int screenHeight) {
        Rectangle[] bounds = getButtonBounds(screenWidth, screenHeight);

        for (int i = 0; i < menuItems.length; i++) {
            Rectangle rect = bounds[i];
            boolean isHovered = (i == hoveredIndex);

            int drawW = buttonWidth + (isHovered ? 10 : 0);
            int drawH = buttonHeight + (isHovered ? 5 : 0);
            int drawX = rect.x - (isHovered ? 5 : 0);
            int drawY = rect.y - (isHovered ? 3 : 0);

            if (buttonImages[i] != null) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, isHovered ? 1.0f : 0.8f));
                g2d.drawImage(buttonImages[i], drawX, drawY, drawW, drawH, null);
                g2d.dispose();
            } else {
                g.setColor(isHovered ? Color.YELLOW : Color.WHITE);
                g.fillRect(drawX, drawY, drawW, drawH);
                FontMetrics fm = g.getFontMetrics();
                int textX = drawX + (drawW - fm.stringWidth(menuItems[i])) / 2;
                int textY = drawY + (drawH - fm.getHeight()) / 2 + fm.getAscent();
                g.setColor(Color.BLACK);
                g.drawString(menuItems[i], textX, textY);
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
                try {
                } catch (Exception ignored) {}

                switch (i) {
                    case 0 -> {
                        showingHighScore = true;
                        System.out.println("HIGH SCORE clicked!");
                    }
                    case 1 -> {
                        System.out.println("PLAY clicked!");
                    }
                    case 2 -> {
                        System.out.println("LEVELS clicked!");
                    }
                    case 3 -> {
                        return "HOWTOPLAY";
                    }
                    case 4 -> {
                        System.out.println(" EXIT clicked!");
                        System.exit(0);
                    }
                }
                return menuItems[i];
            }
        }
        return null;
    }
}
