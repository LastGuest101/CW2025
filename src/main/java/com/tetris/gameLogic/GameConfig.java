package com.tetris.gameLogic;

/**
 * Central repository for all static game configuration settings and constants.
 * <p>
 * This class allows for easy tuning of game balance (speed, dimensions),
 * UI layout, and file persistence paths from a single location.
 *
 * @author Jacob Villegas
 */
public class GameConfig {
    public static final int BRICK_SIZE = 20;
    public static final int WINDOW_WIDTH = 900;
    public static final int WINDOW_HEIGHT = 700;

    /**
     * The duration of the time-freeze power-up in milliseconds.
     * <p>
     * Value: 15000.0 (15 seconds).
     */
    public static final double FREEZE_DURATION = 15000.0;
    /**
     * The initial delay between brick movements in milliseconds.
     * <p>
     * A higher number means a slower game start.
     */
    public static final double BASE_SPEED = 800.0;
    /**
     * The factor used to decrease the delay interval as the level increases.
     * <p>
     * Example: New Delay = Old Delay * 0.8.
     */
    public static final double SPEED_MULTIPLIER = 0.8;
    public static final double MAX_SPEED_CAP = 50.0;

    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 25;

    /** The local filename used for persisting high score data. */
    public static final String HIGHSCORE_FILE = "highscore.dat";
}