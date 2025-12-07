package com.tetris.gameLogic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Manages game progression, difficulty scaling, and speed calculations.
 * <p>
 * This class tracks the total number of lines cleared and automatically increases the
 * game level every 10 lines. As the level increases, the game speed (delay between ticks)
 * decreases based on an exponential decay formula, making the pieces fall faster.
 *
 * @author Jacob Villegas
 */
public class LevelManager {

    /** A bindable property for the current level (used to update the UI automatically). */
    private final IntegerProperty currentLevel = new SimpleIntegerProperty(1);
    private int totalLinesCleared = 0;
    /** The current delay between game ticks in milliseconds. */
    private double gameSpeed = GameConfig.BASE_SPEED;

    /**
     * Resets the level manager to its initial state.
     * <p>
     * Sets total lines to 0, level to 1, and speed to the base configuration.
     * This is called when starting a new game.
     */
    public void reset() {
        totalLinesCleared = 0;
        currentLevel.set(1);
        gameSpeed = GameConfig.BASE_SPEED;
    }

    /**
     * Registers cleared lines and checks for a level-up.
     * <p>
     * The logic follows a "10 lines per level" rule. If the total lines cleared
     * crosses a multiple of 10, the level increases.
     *
     * @param lines The number of lines cleared in the most recent move (1-4).
     * @return {@code true} if the speed has changed (level up occurred), indicating
     * that the game loop needs to be updated; {@code false} otherwise.
     */
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

    /**
     * recalculates the game speed based on the new level.
     * <p>
     * Formula: {@code Speed = Base_Speed * (Multiplier ^ (Level - 1))}
     * <br>
     * The result is clamped so it never goes below {@link GameConfig#MAX_SPEED_CAP}.
     *
     * @param level The new level to calculate speed for.
     * @return {@code true} if the new speed is significantly different from the old speed.
     */
    private boolean recalculateSpeed(int level) {
        double oldSpeed = gameSpeed;

        double newSpeed = GameConfig.BASE_SPEED * Math.pow(GameConfig.SPEED_MULTIPLIER, level - 1);

        if (newSpeed < GameConfig.MAX_SPEED_CAP) {
            newSpeed = GameConfig.MAX_SPEED_CAP;
        }

        gameSpeed = newSpeed;
        return Math.abs(newSpeed - oldSpeed) > 1.0;
    }

    /**
     * Gets the current delay between game ticks.
     *
     * @return The speed in milliseconds.
     */
    public double getGameSpeed() {
        return gameSpeed;
    }

    /**
     * Retrieves the level property for UI binding.
     * <p>
     * This allows the UI labels to automatically update whenever the level changes.
     *
     * @return The JavaFX {@link IntegerProperty} representing the current level.
     */
    public IntegerProperty levelProperty() {
        return currentLevel;
    }

    /**
     * Gets the raw integer value of the current level.
     *
     * @return The current level (starts at 1).
     */
    public int getCurrentLevel() {
        return currentLevel.get();
    }
}