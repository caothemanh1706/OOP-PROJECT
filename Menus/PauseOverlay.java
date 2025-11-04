package Menus;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

public class PauseOverlay {
    private final Map<String, Rectangle2D> buttonBounds = new HashMap<>();
    private final String[] labels = {"CONTINUE", "RESTART", "BACK"};

    public PauseOverlay() {
    }

    public void render(Graphics g, int screenWidth, int screenHeight) {
        g.setColor(new Color(0, 0, 0, 180)); // Màu đen mờ (opacity 180/255)
        g.fillRect(0, 0, screenWidth, screenHeight);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));

        String title = "GAME PAUSED";
        int titleX = (screenWidth - g.getFontMetrics().stringWidth(title)) / 2;
        g.drawString(title, titleX, screenHeight / 2 - 150);

        int buttonWidth = 200;
        int buttonHeight = 50;
        int startY = screenHeight / 2 - 50;
        int spacing = 80;

        buttonBounds.clear();

        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            int buttonX = (screenWidth - buttonWidth) / 2;
            int buttonY = startY + i * spacing;

            g.setColor(new Color(50, 50, 50));
            g.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);

            g.setColor(Color.WHITE);
            g.drawRect(buttonX, buttonY, buttonWidth, buttonHeight);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            FontMetrics fm = g.getFontMetrics();
            int textX = buttonX + (buttonWidth - fm.stringWidth(label)) / 2;
            int textY = buttonY + (buttonHeight - fm.getHeight()) / 2 + fm.getAscent();
            g.drawString(label, textX, textY);

            buttonBounds.put(label, new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight));
        }
    }

    public String handleMouseClick(Point p) {
        for (Map.Entry<String, Rectangle2D> entry : buttonBounds.entrySet()) {
            if (entry.getValue().contains(p)) {
                return entry.getKey();
            }
        }
        return null;
    }
}

