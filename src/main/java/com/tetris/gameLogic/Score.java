package com.tetris.gameLogic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Manages the player's current score and high score tracking.
 * <p>
 * This class uses JavaFX {@link IntegerProperty} objects to store values.
 * This allows the UI (GameView) to bind directly to these properties, ensuring
 * that the on-screen score updates automatically whenever the underlying data changes.
 * <p>
 * It also includes an internal listener that automatically updates the high score
 * if the current score surpasses it.
 *
 * @author Jacob Villegas
 */
public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty highscore = new SimpleIntegerProperty(0);

    /**
     * Constructs a new Score manager.
     * <p>
     * Initializes a listener on the score property. Whenever the score changes,
     * it checks if the new value exceeds the current high score. If it does,
     * the high score is immediately updated to match.
     */
    public Score() {
        score.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > highscore.get()) {
                highscore.set(newValue.intValue());
            }
        });
    }

    /**
     * Retrieves the observable property for the current score.
     * <p>
     * Use this method for UI binding (e.g., {@code label.textProperty().bind(score.scoreProperty().asString())}).
     *
     * @return The JavaFX IntegerProperty for the score.
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Retrieves the observable property for the high score.
     *
     * @return The JavaFX IntegerProperty for the high score.
     */
    public IntegerProperty highScoreProperty() {
        return highscore;
    }

    /**
     * Adds points to the current score.
     *
     * @param i The number of points to add.
     */
    public void add(int i) {
        score.set(score.get() + i);
    }
    /**
     * Resets the current score to 0.
     * <p>
     * Note: This does <b>not</b> reset the high score.
     */

    public void reset() {
        score.set(0);
    }
}