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
        int startX = 50;
        int y = 100;
        int brickWidth = 60;
        int brickHeight = 20;
        int spacing = 10;

        GameManager manager = GameManager.getInstance();
        for (int i = 0; i < 10; i++) {
            int x = startX + i * (brickWidth + spacing);
            Brick brick;
            if (i % 2 == 0) {
                brick = new NormalBrick(x, y, brickWidth, brickHeight);
            } else {
                brick = new StrongBrick(x, y, brickWidth, brickHeight);
            }
            renderer.addGameObject(brick);
            manager.getBricks().add(brick);
        }
        frame.setSize(ScreenWidth,ScreenHeight);
        frame.setResizable(false);
        frame.add(renderer);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        // Game loop ~60 FPS
        while (true) {
            manager.updateGame();
            try {
                Thread.sleep(16); // 1000ms / 60fps ≈ 16ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
