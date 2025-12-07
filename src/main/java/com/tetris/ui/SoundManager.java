package com.tetris.ui;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

/**
 * Manages the loading and playback of all audio assets in the game.
 * <p>``
 * This class handles two types of audio:
 * <ul>
 * <li><b>Background Music:</b> Handled by {@link MediaPlayer} for long, looping tracks.</li>
 * <li><b>Sound Effects:</b> Handled by {@link AudioClip} for short, low-latency sounds (moves, drops, clears) that may need to be played multiple times rapidly.</li>
 * </ul>
 * It provides a centralized interface to play specific sounds and toggle music state.
 *
 * @author Jacob Villegas
 */
public class SoundManager {

    private MediaPlayer backgroundMusic;
    private AudioClip moveSound;
    private AudioClip dropSound;
    private AudioClip clearLineSound;
    private AudioClip freezeSound;

    /**
     * Constructs a new SoundManager and pre-loads all audio files.
     * <p>
     * Resources are loaded from the {@code /sounds/} directory in the classpath.
     * If a file cannot be found or loaded, an error is printed to stderr, but the
     * game continues without that specific sound.
     */
    public SoundManager() {
        moveSound = loadSound("move.wav");
        freezeSound = loadSound("freeze.wav");
        dropSound = loadSound("drop.wav");
        clearLineSound = loadSound("clear.wav");

        // Setup Background Music
        try {
            URL musicUrl = getClass().getResource("/sounds/music.mp3");
            if (musicUrl != null) {
                backgroundMusic = new MediaPlayer(new Media(musicUrl.toExternalForm()));
                backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE); // Loop forever
                backgroundMusic.setVolume(0.5); // 50% volume
            }
        } catch (Exception e) {
            System.err.println("Could not load background music.");
        }
    }

    private AudioClip loadSound(String filename) {
        try {
            URL url = getClass().getResource("/sounds/" + filename);
            if (url != null) {
                return new AudioClip(url.toExternalForm());
            }
        } catch (Exception e) {
            System.err.println("Could not load sound: " + filename);
        }
        return null;
    }

    public void playMusic() {
        if (backgroundMusic != null) backgroundMusic.play();
    }

    public void stopMusic() {
        if (backgroundMusic != null) backgroundMusic.stop();
    }

    public void playMove() {
        if (moveSound != null) {
            moveSound.setVolume(0.02);
            moveSound.play();
        }
    }

    public void playRotate() {
        if (moveSound != null) {
            moveSound.setVolume(0.02);
            moveSound.play();
        }
    }

    public void playDrop() {
        if (dropSound != null) {
            dropSound.setVolume(0.2);
            dropSound.play();
        }
    }

    public void playClearLine() {
        if (clearLineSound != null) clearLineSound.play();
    }

    public void playFreeze(){
        if (freezeSound != null) freezeSound.play();
    }

    public void toggleMusic() {
        if (backgroundMusic != null) {
            if (backgroundMusic.getStatus() == MediaPlayer.Status.PLAYING) {
                backgroundMusic.pause();
            } else {
                backgroundMusic.play();
            }
        }
    }
}