package com.tetris.gameLogic;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HighScoreManager {

    private static final String DATA_FILE = "highscore.dat";

    // Save the score to a file
    public static void saveHighScore(int score) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(DATA_FILE))) {
            dos.writeInt(score);
        } catch (IOException e) {
            System.err.println("Could not save high score: " + e.getMessage());
        }
    }

    // Load the score from the file
    public static int loadHighScore() {
        if (!Files.exists(Paths.get(DATA_FILE))) {
            return 0; // Return 0 if no file exists yet
        }

        try (DataInputStream dis = new DataInputStream(new FileInputStream(DATA_FILE))) {
            return dis.readInt();
        } catch (IOException e) {
            System.err.println("Could not load high score: " + e.getMessage());
            return 0;
        }
    }
}