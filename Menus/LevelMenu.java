package Menus;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LevelMenu implements MenuGame {

    private final String[] menuItems = {"LEVEL 1", "LEVEL 2", "LEVEL 3", "LEVEL 4", "LEVEL 5", "BACK"};
    private final BufferedImage[] buttonImages = new BufferedImage[menuItems.length];

    private final int buttonWidth = 160;
    private final int buttonHeight = 80;
    private int hoveredIndex = -1;

    private BufferedImage bgImage;

    public LevelMenu() {
        try {
            // Load background
            bgImage = ImageIO.read(getClass().getResourceAsStream("/assets/Levelsbg.png"));

            // Load level buttons
            for (int i = 0; i < 5; i++) {
                String path = String.format("/assets/level%02d.png", i + 1);
                try {
                    buttonImages[i] = ImageIO.read(getClass().getResourceAsStream(path));
                } catch (Exception e) {
                    System.err.println("Không tìm thấy ảnh cho " + menuItems[i] + ", dùng fallback.");
                    buttonImages[i] = null;
                }
            }

            // Load BACK button
            buttonImages[5] = ImageIO.read(getClass().getResourceAsStream("/assets/Back.png"));

        } catch (IOException e) {
            System.err.println("Không thể tải ảnh LevelMenu:");
            e.printStackTrace();
        }
    }

    private Rectangle[] getButtonBounds(int screenWidth, int screenHeight) {
        Rectangle[] bounds = new Rectangle[menuItems.length];
        int[] rowCounts = {1, 2, 2, 1};
        int rowSpacing = 20;
        int colSpacing = 15;

        int totalHeight = rowCounts.length * buttonHeight + (rowCounts.length - 1) * rowSpacing;

        int startY = (screenHeight - totalHeight) / 2 + 60; // dịch xuống 50px

        int index = 0;
        for (int row = 0; row < rowCounts.length; row++) {
            int count = rowCounts[row];
            int y = startY + row * (buttonHeight + rowSpacing);

            int totalRowWidth = count * buttonWidth + (count - 1) * colSpacing;
            int startX = (screenWidth - totalRowWidth) / 2;

            for (int col = 0; col < count; col++) {
                if (index >= menuItems.length) break;
                int x = startX + col * (buttonWidth + colSpacing);
                bounds[index++] = new Rectangle(x, y, buttonWidth, buttonHeight);
            }
        }

        return bounds;
    }

    @Override
    public void render(Graphics g, int screenWidth, int screenHeight) {
        Graphics2D g2 = (Graphics2D) g;

        // --- Vẽ background ---
        if (bgImage != null)
            g2.drawImage(bgImage, 0, 0, screenWidth, screenHeight, null);
        else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, screenWidth, screenHeight);
        }

        // --- Nút ---
        Rectangle[] bounds = getButtonBounds(screenWidth, screenHeight);

        for (int i = 0; i < menuItems.length; i++) {
            Rectangle r = bounds[i];
            boolean isHovered = (i == hoveredIndex);

            int drawW = buttonWidth + (isHovered ? 12 : 0);
            int drawH = buttonHeight + (isHovered ? 8 : 0);
            int drawX = r.x - (isHovered ? 6 : 0);
            int drawY = r.y - (isHovered ? 4 : 0);

            if (buttonImages[i] != null) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, isHovered ? 1f : 0.9f));
                g2.drawImage(buttonImages[i], drawX, drawY, drawW, drawH, null);
            } else {
                // Nếu không có ảnh nút thì vẽ hình chữ nhật cơ bản
                g2.setColor(isHovered ? Color.YELLOW : Color.LIGHT_GRAY);
                g2.fillRoundRect(drawX, drawY, drawW, drawH, 20, 20);
            }

            // Border
            g2.setColor(isHovered ? Color.ORANGE : new Color(100, 100, 100));
            g2.drawRoundRect(drawX, drawY, drawW, drawH, 20, 20);
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
                if (i == 5) return "BACK";
                return "LEVEL " + (i + 1); // ví dụ: LEVEL_1, LEVEL_2, ...
            }
        }
        return null;
    }
}
