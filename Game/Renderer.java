package Game;

import Objects.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Renderer extends JPanel {
    private final List<GameObjects> gameObjects = new ArrayList<>();
    private BufferedImage background;
    private Paddle paddle;
    private Ball ball;

    public Renderer() {
        try {
            background = ImageIO.read(getClass().getResourceAsStream("/assets/background1.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Background not found.");
            background = null;
        }

        setBackground(Color.BLACK);
        paddle = new Paddle(350, 500, 100, 20, 0, 0, 5);
        gameObjects.add(paddle);

        ball = new Ball(paddle.getX() + paddle.getWidth() / 2 - 10,
                paddle.getY() - 20 - 5,
                20, 20, 0, 0, 0f, 0f, 5f,
                800, 600, "/assets/ball.png"
         );

        gameObjects.add(ball);
        GameManager.getInstance().startGame(this, paddle, ball);
    }

    public void addGameObject(GameObjects obj) {
        gameObjects.add(obj);
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public Ball getBall() {
        return ball;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        }

        for (GameObjects obj : gameObjects) {
            obj.render(g);
        }
    }

    public void clearBricks() {
        gameObjects.removeIf(obj -> obj instanceof Objects.Brick);
    }
}
