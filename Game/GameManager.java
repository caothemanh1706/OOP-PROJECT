package Game;

import Objects.Ball;
import Objects.Brick;
import Objects.Paddle;
import powerups.PowerUp;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Quản lý toàn bộ logic của game Arkanoid.
 */
public class GameManager {
    private static GameManager instance;
    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;
    private int score = 0;
    private int lives = 3;
    private String gameState;
    private Renderer renderer;

    /**
     * constructor gameManager.
     */
    public GameManager() {
        this.bricks = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.score = 0;
        this.lives = 3;
        this.gameState = "READY";
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
}
