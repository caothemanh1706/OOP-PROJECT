package powerups;

import Objects.Paddle;
import java.awt.*;

public class ExpandPaddlePowerUp extends PowerUp {
    private static boolean activeEffect = false;
    private static long effectEndTime = 0;
    private static int originalWidth;

    public ExpandPaddlePowerUp(float x, float y) {
        super(x, y);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillOval((int)x, (int)y, width, height);
    }

    @Override
    public void activate(Paddle paddle) {
        long currentTime = System.currentTimeMillis();

        // Lưu chiều rộng ban đầu 1 lần duy nhất
        if (!activeEffect) {
            originalWidth = paddle.getWidth();
            int newWidth = (int) (originalWidth * 1.5);
            paddle.setWidth(newWidth);
            activeEffect = true;
        }

        // Cộng dồn thời gian thêm 5 giây mỗi khi nhặt
        effectEndTime = currentTime + 5000;

        // Chạy một luồng kiểm tra liên tục thời gian
        new Thread(() -> {
            while (System.currentTimeMillis() < effectEndTime) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {}
            }

            // Khi hết thời gian, trả về kích thước ban đầu
            paddle.setWidth(originalWidth);
            activeEffect = false;
        }).start();

        active = false; // power-up biến mất khi được nhặt
    }
}
