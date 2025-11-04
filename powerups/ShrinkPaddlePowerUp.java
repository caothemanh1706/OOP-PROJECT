package powerups;

import Objects.Paddle;
import java.awt.*;

public class ShrinkPaddlePowerUp extends PowerUp {
    private static final long DURATION_MS = 5000;

    public ShrinkPaddlePowerUp(float x, float y) {
        super(x, y);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval((int) x, (int) y, width, height);
    }

    @Override
    public void activate(Paddle paddle) {
        PaddleEffectManager.ensureBaseWidth(paddle);
        PaddleEffectManager.applyEffect(paddle, "SHRINK", 0.5, DURATION_MS);
        active = false;
    }
}
