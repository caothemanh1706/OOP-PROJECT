package Game;

import Objects.*;
import types.BrickType;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Arkanoid");
        Renderer renderer = new Renderer();
        int ScreenHeight = 600;
        int ScreenWidth = 800;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameManager manager = GameManager.getInstance();

        frame.setSize(ScreenWidth,ScreenHeight);
        frame.setResizable(false);
        frame.add(renderer);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        // Game loop ~60 FPS
        while (true) {
            manager.updateGame();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
