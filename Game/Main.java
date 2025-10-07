package Game;

import Objects.Brick;
import Objects.NormalBrick;
import Objects.Paddle;
import Objects.StrongBrick;
import types.BrickType;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Arkanoid");
        Renderer renderer = new Renderer();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int startX = 50;
        int y = 100;
        int brickWidth = 60;
        int brickHeight = 20;
        int spacing = 10;

        for (int i = 0; i < 10; i++) {
            int x = startX + i * (brickWidth + spacing);
            if (i % 2 == 0) {
                renderer.addGameObject(new NormalBrick(x, y, brickWidth, brickHeight));
            } else {
                renderer.addGameObject(new StrongBrick(x, y, brickWidth, brickHeight));
            }
        }

        frame.setSize(800,600);
        frame.setResizable(false);
        frame.add(renderer);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
