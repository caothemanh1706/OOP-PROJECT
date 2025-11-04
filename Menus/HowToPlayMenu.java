package Menus;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class HowToPlayMenu implements MenuGame {
    private final String[] menuItems = {"BACK"};
    private final BufferedImage[] buttonImages = new BufferedImage[menuItems.length];

    private final int buttonWidth = 200;
    private final int buttonHeight = 60;
    private int hoveredIndex = -1;

    private BufferedImage bgImage;

    // Hướng dẫn chơi
    private final String[] instructions = {
            "Use LEFT / RIGHT arrows to move the paddle",
            "Bounce the ball to break all bricks",
            "Catch power-ups to gain special abilities",
            "Don't let the ball fall!"
    };

    public HowToPlayMenu() {
        try {
            buttonImages[0] = ImageIO.read(getClass().getResourceAsStream("/assets/Back.png"));
            bgImage = ImageIO.read(getClass().getResourceAsStream("/assets/Howtoplaybg.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Không thể tải ảnh nền hoặc button.");
            e.printStackTrace();
        }
    }

    private Rectangle[] getButtonBounds(int screenWidth, int screenHeight) {
        Rectangle[] bounds = new Rectangle[menuItems.length];
        int startY = screenHeight / 2 + 140; // nâng nút lên hợp lý
        for (int i = 0; i < menuItems.length; i++) {
            int x = screenWidth / 2 - buttonWidth / 2;
            int y = startY + i * (buttonHeight + 20);
            bounds[i] = new Rectangle(x, y, buttonWidth, buttonHeight);
        }
        return bounds;
    }

    @Override
    public void render(Graphics g, int screenWidth, int screenHeight) {
        Graphics2D g2 = (Graphics2D) g;

        // Vẽ background
        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, screenWidth, screenHeight, null);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, screenWidth, screenHeight);
        }


        g2.setColor(Color.WHITE);
        // Vẽ các hướng dẫn chơi
        g2.setFont(new Font("Arial", Font.PLAIN, 24));
        int startY = screenHeight / 4 + 90; // tăng từ 60 lên 120 để hạ chữ xuống
        int lineSpacing = 50; // khoảng cách giữa các dòng
        for (int i = 0; i < instructions.length; i++) {
            String line = instructions[i];
            int lineX = (screenWidth - g2.getFontMetrics().stringWidth(line)) / 2;
            g2.drawString(line, lineX, startY + i * lineSpacing);
        }

        // Vẽ nút BACK
        Rectangle[] bounds = getButtonBounds(screenWidth, screenHeight);
        g2.setFont(new Font("Arial", Font.BOLD, 26));
        for (int i = 0; i < menuItems.length; i++) {
            Rectangle r = bounds[i];
            boolean isHovered = (i == hoveredIndex);

            int drawW = buttonWidth + (isHovered ? 10 : 0);
            int drawH = buttonHeight + (isHovered ? 5 : 0);
            int drawX = r.x - (isHovered ? 5 : 0);
            int drawY = r.y - (isHovered ? 3 : 0);

            if (buttonImages[i] != null) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, isHovered ? 1.0f : 0.85f));
                g2.drawImage(buttonImages[i], drawX, drawY, drawW, drawH, null);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            } else {
                g2.setColor(isHovered ? Color.YELLOW : Color.WHITE);
                g2.fillRect(drawX, drawY, drawW, drawH);
                g2.setColor(Color.BLACK);
                FontMetrics fm = g2.getFontMetrics();
                int textX = drawX + (drawW - fm.stringWidth(menuItems[i])) / 2;
                int textY = drawY + (drawH - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(menuItems[i], textX, textY);
            }

            g2.setColor(isHovered ? Color.ORANGE : Color.GRAY);
            g2.drawRect(drawX, drawY, drawW, drawH);
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
            if (bounds[i].contains(p)) return menuItems[i];
        }
        return null;
    }
}
