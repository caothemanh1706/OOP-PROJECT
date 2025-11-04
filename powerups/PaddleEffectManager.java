package powerups;

import Objects.Paddle;
import java.util.concurrent.*;

/**
 * Quản lý chung cho các hiệu ứng thay đổi kích thước paddle.
 * - Lưu baseWidth 1 lần (lấy từ paddle khi lần đầu apply)
 * - Cho phép applyEffect(cancel previous) và schedule restore sau duration
 */
public class PaddleEffectManager {
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static volatile int baseWidth = 0;
    private static volatile String currentEffect = null;
    private static volatile ScheduledFuture<?> restoreTask = null;

    public static synchronized void ensureBaseWidth(Paddle paddle) {
        if (baseWidth == 0 && paddle != null) {
            baseWidth = paddle.getWidth();
            if (baseWidth <= 0) baseWidth = 50; // fallback an toàn (nếu cần)
        }
    }

    /**
     * Apply effect với factor .
     * Cancel restore cũ nếu còn, set width mới, schedule restore sau durationMs.
     */
    public static synchronized void applyEffect(Paddle paddle, String effectName, double factor, long durationMs) {
        if (paddle == null) return;

        ensureBaseWidth(paddle);
        if (restoreTask != null && !restoreTask.isDone()) {
            restoreTask.cancel(false);
        }
        int newWidth = Math.max(1, (int) Math.round(baseWidth * factor));
        paddle.setWidth(newWidth);

        currentEffect = effectName;
        restoreTask = scheduler.schedule(() -> {
            try {
                paddle.setWidth(baseWidth);
            } catch (Throwable ignored) {}
            synchronized (PaddleEffectManager.class) {
                currentEffect = null;
                restoreTask = null;
            }
        }, durationMs, TimeUnit.MILLISECONDS);
    }

    public static synchronized String getCurrentEffect() {
        return currentEffect;
    }

    public static synchronized int getBaseWidth() {
        return baseWidth;
    }
    public static void shutdown() {
        scheduler.shutdownNow();
    }
}
