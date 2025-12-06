package com.tetris.gameLogic;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HighScoreManager {

    public static void saveHighScore(int score) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(GameConfig.HIGHSCORE_FILE))) {
            dos.writeInt(score);
        } catch (IOException e) {
            System.err.println("Could not save high score: " + e.getMessage());
        }
    }

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