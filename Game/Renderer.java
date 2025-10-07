package Game;

import Objects.Ball;
import Objects.GameObjects;
import Objects.Paddle;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Renderer extends JPanel {
    private final List<GameObjects> gameObjects = new ArrayList<>();

    /**
     * constructor 1 .
     * setBackground color back.
     */
    public Renderer() {
        setBackground(Color.BLACK);

        Paddle paddle = new Paddle(350, 500, 100, 20, 0, 0, 5);
        gameObjects.add(paddle);

        Ball ball = new Ball (380, 460, 20, 20, 0,0 ,0f, 0f, 0f);
        gameObjects.add(ball);
    }

    /**
     * Add a new game object to the list.
     */
    public void addGameObject(GameObjects obj) {
        gameObjects.add(obj);

    }

    /**
     * Draw object.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (GameObjects obj : gameObjects) {
            obj.render(g);
        }
    }

}
