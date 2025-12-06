package com.tetris.gameLogic;

public class ScoringSystem {

    private int comboCount = -1; // -1 means no combo

    public int calculateScore(int linesRemoved, int level) {
        if (linesRemoved == 0) {
            comboCount = -1; // Reset combo if a piece drops without clearing
            return 0;
        }

        comboCount++;

        // Standard Tetris Base Scores
        int baseScore = switch (linesRemoved) {
            case 1 -> 100;
            case 2 -> 300;
            case 3 -> 500;
            case 4 -> 800;
            default -> 0;
        };

        // Formula: (Base * Level) + (Combo Bonus * Level)
        return (baseScore * level) + (50 * comboCount * level);
    }

    public void reset() {
        comboCount = -1;
    }
}


