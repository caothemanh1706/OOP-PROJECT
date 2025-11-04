package Game;

import Levels.Level;
import Levels.LevelLoader;
import Objects.*;
import powerups.*;
import Menus.Menu;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Quản lý toàn bộ logic của game Arkanoid.
 */
public class GameManager {

    // ==== SINGLETON ====
    private static GameManager instance;
    private boolean isCustomLevel = false;

    // ==== GAME OBJECTS ====
    private Paddle paddle;
    private Renderer renderer;

    private final List<Brick> bricks = new ArrayList<>();
    private final List<Ball> balls = new ArrayList<>();
    private final List<PowerUp> powerUps = new ArrayList<>();

    private Level currentLevel;
    private int lastLevelNumber = 1;
    private int currentLevelIndex = 1;
    private static final int MAX_LEVELS = 5;

    // ==== GAME DATA ====
    private final Random random = new Random();
    private int score = 0;
    private int lives = 3;
    private GameState gameState;

    // ==== GETTERS ====
    public List<PowerUp> getPowerUps() { return powerUps; }
    public List<Ball> getBalls() { return balls; }
    public GameState getGameState() { return gameState; }
    public List<Brick> getBricks() { return bricks; }
    public Paddle getPaddle() { return paddle; }
    public int getLives() { return lives; }
    public int getScore() { return score; }
    public List<ExplosionEffect> getExplosions() { return explosions; }

    // ==== SINGLETON ACCESS ====
    public static GameManager getInstance() {
        if (instance == null) instance = new GameManager();
        return instance;
    }

    // ==== CONSTRUCTOR ====
    public GameManager() {
        this.score = 0;
        this.lives = 3;
        this.gameState = GameState.MENU;
    }

    // ==== CORE GAME SETUP ====
    public void startGame(Renderer renderer, Paddle paddle, Ball ball) {
        SoundManager.init();
        this.renderer = renderer;
        this.paddle = paddle;

        balls.clear();
        balls.add(ball);
        this.gameState = GameState.MENU;

        SoundManager.playMusic();

        renderer.setFocusable(true);
        renderer.requestFocusInWindow();

        renderer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) { handleInput(e.getKeyCode(), true); }
            @Override
            public void keyReleased(KeyEvent e) { handleInput(e.getKeyCode(), false); }
        });
    }

    // ==== LEVEL MANAGEMENT ====
    /** Load a level from file */
    public void loadLevelFromFile(int levelNumber, boolean custom) {
        if (renderer != null) renderer.resetRenderer();
        this.bricks.clear();

        isCustomLevel = custom;
        currentLevelIndex = levelNumber;
        currentLevel = new Level(0, 0, 60, 25, 0) {
            @Override
            public void loadLevel(Renderer renderer, List<Brick> bricks) {
                LevelLoader.loadFromFile(
                        "level" + levelNumber + ".txt",
                        renderer, bricks, startX, startY, brickWidth, brickHeight, spacing
                );
            }
        };
        lastLevelNumber = levelNumber;
        currentLevel.loadLevel(renderer, bricks);
    }

    /** Check if level cleared */
    private boolean checkLevelCleared() {
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && !(brick instanceof UnbreakableBrick)) return false;
        }
        return true;
    }

    /** Handle level progression */
    private void handleLevelProgression() {
        if (checkLevelCleared()) {
            if (isCustomLevel) {
                gameState = GameState.WON;  //  nếu là level chọn thủ công thì thắng luôn
                System.out.println("You won the selected level!");
                renderer.repaint();
                return;
            }
            if (currentLevelIndex < MAX_LEVELS) {
                currentLevelIndex++;
                System.out.println("Level cleared! Loading level " + currentLevelIndex + "...");
                loadLevelFromFile(currentLevelIndex,false);
                resetAfterLevel();
            } else {
                gameState = GameState.WON;
                System.out.println("Congratulations! You completed all levels!");
                renderer.repaint();
            }
        }
    }

    /** Reset balls, paddle, power-ups after level cleared */
    private void resetAfterLevel() {
        powerUps.clear();
        balls.clear();
        Ball newBall = new Ball(
                paddle.getX() + paddle.getWidth() / 2f - 10,
                paddle.getY() - 25,
                20, 20, 0, 0, 0f, 0f, 6f,
                renderer.getWidth(), renderer.getHeight(),
                "/assets/ball.png"
        );
        balls.add(newBall);
        paddle.setX((renderer.getWidth() - paddle.getWidth()) / 2f);
        gameState = GameState.READY;
    }

    // ==== INPUT HANDLING ====
    public void handleMouseClick(int mouseX, int mouseY) {
        if (gameState != GameState.MENU) return;

        Menu menu = renderer.getGameMenu();
        Rectangle[] buttonBounds = menu.getButtonBounds(renderer.getWidth(), renderer.getHeight());

        if (buttonBounds[1].contains(mouseX, mouseY)) { // PLAY
            if (currentLevel == null) {
                currentLevelIndex = 1;
                loadLevelFromFile(currentLevelIndex,false);
            }
            SoundManager.stopMenuMusic();
            gameState = GameState.READY;
            System.out.println("Game ready to start");

        } else if (buttonBounds[0].contains(mouseX, mouseY)) { // HIGHSCORE
            gameState = GameState.HIGHSCORE;

        } else if (buttonBounds[2].contains(mouseX, mouseY)) { // LEVELS
            gameState = GameState.LEVELS;
            System.out.println("Switched to Level Select Menu");

        } else if (buttonBounds[3].contains(mouseX, mouseY)) { // EXIT → HOW TO PLAY
            gameState = GameState.HOWTOPLAY;
            System.out.println("Switched to How To Play menu (via EXIT button)");

        } else if (buttonBounds.length > 4 && buttonBounds[4].contains(mouseX, mouseY)) { // HOW TO PLAY
            System.exit(0);
        }

    }


    public void handleInput(int keyCode, boolean pressed) {
        if (gameState == GameState.MENU) return;
        if (!pressed) { paddle.stop(); return; }

        switch (keyCode) {
            case KeyEvent.VK_LEFT -> {
                if (gameState == GameState.PLAYING || gameState == GameState.READY) paddle.moveLeft();
            }
            case KeyEvent.VK_RIGHT -> {
                if (gameState == GameState.PLAYING || gameState == GameState.READY) paddle.moveRight();
            }
            case KeyEvent.VK_SPACE -> {
                if (gameState == GameState.READY) {
                    gameState = GameState.PLAYING;
                    balls.get(0).setDirectionX(0f);
                    balls.get(0).setDirectionY(-1f);
                }
            }
        }
    }

    // ==== GAME LOOP ====
    public void updateGame() {
        if (gameState == GameState.PLAYING) {
            paddle.update();
            for (Ball b : balls) b.update();
            checkCollisions();
        }

        if (gameState == GameState.PAUSED || gameState == GameState.GAME_OVER || gameState == GameState.WON) {
            renderer.repaint();
            return;
        }

        if (gameState == GameState.READY) {
            Ball mainBall = balls.get(0);
            mainBall.setX(paddle.getX() + paddle.getWidth() / 2 - mainBall.getWidth() / 2);
            mainBall.setY(paddle.getY() - mainBall.getHeight() - 5);
            paddle.update();
            renderer.repaint();
        }

        // Power-ups falling
        Iterator<PowerUp> iterator = powerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp p = iterator.next();
            p.update();
            if (p.getY() > 600) { iterator.remove(); continue; }
            if (p.getBounds().intersects(paddle.getBounds())) {
                SoundManager.playSound("power_up_paddle.wav");
                p.activate(paddle);
                iterator.remove();
            }
        }
    }

    // ==== COLLISIONS ====
    public void checkCollisions() {
        List<Brick> bricksToRemove = new ArrayList<>();

        for (Ball ball : balls) {
            // Paddle collision
            if (ball.getBounds().intersects(paddle.getBounds()) ) {
                float ballCenterX = ball.getX() + ball.getWidth() / 2f;
                float paddleCenterX = paddle.getX() + paddle.getWidth() / 2f;
                float relative = (ballCenterX - paddleCenterX) / (paddle.getWidth() / 2f);
                relative = Math.max(-1f, Math.min(1f, relative));
                float angle = relative * (float) Math.toRadians(45);
                ball.setDirectionX((float) Math.sin(angle));
                ball.setDirectionY((float) -Math.cos(angle));
                ball.setY(paddle.getY() - ball.getHeight() - 1);
                SoundManager.playSound("paddle_hit.wav");
            }

            // Brick collision
            for (Brick brick : bricks) {
                if (!brick.isDestroyed() && ball.getBounds().intersects(brick.getBounds())) {
                    Rectangle brickBounds = brick.getBounds();
                    Rectangle ballBounds = ball.getBounds();

                    float ballCenterX = ball.getX() + ball.getWidth() / 2f;
                    float ballCenterY = ball.getY() + ball.getHeight() / 2f;

                    float brickCenterX = brickBounds.x + brickBounds.width / 2f;
                    float brickCenterY = brickBounds.y + brickBounds.height / 2f;

                    float distanceX = Math.abs(ballCenterX - brickCenterX);
                    float distanceY = Math.abs(ballCenterY - brickCenterY);

                    float minDistanceX = ball.getWidth() / 2f + brickBounds.width / 2f;
                    float minDistanceY = ball.getHeight() / 2f + brickBounds.height / 2f;

                    float overlapX = minDistanceX - distanceX;
                    float overlapY = minDistanceY - distanceY;

                    boolean collisionX = false;
                    boolean collisionY = false;

                    if (overlapX > 0 && overlapY > 0) {
                        if (overlapX < overlapY) {
                            collisionX = true;
                        } else {
                            collisionY = true;
                        }
                    }

                    if (collisionY) {
                        if (ballCenterY < brickCenterY) {
                            ball.setY(brickBounds.y - ball.getHeight() - 1);
                        } else {
                            ball.setY(brickBounds.y + brickBounds.height + 1);
                        }
                        ball.bounceY();
                    } else if (collisionX) {
                        if (ballCenterX < brickCenterX) {
                            ball.setX(brickBounds.x - ball.getWidth() - 1);
                        } else {
                            ball.setX(brickBounds.x + brickBounds.width + 1);
                        }
                        ball.bounceX();
                    } else {
                        if (ball.getDirectionY() < 0) {
                            ball.setY(brickBounds.y + brickBounds.height + 1);
                        } else {
                            ball.setY(brickBounds.y - ball.getHeight() - 1);
                        }
                        ball.bounceY();
                    }

                    if (brick.getClass().getSimpleName().equals("UnbreakableBrick")) {
                        SoundManager.playSound("paddle_hit.wav");
                    } else if (brick instanceof ExplosiveBrick) {
                        SoundManager.playSound("explosion.wav");
                        ((ExplosiveBrick) brick).takeHit(bricks);
                    } else {
                        brick.takeHit();
                        SoundManager.playSound("brick_hit.wav");
                    }

                    if (brick.isDestroyed()) {
                        if (brick instanceof ExplosiveBrick) {
                            int explosionSize = 160;
                            float offset = 80f;

                            ExplosionEffect newExplosion = new ExplosionEffect(
                                    brick.getX() + brick.getWidth() / 2f - offset,
                                    brick.getY() + brick.getHeight() / 2f - offset,
                                    explosionSize, explosionSize,
                                    "/assets/explosion.gif"
                            );
                            explosions.add(newExplosion);
                        }
                        
                        bricksToRemove.add(brick);
                        score += 20;

                        // Spawn ExpandPaddle powerup
                        if (random.nextFloat() < 0.2f) {
                            powerUps.add(new ExpandPaddlePowerUp(
                                    brick.getX() + brick.getWidth() / 2f,
                                    brick.getY() + brick.getHeight()));
                        }

                        // Spawn TripleBall powerup
                        if (random.nextFloat() < 0.2f) {
                            powerUps.add(new TripleBallsPowerUp(
                                    brick.getX() + brick.getWidth() / 2f,
                                    brick.getY() + brick.getHeight()));
                        }
                        if (random.nextFloat() < 0.2f) {
                            powerUps.add(new ShrinkPaddlePowerUp(
                                    brick.getX() + brick.getWidth() / 2f,
                                    brick.getY() + brick.getHeight()));
                        }
                    }
                    break;
                }
            }
        }

        bricks.removeAll(bricksToRemove);

        // Ball falls out
        Iterator<Ball> ballIterator = balls.iterator();
        while (ballIterator.hasNext()) {
            Ball b = ballIterator.next();
            if (b.getY() + b.getHeight() >= paddle.getY() + 36) {
                ballIterator.remove();
            }
        }

        // Lose life
        if (balls.isEmpty()) {
            SoundManager.playSound("game_lost.wav");
            lives--;
            if (lives == 0) {
                gameOver();
                return;
            }

            powerUps.clear();
            paddle.setWidth(100);
            paddle.setX((renderer.getWidth() - paddle.getWidth()) / 2f);
            paddle.stop();

            Ball mainBall = new Ball(
                    paddle.getX() + paddle.getWidth() / 2f - 10,
                    paddle.getY() - 25,
                    20, 20, 0, 0, 0f, 0f, 6f,
                    renderer.getWidth(), renderer.getHeight(),
                    "/assets/ball.png"
            );
            balls.add(mainBall);
            gameState = GameState.READY;
        }

        // Level progression
        handleLevelProgression();
    }

    // ==== GAME OVER ====
    public void gameOver() {
        gameState = GameState.GAME_OVER;
        System.out.println("Game Over! Final score: " + score);

        int oldHigh = HighScoreManager.loadHighScore();
        if (score > oldHigh) HighScoreManager.saveHighScore(score);
    }


    // ==== RESET GAME ====
    public void startNewGame() {
        this.score = 0;
        this.lives = 3;
        this.currentLevelIndex = 1;

        this.powerUps.clear();
        this.balls.clear();
        this.bricks.clear();
        renderer.resetRenderer();

        if (isCustomLevel) {
            loadLevelFromFile(lastLevelNumber, true);
        } else {
            this.currentLevelIndex = 1;
            loadLevelFromFile(currentLevelIndex, false);
        }

        paddle.setX((renderer.getWidth() - paddle.getWidth()) / 2f);
        paddle.stop();

        Ball mainBall = new Ball(
                paddle.getX() + paddle.getWidth() / 2f - 10,
                paddle.getY() - 25,
                20, 20, 0, 0, 0f, 0f, 6f,
                renderer.getWidth(), renderer.getHeight(),
                "/assets/ball.png"
        );
        balls.add(mainBall);
        gameState = GameState.READY;

        SoundManager.stopMenuMusic();
        renderer.repaint();

        System.out.println("Game restarted (Level 1 loaded cleanly)!");
    }

    public void goToMainMenu() {
        this.powerUps.clear();
        this.balls.clear();
        this.bricks.clear();

        this.score = 0;
        this.lives = 3;

        this.isCustomLevel = false;
        this.currentLevel = null;
        this.currentLevelIndex = 1;
        this.lastLevelNumber = 1;

        if (renderer != null) renderer.resetRenderer();
        startNewGame();
        setGameState(GameState.MENU);
    }



    public void goToHighScore() {
        setGameState(GameState.HIGHSCORE);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
