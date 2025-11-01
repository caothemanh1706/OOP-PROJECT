package Game;

import Objects.*;
import javax.swing.*;
import java.awt.*;
public class Main {
    public static final int ScreenWidth = 800;
    public static final int ScreenHeight = 600;
    public static void main(String[] args) {
        JFrame frame = new JFrame("Arkanoid");
        Renderer renderer = new Renderer();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GameManager manager = GameManager.getInstance();
        frame.setSize(ScreenWidth,ScreenHeight);
        frame.setResizable(false);
        frame.add(renderer);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Timer timer = new Timer(16, e -> {
            manager.updateGame();
            renderer.repaint();
        });
        timer.start();
    }
}
