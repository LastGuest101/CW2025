package com.tetris.gameLogic;

public class ScoringSystem {

    public int calculateScore(int linesRemoved) {
        if (linesRemoved == 0) {
            return 0;
        }

        return 50 * linesRemoved * linesRemoved;
    }
}

