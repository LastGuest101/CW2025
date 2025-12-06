package com.tetris.gameLogic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty highscore = new SimpleIntegerProperty(0);

    public Score() {
        score.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > highscore.get()) {
                highscore.set(newValue.intValue());
            }
        });
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public IntegerProperty highScoreProperty() {
        return highscore;
    }

    public void add(int i) {
        score.set(score.get() + i);
    }

    public void reset() {
        score.set(0);
    }
}