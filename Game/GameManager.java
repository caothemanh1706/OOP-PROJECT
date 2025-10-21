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

    public void startGame(Renderer renderer, Paddle paddle, Ball ball) {
        this.renderer = renderer;
        this.paddle = paddle;
        this.ball = ball;
        this.gameState = "READY";

        renderer.setFocusable(true);
        renderer.requestFocusInWindow();

        renderer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleInput(e.getKeyCode(), true);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleInput(e.getKeyCode(), false);
            }
        });
    }

    public void updateGame() {
        if (gameState.equals("PLAYING")) {
            paddle.update();
            ball.update();
            checkCollisions();
            renderer.repaint();
        } else if (gameState.equals("READY")) {
            ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2);
            ball.setY(paddle.getY() - ball.getHeight() - 5);
            paddle.update();
            renderer.repaint();
            System.out.println("Game in READY state, press SPACE to start.");
        }
    }

    public void handleInput(int keyCode, boolean pressed) {
        if (!pressed) {
            paddle.stop();
            return;
        }

        switch (keyCode) {
            case KeyEvent.VK_LEFT -> paddle.moveLeft();
            case KeyEvent.VK_RIGHT -> paddle.moveRight();
            case KeyEvent.VK_SPACE -> {
                if (gameState.equals("READY")) {
                    gameState = "PLAYING";
                    ball.setDirectionX(0f);
                    ball.setDirectionY(-1f);
                    System.out.println("Game started");
                }
            }
        }
    }

    /**
     * Check for physical collision with the brick and paddle.
     */
    public void checkCollisions() {
        List<Brick> bricksToRemove = new ArrayList<>();

        if (gameState.equals("PLAYING") && ball.getBounds().intersects(paddle.getBounds())) {
            if (ball.getDirectionY() > 0f) {
                float ballCenterX = ball.getX() + ball.getWidth() / 2f;
                float paddleCenterX = paddle.getX() + paddle.getWidth() / 2f;
                float relative = (ballCenterX - paddleCenterX) / (paddle.getWidth() / 2f);
                relative = Math.max(-1f, Math.min(1f, relative));

                float maxAngle = (float) Math.toRadians(45);
                float angle = relative * maxAngle;

                float newDirX = (float) Math.sin(angle);
                float newDirY = (float) -Math.cos(angle);

                ball.setDirectionX(newDirX);
                ball.setDirectionY(newDirY);

                ball.setY(paddle.getY() - ball.getHeight() - 1);
            }
        }

        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && ball.getBounds().intersects(brick.getBounds())) {
                ball.bounceY();

                if (brick instanceof ExplosiveBrick) {
                    ((ExplosiveBrick) brick).takeHit(bricks);
                } else {
                    brick.takeHit();
                }

                if (brick.isDestroyed()) {
                    bricksToRemove.add(brick);
                    score += 20;
                }
                break;
            }
        }

        bricks.removeAll(bricksToRemove);

        if (ball.getY() + ball.getHeight() >= paddle.getY() + 36) {
            lives--;
            if (lives == 0) {
                gameOver();
            } else {
                ball.resetPosition();
                ball.setDirectionX(0f);
                ball.setDirectionY(-1f);
                gameState = "PLAYING";
            }
        }

        if (bricks.isEmpty()) {
            gameState = "WON";
            System.out.println("Congratulations! You won! Final score: " + score);
        }
    }


    public void gameOver() {
        gameState = "GAME_OVER";
        System.out.println("Game Over! Final score: " + score);
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public Paddle getPaddle() {
        return paddle;
    }
}
