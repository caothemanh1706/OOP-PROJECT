package Game;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Quản lý và phát âm thanh trong game Arkanoid.
 */
public class SoundManager {
    private static final Map<String, Clip> clips = new HashMap<>();
    private static Clip backgroundClip = null;
    /**
     * Preload âm thanh từ file .wav
     */
    private static void preload(String soundFile) {
        try {
            URL url = SoundManager.class.getResource("/Sounds/" + soundFile);
            if (url == null) {
                System.err.println("Không tìm thấy âm thanh: " + soundFile);
                return;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clips.put(soundFile, clip);
            ais.close();
            clip.start();
            clip.stop();
            clip.setFramePosition(0);
        } catch (Exception e) {
            System.err.println("Không thể preload âm thanh: " + soundFile);
            e.printStackTrace();
        }
    }

    /**
     * Khởi tạo tất cả âm thanh trước khi game bắt đầu
     */
    public static void init() {
        preload("brick_hit.wav");
        preload("paddle_hit.wav");
        preload("explosion.wav");
        preload("game_lost.wav");
        preload("power_up_paddle.wav");
        preload("game_start.wav");

        warmUpMixerReal();
    }

    private static void warmUpMixerReal() {
        try {
            Clip testClip = clips.values().stream().findFirst().orElse(null);
            if (testClip == null) return;

            FloatControl gain = null;
            if (testClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                gain = (FloatControl) testClip.getControl(FloatControl.Type.MASTER_GAIN);
                gain.setValue(-80.0f);
            }

            testClip.setFramePosition(0);
            testClip.start();
            Thread.sleep(100);
            testClip.stop();
            testClip.setFramePosition(0);

            if (gain != null) gain.setValue(0.0f);
        } catch (Exception e) {
            System.err.println("Warm-up mixer thất bại");
        }
    }

    /**
     * phát âm thanh vô thời hạn ở màn hình chờ
     */
    public static void playMusic() {
        stopMenuMusic();
        Clip clip = clips.get("game_start.wav");
        if (clip == null) {
            return ;
        }

        backgroundClip = clip;
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public static void stopMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }
    }

    public static void playMenuMusic() {
        stopMusic();
        Clip clip = clips.get("game_start.wav"); // dùng clip đã preload
        if (clip == null) {
            System.err.println("Nhạc menu chưa được preload!");
            return;
        }

        backgroundClip = clip;
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }


    /**
     * Stop Music.
     */
    public static void stopMenuMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }
    }

    /**
     * Phát âm thanh ngay lập tức, không delay.
     */
    public static void playSound(String soundFile) {
        Clip clip = clips.get(soundFile);
        if (clip == null) {
            System.err.println("Âm thanh chưa được preload: " + soundFile);
            return;
        }

        try {
            if (clip.isRunning()) {
                Clip newClip = AudioSystem.getClip();
                AudioInputStream ais = AudioSystem.getAudioInputStream(
                        SoundManager.class.getResource("/Sounds/" + soundFile));
                newClip.open(ais);
                newClip.start();
            } else {
                clip.setFramePosition(0);
                clip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
