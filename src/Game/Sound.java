package Game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    Clip startGameSound;
    Clip coinSound;
    Clip deathSound;
    Clip levelUpSound;

    public Sound() {
        try {
            startGameSound = loadSound("start2.wav");
            coinSound = loadSound("pacman_eating2.wav");
            deathSound = loadSound("died2.wav");
            levelUpSound = loadSound("next_level.wav");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Clip loadSound(String fileName) {
        try {
            // משתמש בפונקציית עזר דומה ל-imgUrl
            File soundFile = new File("C:\\תכנות\\jbh\\java\\projects\\GamePacMan\\src\\sounds\\" + fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void playStartGame() {
        playSound(startGameSound);
    }

    public void playCoin() {
        playSound(coinSound);
    }

    public void playDeath() {
        playSound(deathSound);
    }

    public void playLevelUp() {
        playSound(levelUpSound);
    }

    private void playSound(Clip clip) {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }
}