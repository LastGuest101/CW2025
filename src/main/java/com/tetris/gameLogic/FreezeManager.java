package com.tetris.gameLogic;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Manages the state and duration of the "Time Freeze" power-up.
 * <p>
 * This class handles the activation logic, ensures the ability can only be used
 * once per game (or per reset cycle), and sets up a timer to automatically
 * expire the freeze effect after a configured duration.
 *
 * @author Jacob Villegas
 */
public class FreezeManager {

    /** Indicates if the freeze effect is currently active. */
    private boolean isFrozen = false;
    /** Tracks if the ability has already been consumed in the current session. */
    private boolean hasUsedFreeze = false;

    /**
     * Attempts to activate the freeze ability.
     * <p>
     * Activation will fail if the board is already frozen or if the ability
     * has already been used since the last reset. If successful, it starts a
     * JavaFX {@link Timeline} that will automatically clear the frozen state
     * after {@link GameConfig#FREEZE_DURATION}.
     *
     * @param onFreezeEnd A {@link Runnable} to execute when the timer expires
     * (e.g., resuming the game loop or playing a sound).
     * @return {@code true} if the freeze was successfully activated;
     * {@code false} if it was blocked (already used or active).
     */
    public boolean tryActivate(Runnable onFreezeEnd) {
        if (isFrozen || hasUsedFreeze) {
            return false;
        }

        isFrozen = true;
        hasUsedFreeze = true;

        new Timeline(new KeyFrame(
                Duration.millis(GameConfig.FREEZE_DURATION),
                ae -> {
                    isFrozen = false;
                    if (onFreezeEnd != null) onFreezeEnd.run();
                }
        )).play();

        return true;
    }

    /**
     * Resets the manager state, allowing the ability to be used again.
     * <p>
     * This is typically called when a new game starts.
     */
    public void reset() {
        isFrozen = false;
        hasUsedFreeze = false;
    }

    /**
     * Checks if the time freeze effect is currently active.
     *
     * @return {@code true} if time is frozen; {@code false} otherwise.
     */
    public boolean isFrozen() {
        return isFrozen;
    }
}