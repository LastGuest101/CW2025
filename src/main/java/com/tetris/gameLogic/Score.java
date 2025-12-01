package com.tetris.gameLogic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private IntegerProperty highscore = new SimpleIntegerProperty(0);

    public IntegerProperty scoreProperty() {
        return score;
    }

    public IntegerProperty highScoreProperty() {
        return highscore;
    }

    public void add(int i) {
        score.setValue(score.getValue() + i);
    }

    public void reset() {
        score.setValue(0);
    }

    public void updateHighscore(IntegerProperty scoreToCheck) {
        if (scoreToCheck.get() > highscore.get()) {
            highscore.set(scoreToCheck.get());
        }
    }
}
