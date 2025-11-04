package Game;

import Objects.*;
import powerups.PowerUp;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

import Menus.Menu;
import Menus.GameOverMenu;
import Menus.HighScoreMenu;
import Menus.HowToPlayMenu;
import Menus.LevelMenu;
import Menus.PauseOverlay;


/**
 * Xử lý render giao diện game Arkanoid.
 */
public class Renderer extends JPanel implements MouseListener, MouseMotionListener {

    // ====================== TÀI NGUYÊN ======================
    private BufferedImage background;
    private BufferedImage menuBackground;
    private BufferedImage gameOverBackground;
    private BufferedImage heartImage;
    private BufferedImage pauseImage;
    private BufferedImage winBackground;
    private BufferedImage playButtonimage;
    private boolean showStartText = true;

    // ====================== GAME OBJECTS ======================
    private final List<GameObjects> gameObjects = new ArrayList<>();
    private Paddle paddle;

    // ====================== MENU ======================
    private Menu gameMenu;
    private GameOverMenu gameOverMenu;
    private HighScoreMenu highScoreMenu;
    private HowToPlayMenu howtoplayMenu;
    private LevelMenu levelMenu;
    private PauseOverlay pauseOverlay;

    // ====================== PAUSE ICON CONFIG ======================
    private static final int PAUSE_ICON_SIZE = 50;
    private static final int PAUSE_ICON_X = 20;
    private static final int PAUSE_ICON_Y = 20;

    // ====================== CONSTRUCTOR ======================
    public Renderer() {
        loadAssets();

        setBackground(Color.BLACK);

        paddle = new Paddle(350, 500, 100, 20, 0, 0, 7, "/assets/paddle.png");
        gameObjects.add(paddle);

        Ball firstBall = new Ball(
                paddle.getX() + paddle.getWidth() / 2 - 10,
                paddle.getY() - 25,
                20, 20, 0, 0,
                0f, 0f, 6f,
                785, 600, "/assets/ball.png"
        );

        initMenus();
        GameManager.getInstance().startGame(this, paddle, firstBall);

        Timer blinkTimer = new Timer(400, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStartText = !showStartText;
                repaint();
            }
        });
        blinkTimer.start();

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // ====================== LOAD IMAGES ======================
    private void loadAssets() {
        try {
            background = ImageIO.read(getClass().getResourceAsStream("/assets/background2.png"));
            menuBackground = ImageIO.read(getClass().getResourceAsStream("/assets/Background3.png"));
            gameOverBackground = ImageIO.read(getClass().getResourceAsStream("/assets/GameOverBackGround.png"));
            winBackground = ImageIO.read(getClass().getResourceAsStream("/assets/win.png"));
            heartImage = ImageIO.read(getClass().getResourceAsStream("/assets/heart.png"));
            pauseImage = ImageIO.read(getClass().getResourceAsStream("/assets/pause.png"));
            playButtonimage = ImageIO.read(getClass().getResourceAsStream("/assets/playbutton.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println(" Không tìm thấy ảnh background/menu.");
        }
    }

    private void initMenus() {
        gameMenu = new Menu();
        gameOverMenu = new GameOverMenu();
        highScoreMenu = new HighScoreMenu();
        howtoplayMenu = new HowToPlayMenu();
        levelMenu = new LevelMenu();
        pauseOverlay = new PauseOverlay();
    }

    public Menu getGameMenu() { return gameMenu; }
    public GameOverMenu getGameOverMenu() { return gameOverMenu; }
    public HowToPlayMenu getHowToPlayMenu() { return howtoplayMenu; }
    public LevelMenu getLevelMenu() { return levelMenu; }

    // ====================== GAME OBJECT CONTROL ======================
    public void addGameObject(GameObjects obj) {
        gameObjects.add(obj);
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public void resetRenderer() {
        gameObjects.clear();
        gameObjects.add(paddle);
        repaint();
    }

    // ====================== RENDER ======================
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        GameManager manager = GameManager.getInstance();
        GameState gameState = manager.getGameState();

        switch (gameState) {
            case MENU -> drawMenu(g);
            case HOWTOPLAY -> drawHowToPlayScreen(g);
            case WON -> drawWinScreen(g);
            case GAME_OVER -> drawGameOverScreen(g);
            case LEVELS -> drawLevelsScreen(g);
            case HIGHSCORE -> {
                highScoreMenu.render(g, getWidth(), getHeight());
                return;
            }
            default -> drawGame(g);
        }

        if (gameState == GameState.READY) drawStartText(g);

        if (gameState == GameState.PAUSED) {
            pauseOverlay.render(g, getWidth(), getHeight());
        }
    }

    // ====================== DRAW STATES ======================
    private void drawMenu(Graphics g) {
        if (menuBackground != null)
            g.drawImage(menuBackground, 0, 0, getWidth(), getHeight(), null);

        gameMenu.render(g, getWidth(), getHeight());
    }

    private void drawHowToPlayScreen(Graphics g) {
        howtoplayMenu.render(g, getWidth(), getHeight());
    }

    private void drawLevelsScreen(Graphics g) {
        levelMenu.render(g, getWidth(), getHeight());
    }

    private void drawWinScreen(Graphics g) {
        if (winBackground != null)
            g.drawImage(winBackground, 0, 0, getWidth(), getHeight(), null);

        gameOverMenu.render(g, getWidth(), getHeight());

    }

    private void drawGameOverScreen(Graphics g) {
        if (gameOverBackground != null)
            g.drawImage(gameOverBackground, 0, 0, getWidth(), getHeight(), null);

        gameOverMenu.render(g, getWidth(), getHeight());
    }

    private void drawGame(Graphics g) {
        if (background != null)
            g.drawImage(background, 0, 0, getWidth(), getHeight(), null);

        for (GameObjects obj : gameObjects) obj.render(g);
        GameManager.getInstance().getBalls().forEach(ball -> ball.render(g));
        GameManager.getInstance().getPowerUps().forEach(p -> p.render(g));
        List<ExplosionEffect> explosions = GameManager.getInstance().getExplosions();
        for (ExplosionEffect e : explosions) {
            e.render(g);
        }
        drawHUD(g, GameManager.getInstance());
    }

    private void drawHUD(Graphics g, GameManager manager) {
        GameManager m = GameManager.getInstance();

        // Draw hearts
        int size = 25;
        int startX = getWidth() - (m.getLives() * (size + 5)) - 20;
        for (int i = 0; i < m.getLives(); i++)
            g.drawImage(heartImage, startX + i * (size + 5), 10, size, size, null);

        // Score
        g.setColor(Color.WHITE);
        g.drawString("Score: " + m.getScore(), getWidth() - 120, 50);

        // Pause icon
        if (manager.getGameState() == GameState.PLAYING || manager.getGameState() == GameState.PAUSED) {
            BufferedImage iconToDraw;
            if (manager.getGameState() == GameState.PAUSED) {
                iconToDraw = playButtonimage;
            } else {
                iconToDraw = pauseImage;
            }
            g.drawImage(pauseImage, PAUSE_ICON_X, PAUSE_ICON_Y, PAUSE_ICON_SIZE, PAUSE_ICON_SIZE, this);
        }
    }

    private void drawStartText(Graphics g) {
        if (showStartText) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Serif", Font.ITALIC, 48));
            String msg = "Press SPACE to START";
            int x = (getWidth() - g.getFontMetrics().stringWidth(msg)) / 2;

            g.drawString(msg, x, getHeight() / 2 + 150);
        }
    }

    // ====================== MOUSE EVENTS ======================
    @Override
    public void mouseMoved(MouseEvent e) {
        GameManager manager = GameManager.getInstance();

        switch (manager.getGameState()) {
            case MENU -> gameMenu.handleMouseMove(e.getPoint(), getWidth(), getHeight());
            case HOWTOPLAY -> howtoplayMenu.handleMouseMove(e.getPoint(), getWidth(), getHeight());
            case GAME_OVER,WON -> gameOverMenu.handleMouseMove(e.getPoint(), getWidth(), getHeight());
            case HIGHSCORE -> highScoreMenu.handleMouseMove(e.getPoint(), getWidth(), getHeight());
            case LEVELS-> levelMenu.handleMouseMove(e.getPoint(), getWidth(), getHeight());
        }

        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        GameManager manager = GameManager.getInstance();
        Point p = e.getPoint();

        // Pause button click
        if (isPauseClicked(p)) {
            togglePause(manager);
            repaint();
            return;
        }

        if (manager.getGameState() == GameState.PAUSED) {
            String choice = pauseOverlay.handleMouseClick(p);

            if (choice != null) {
                switch (choice) {
                    case "CONTINUE" -> {
                        togglePause(manager);
                        SoundManager.stopMenuMusic();
                    }
                    case "RESTART" -> {
                        manager.startNewGame();
                        SoundManager.stopMenuMusic();
                    }
                    case "BACK" -> manager.goToMainMenu();
                }
                repaint();
                return;
            }
        }

        switch (manager.getGameState()) {
            case MENU -> manager.handleMouseClick(p.x, p.y);
            case GAME_OVER, WON -> handleGameOverClick(manager, p);
            case HOWTOPLAY -> {
                String choice = howtoplayMenu.handleMouseClick(p, getWidth(), getHeight());
                if ("BACK".equals(choice)) {
                    manager.setGameState(GameState.MENU);
                }
            }
            case HIGHSCORE -> {
                if ("EXIT MENU".equals(highScoreMenu.handleMouseClick(p, getWidth(), getHeight())))
                    manager.setGameState(GameState.MENU);
            }
            case LEVELS -> {
                String choice = levelMenu.handleMouseClick(p, getWidth(), getHeight());
                if (choice == null) break;

                if (choice.equals("BACK")) {
                    manager.setGameState(GameState.MENU);
                    break;
                }

                if (choice.startsWith("LEVEL ")) {
                    int levelNum = Integer.parseInt(choice.split(" ")[1]);
                    manager.loadLevelFromFile(levelNum, true);
                    manager.setGameState(GameState.READY);
                    SoundManager.stopMenuMusic();
                    repaint();
                    System.out.println("Loaded Level " + levelNum);
                }
            }

        }

        repaint();
    }

    private boolean isPauseClicked(Point p) {
        return p.x >= PAUSE_ICON_X && p.x <= PAUSE_ICON_X + PAUSE_ICON_SIZE &&
                p.y >= PAUSE_ICON_Y && p.y <= PAUSE_ICON_Y + PAUSE_ICON_SIZE;
    }

    private void togglePause(GameManager m) {
        if (m.getGameState() == GameState.PLAYING) {
            m.setGameState(GameState.PAUSED);
            SoundManager.stopMenuMusic();
        }  else if (m.getGameState() == GameState.PAUSED) {
            m.setGameState(GameState.PLAYING);
        }
        repaint();
    }

    private void handleGameOverClick(GameManager m, Point p) {
        String choice = gameOverMenu.handleMouseClick(p, getWidth(), getHeight());

        switch (choice) {
            case "REPLAY" -> m.startNewGame();
            case "EXIT MENU" -> m.goToMainMenu();
            case "EXIT" -> System.exit(0);
        }
    }

    // Unused
    @Override public void mouseDragged(MouseEvent e) {}
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
