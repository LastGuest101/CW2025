package com.tetris.gameLogic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class LevelManager {

    private final IntegerProperty currentLevel = new SimpleIntegerProperty(1);
    private int totalLinesCleared = 0;
    private double gameSpeed = GameConfig.BASE_SPEED;

    public void reset() {
        totalLinesCleared = 0;
        currentLevel.set(1);
        gameSpeed = GameConfig.BASE_SPEED;
    }

    public boolean addLines(int lines) {
        if (lines <= 0) return false;

        totalLinesCleared += lines;
        int newLevelVal = (totalLinesCleared / 10) + 1;

        if (newLevelVal > currentLevel.get()) {
            currentLevel.set(newLevelVal);
            return recalculateSpeed(newLevelVal);
        }
        return false;
    }

    private boolean recalculateSpeed(int level) {
        double oldSpeed = gameSpeed;

        double newSpeed = GameConfig.BASE_SPEED * Math.pow(GameConfig.SPEED_MULTIPLIER, level - 1);

        if (newSpeed < GameConfig.MAX_SPEED_CAP) {
            newSpeed = GameConfig.MAX_SPEED_CAP;
        }

        gameSpeed = newSpeed;
        return Math.abs(newSpeed - oldSpeed) > 1.0;
    }

    public double getGameSpeed() {
        return gameSpeed;
    }

    public IntegerProperty levelProperty() {
        return currentLevel;
    }

    public int getCurrentLevel() {
        return currentLevel.get();
    }
}