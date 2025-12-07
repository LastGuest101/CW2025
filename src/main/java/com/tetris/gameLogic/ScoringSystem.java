package com.tetris.gameLogic;

/**
 * Calculates the points awarded for clearing lines.
 * <p>
 * This class implements a scoring system that rewards clearing multiple lines at once
 * (especially "Tetris" clears of 4 lines) and maintains a "Combo" counter to reward
 * consecutive clears.
 * <p>
 * <b>Scoring Formula:</b><br>
 * {@code Score = (BaseLineScore * Level) + (50 * ComboCount * Level)}
 *
 * @author Jacob Villegas
 */
public class ScoringSystem {

    /**
     * Tracks the number of consecutive brick drops that resulted in a line clear.
     * <p>
     * -1 indicates no active combo. 0 is the first clear, 1 is the second consecutive clear, etc.
     */
    private int comboCount = -1;

    /**
     * Calculates the score for a specific move based on lines cleared and current level.
     * <p>
     * Base scores per line count:
     * <ul>
     * <li>1 Line: 100 points</li>
     * <li>2 Lines: 300 points</li>
     * <li>3 Lines: 500 points</li>
     * <li>4 Lines (Tetris): 800 points</li>
     * </ul>
     * If no lines are cleared, the combo counter is reset to -1.
     *
     * @param linesRemoved The number of full rows cleared (0 to 4).
     * @param level        The current game level (multiplier).
     * @return The total points awarded for this move.
     */
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

    /**
     * Resets the combo counter.
     * <p>
     * This is typically called when a new game starts to ensure the previous
     * game's combo streak doesn't carry over.
     */
    public void reset() {
        comboCount = -1;
    }
}


