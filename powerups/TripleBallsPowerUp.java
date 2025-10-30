package powerups;
import Game.GameManager;
import Objects.Ball;
import Objects.Paddle;
import java.awt.*;


public class TripleBallsPowerUp extends PowerUp {
    public TripleBallsPowerUp(float x, float y) {
        super(x, y);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillOval((int)x, (int)y, width, height);
        g.setColor(Color.RED);
        g.drawString("×3", (int)x + width / 4, (int)y + height / 2);
    }

    @Override
    //access to list of balls
    public void activate(Paddle paddle) {
        GameManager game = GameManager.getInstance();

        if(game.getBalls().size() > 1) {
            active = false;
            return;
        }
        Ball main = game.getBalls().get(0);

        float bx = main.getX();
        float by = main.getY();
        int w = main.getWidth();
        int h = main.getHeight();
        float dx = main.getDirectionX();
        float dy = main.getDirectionY();
        float speed = main.getSpeed();
        int screenW = main.getScreenWidth();
        int screenH = main.getScreenHeight();

        float angleDiff = (float)Math.toRadians(20);//left,right diff

        float leftX = dx * (float)Math.cos(angleDiff) - dy * (float)Math.sin(angleDiff);
        float leftY = dx * (float)Math.sin(angleDiff) + dy * (float)Math.cos(angleDiff);
        float rightX = dx * (float)Math.cos(-angleDiff) - dy * (float)Math.sin(-angleDiff);
        float rightY = dx * (float)Math.sin(-angleDiff) + dy * (float)Math.cos(-angleDiff);

        Ball leftBall = new Ball(bx, by, w, h, 0, 0,
                leftX, leftY, speed, screenW, screenH, "/assets/ball.png");

        Ball rightBall = new Ball(bx, by, w, h, 0, 0,
                rightX, rightY, speed, screenW, screenH, "/assets/ball.png");

        game.getBalls().add(leftBall);
        game.getBalls().add(rightBall);

        active = false;
    }
}
