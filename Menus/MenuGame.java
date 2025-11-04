package Menus;

import java.awt.*;

public interface MenuGame {
    void render(Graphics g, int screenWidth, int screenHeight);
    void handleMouseMove(Point p, int screenWidth, int screenHeight);
    String handleMouseClick(Point p, int screenWidth, int screenHeight);
}
