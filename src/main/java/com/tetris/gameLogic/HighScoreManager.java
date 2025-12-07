package com.tetris.gameLogic;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
/**
 * A utility class responsible for persisting the high score to the local file system.
 * <p>
 * This class uses binary file I/O to save and retrieve a single integer value,
 * ensuring the player's best score is remembered even after the application closes.
 * The file location is determined by {@link GameConfig#HIGHSCORE_FILE}.
 *
 * @author Jacob Villegas
 */

public class HighScoreManager {

    /**
     * Writes the given score to the high score file.
     * <p>
     * This method overwrites the existing file. It uses {@link DataOutputStream}
     * to write the raw integer bytes, meaning the resulting file is binary
     * and not meant to be edited manually by the user.
     *
     * @param score The new high score to save.
     */
    public static void saveHighScore(int score) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(GameConfig.HIGHSCORE_FILE))) {
            dos.writeInt(score);
        } catch (IOException e) {
            System.err.println("Could not save high score: " + e.getMessage());
        }
    }

    /**
     * Retrieves the stored high score from the file system.
     * <p>
     * If the high score file does not exist or if
     * a read error occurs, this method safely returns 0.
     *
     * @return The saved high score, or 0 if no data is found.
     */
    public static int loadHighScore() {
        if (!Files.exists(Paths.get(GameConfig.HIGHSCORE_FILE))) {
            return 0;
        }

        try (DataInputStream dis = new DataInputStream(new FileInputStream(GameConfig.HIGHSCORE_FILE))) {
            return dis.readInt();
        } catch (IOException e) {
            System.err.println("Could not load high score: " + e.getMessage());
            return 0;
        }
    }
}