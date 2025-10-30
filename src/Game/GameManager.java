package Game;

import Levels.Level;
import Objects.Ball;
import Objects.Brick;
import Objects.ExplosiveBrick;
import Objects.Paddle;
import powerups.ExpandPaddlePowerUp;
import powerups.PowerUp;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/** Manage all the game logic. */
public class GameManager {
    public static final String STATE_MENU = "MENU";
    public static final String STATE_READY = "READY";
    public static final String STATE_PLAYING = "PLAYING";
    public static final String STATE_GAME_OVER = "GAME_OVER";
    public static final String STATE_WON = "WON";

    private static GameManager instance;
    private Paddle paddle;
    private Ball ball;
    public List<Brick> bricks;
    private Level currentLevel;
    private int currentLevelIndex;
    private List<PowerUp> powerUps = new ArrayList<>();
    private Random random = new Random();
    private int score = 0;
    private int lives = 3;
    private String gameState;
    private Renderer renderer;
    private boolean menuSoundPlayed = false;

    /** Returns the singleton instance of GameManager. */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /** Constructor gameManager. */
    public GameManager() {
        this.bricks = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.score = 0;
        this.lives = 3;
        this.gameState = STATE_MENU;
    }

    // ───────────────────────────────────────────────
    //                INITIALIZATION
    // ───────────────────────────────────────────────

    /** Starts the game and initializes paddle, ball, and renderer. */
    public void startGame(Renderer renderer, Paddle paddle, Ball ball) {
        SoundManager.init();
        this.renderer = renderer;
        this.paddle = paddle;
        this.ball = ball;
        this.gameState = STATE_MENU;

        SoundManager.playMusic();

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

    /** Loads a specific level into the game. */
    public void loadLevel(Level level, Renderer renderer) {
        this.currentLevel = level;
        this.bricks.clear();
        level.loadLevel(renderer, bricks);
    }

    // ───────────────────────────────────────────────
    //                INPUT HANDLING
    // ───────────────────────────────────────────────

    /** Handles keyboard inputs for paddle movement and game start. */
    public void handleInput(int keyCode, boolean pressed) {
        if (gameState.equals(STATE_MENU)) {
            return;
        }

        if (!pressed) {
            paddle.stop();
            return;
        }

        switch (keyCode) {
            case KeyEvent.VK_LEFT -> {
                if (gameState.equals(STATE_PLAYING) || gameState.equals(STATE_READY)) {
                    paddle.moveLeft();
                }
            }
            case KeyEvent.VK_RIGHT -> {
                if (gameState.equals(STATE_PLAYING) || gameState.equals(STATE_READY)) {
                    paddle.moveRight();
                }
            }
            case KeyEvent.VK_SPACE -> {
                if (gameState.equals(STATE_READY)) {
                    gameState = STATE_PLAYING;
                    ball.setDirectionX(0f);
                    ball.setDirectionY(-1f);
                }
            }
        }
    }

    /** Handles mouse clicks in menu for start, high score, and exit buttons. */
    public void handleMouseClick(int mouseX, int mouseY) {
        if (gameState.equals(STATE_MENU)) {
            Menu menu = renderer.getGameMenu();
            Rectangle[] buttonBounds = menu.getButtonBounds(renderer.getWidth(), renderer.getHeight());

            if (buttonBounds[1].contains(mouseX, mouseY)) {
                if (currentLevel == null) {
                    currentLevelIndex = 2;
                    currentLevel = new Levels.Level2();
                }

                SoundManager.stopMenuMusic();

                loadLevel(currentLevel, renderer);
                gameState = STATE_READY;
                System.out.println("Game ready to start");
            } else if (buttonBounds[0].contains(mouseX, mouseY)) {
                System.out.println("Display High Score (TBD)");
            } else if (buttonBounds[2].contains(mouseX, mouseY)) {
                System.exit(0);
            }
        }
    }

    // ───────────────────────────────────────────────
    //                GAME LOOP
    // ───────────────────────────────────────────────

    /** Updates all game objects depending on current state. */
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
        }
        Iterator<PowerUp> iterator = powerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp p = iterator.next();
            p.update();

            if (p.getY() > 600) {
                iterator.remove();
                continue;
            }

            if (p.getBounds().intersects(paddle.getBounds())) {
                SoundManager.playSound("power_up_paddle.wav");
                p.activate(paddle);
                iterator.remove();
            }
        }
    }

    // ───────────────────────────────────────────────
    //                COLLISION & LEVEL LOGIC
    // ───────────────────────────────────────────────

    /** Checks and handles all collisions between ball, paddle, and bricks. */
    public void checkCollisions() {
        List<Brick> bricksToRemove = new ArrayList<>();

        // Ball vs Paddle
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

                SoundManager.playSound("paddle_hit.wav");
            }
        }

        // Ball vs Bricks
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && ball.getBounds().intersects(brick.getBounds())) {

                Rectangle ballRect = ball.getBounds();
                Rectangle brickRect = brick.getBounds();

                // Calculate the overlap between the shadow and the tile
                float overlapLeft = ballRect.x + ballRect.width - brickRect.x;
                float overlapRight = brickRect.x + brickRect.width - ballRect.x;
                float overlapTop = ballRect.y + ballRect.height - brickRect.y;
                float overlapBottom = brickRect.y + brickRect.height - ballRect.y;

                // Determine the collision direction (the direction with the smaller overlap)
                float minOverlapX = Math.min(overlapLeft, overlapRight);
                float minOverlapY = Math.min(overlapTop, overlapBottom);

                if (minOverlapX < minOverlapY) {
                    // Hit on the left or right
                    ball.bounceX();

                    // Kick the ball out of the tiles to avoid getting stuck
                    if (overlapLeft < overlapRight) {
                        ball.setX(brick.getX() - ball.getWidth() - 1);
                    } else {
                        ball.setX(brick.getX() + brick.getWidth() + 1);
                    }
                } else {
                    // Hit on top or bottom
                    ball.bounceY();

                    // Kick the ball out of bounds to avoid congestion
                    if (overlapTop < overlapBottom) {
                        ball.setY(brick.getY() - ball.getHeight() - 1);
                    } else {
                        ball.setY(brick.getY() + brick.getHeight() + 1);
                    }
                }

                // Brick handling (explosions, points, power-ups...)
                if (brick instanceof ExplosiveBrick) {
                    SoundManager.playSound("explosion.wav");
                    ((ExplosiveBrick) brick).takeHit(bricks);
                } else {
                    brick.takeHit();
                    SoundManager.playSound("brick_hit.wav");
                }

                if (brick.isDestroyed()) {
                    bricksToRemove.add(brick);
                    score += 20;

                    if (currentLevel != null && currentLevel.hasPowerUp()) {
                        if (random.nextFloat() < 0.2f) {
                            PowerUp p = new ExpandPaddlePowerUp(
                                    brick.getX() + brick.getWidth() / 2f,
                                    brick.getY() + brick.getHeight()
                            );
                            powerUps.add(p);
                        }
                    }
                }
                // Process only 1 tile per frame
                break;
            }
        }

        bricks.removeAll(bricksToRemove);

        // Ball out of bounds
        if (ball.getY() + ball.getHeight() >= paddle.getY() + 36) {
            SoundManager.playSound("game_lost.wav");
            lives--;
            if (lives == 0) {
                gameOver();
            } else {
                ball.resetPosition();
                ball.setDirectionX(0f);
                ball.setDirectionY(-1f);
                gameState = STATE_READY;
            }
        }

        // Level cleared
        if (bricks.isEmpty()) {
            System.out.println(" Level " + currentLevelIndex + " completed!");

            currentLevelIndex++;

            if (currentLevelIndex <= 2) {
                System.out.println("Loading Level " + currentLevelIndex + "...");
                loadLevelFromFile(currentLevelIndex);

                paddle.setX((renderer.getWidth() - paddle.getWidth()) / 2f);
                ball.resetPosition();

                gameState = STATE_READY;
            } else {
                gameState = STATE_WON;
                System.out.println(" Congratulations! You completed all levels!");
            }
        }
    }

    /** Loads a level by reading its text file. */
    private void loadLevelFromFile(int levelNumber) {
        bricks.clear();

        currentLevel = new Levels.Level(100, 50, 64, 32, 4) {
            @Override
            public void loadLevel(Renderer renderer, List<Brick> bricks) {
                Levels.LevelLoader.loadFromFile(
                        "level" + levelNumber + ".txt",
                        renderer, bricks, startX, startY, brickWidth, brickHeight, spacing
                );
            }
        };

        currentLevel.loadLevel(renderer, bricks);
    }

    // ───────────────────────────────────────────────
    //                GAME STATE METHODS
    // ───────────────────────────────────────────────

    /** Called when player loses all lives. */
    public void gameOver() {
        gameState = STATE_GAME_OVER;
        System.out.println("Game Over! Final score: " + score);
    }

    // ───────────────────────────────────────────────
    //                GETTERS
    // ───────────────────────────────────────────────

    public List<Brick> getBricks() {
        return bricks;
    }
    public Paddle getPaddle() {
        return paddle;
    }
    public List<PowerUp> getPowerUps() {
        return powerUps;
    }
    public int getLives() {
        return lives;
    }
    public int getScore() {
        return score;
    }
    public String getGameState() {
        return gameState;
    }
}
