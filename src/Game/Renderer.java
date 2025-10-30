package Game;

import Objects.*;
import powerups.PowerUp;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Renderer extends JPanel implements MouseListener {
    private final List<GameObjects> gameObjects = new ArrayList<>();
    private BufferedImage background;
    private BufferedImage menuBackground;
    private Paddle paddle;
    private Ball ball;
    private Menu gameMenu;


    public Renderer() {
        try {
            background = ImageIO.read(getClass().getResourceAsStream("/assets/background2.png"));
            menuBackground = ImageIO.read(getClass().getResourceAsStream("/assets/Background3.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Background not found.");
            background = null;
            menuBackground = null;
        }

        setBackground(Color.BLACK);
        paddle = new Paddle(350, 500, 100, 20, 0, 0, 7, "/assets/paddle.png");
        gameObjects.add(paddle);

        ball = new Ball(paddle.getX() + paddle.getWidth() / 2 - 10,
                paddle.getY() - 20 - 5,
                20, 20, 0, 0, 0f, 0f, 6f,
                785, 600, "/assets/ball.png"
        );

        gameObjects.add(ball);
        gameMenu = new Menu();
        GameManager.getInstance().startGame(this, paddle, ball);
        addMouseListener(this);
    }

    public Menu getGameMenu() {
        return gameMenu;
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

        GameManager manager = GameManager.getInstance();
        String gameState = manager.getGameState();

        if (gameState.equals(GameManager.STATE_MENU)) {

            if (menuBackground != null) {
                g.drawImage(menuBackground, 0, 0, getWidth(), getHeight(), null);
            } else {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.BOLD, 48));
                g.drawString("ARKANOID", (getWidth() - g.getFontMetrics().stringWidth("ARKANOID")) / 2, getHeight() / 3);
            }
            gameMenu.render(g, getWidth(), getHeight());
        } else {
            if (background != null) {
                g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
            }

            for (GameObjects obj : gameObjects) {
                obj.render(g);
            }

            for (PowerUp p : GameManager.getInstance().getPowerUps()) {
                p.render(g);
            }
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Lives: " + manager.getLives(), getWidth() - 100, 30);
        g.drawString("Score: " + manager.getScore(), getWidth() - 100, 50);


        if (manager.getGameState().equals("READY")) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Serif", Font.ITALIC , 48));
            FontMetrics fm = g.getFontMetrics();
            String startMessage = "Press SPACE to START";
            int x = (getWidth() - fm.stringWidth(startMessage)) / 2;
            int y = getHeight() / 2 + 100;
            g.drawString(startMessage, x, y);
        }
    }

    public void mouseClicked(MouseEvent e) {
        GameManager.getInstance().handleMouseClick(e.getX(), e.getY());
    }

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

}
