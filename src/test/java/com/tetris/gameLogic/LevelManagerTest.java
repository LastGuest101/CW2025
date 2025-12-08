package com.tetris.gameLogic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the LevelManager class.
 *
 * @author Jacob Villegas
 */
class LevelManagerTest {

    private LevelManager levelManager;

    @BeforeEach
    void setUp() {
        levelManager = new LevelManager();
    }

    // Initialisation Tests

    @Test
    void testInitialLevel() {
        assertEquals(1, levelManager.getCurrentLevel(),
                "Level should start at 1");
    }

    @Test
    void testInitialSpeed() {
        assertEquals(GameConfig.BASE_SPEED, levelManager.getGameSpeed(), 0.01,
                "Initial speed should be 1000ms");
    }

    //Level Progression Tests

    @Test
    void testLevelUpAfter10Lines() {
        boolean speedChanged = levelManager.addLines(10);

        assertEquals(2, levelManager.getCurrentLevel(),
                "Level should increase to 2 after 10 lines");
        assertTrue(speedChanged,
                "Speed should change when leveling up");
    }

    @Test
    void testNoLevelUpBefore10Lines() {
        boolean speedChanged = levelManager.addLines(9);

        assertEquals(1, levelManager.getCurrentLevel(),
                "Level should still be 1 with only 9 lines cleared");
        assertFalse(speedChanged,
                "Speed should not change without level up");
    }

    @Test
    void testMultipleLevelUps() {
        levelManager.addLines(10);
        assertEquals(2, levelManager.getCurrentLevel());

        levelManager.addLines(10);
        assertEquals(3, levelManager.getCurrentLevel());

        levelManager.addLines(10);
        assertEquals(4, levelManager.getCurrentLevel());
    }

    @Test
    void testGradualLineClearing() {
        for (int i = 0; i < 9; i++) {
            levelManager.addLines(1);
            assertEquals(1, levelManager.getCurrentLevel(),
                    "Level should stay at 1 until 10th line");
        }

        boolean speedChanged = levelManager.addLines(1); // 10th line
        assertEquals(2, levelManager.getCurrentLevel(),
                "Level should increase to 2 on 10th line");
        assertTrue(speedChanged);
    }

    @Test
    void testLargeSingleClear() {
        levelManager.addLines(23);

        assertEquals(3, levelManager.getCurrentLevel(),
                "Level should be 3 after 23 lines (0-9: L1, 10-19: L2, 20-29: L3)");
    }

    // Speed Calculation Tests

    @Test
    void testSpeedDecreasesWithLevel() {
        double level1Speed = levelManager.getGameSpeed();

        levelManager.addLines(10); // Level 2
        double level2Speed = levelManager.getGameSpeed();

        levelManager.addLines(10); // Level 3
        double level3Speed = levelManager.getGameSpeed();

        assertTrue(level2Speed < level1Speed,
                "Speed should decrease (fall faster) at level 2");
        assertTrue(level3Speed < level2Speed,
                "Speed should continue decreasing at level 3");
    }

    @Test
    void testSpeedFormula() {
        levelManager.addLines(10); // Level 2

        double expectedSpeed = GameConfig.BASE_SPEED *
                Math.pow(GameConfig.SPEED_MULTIPLIER, 1); // Level 2 = exponent 1

        assertEquals(expectedSpeed, levelManager.getGameSpeed(), 0.01,
                "Speed should follow formula: BASE_SPEED * (MULTIPLIER ^ (level - 1))");
    }

    @Test
    void testSpeedCapAtHighLevels() {
        // Reach a very high level where speed would go below cap
        levelManager.addLines(300); // Level 31

        double speed = levelManager.getGameSpeed();

        assertTrue(speed >= GameConfig.MAX_SPEED_CAP,
                "Speed should never go below MAX_SPEED_CAP (100ms)");
        assertEquals(GameConfig.MAX_SPEED_CAP, speed, 0.01,
                "Speed should be exactly at cap at high levels");
    }

    @Test
    void testSpeedAtLevel10() {
        levelManager.addLines(90); // Level 10

        double expectedSpeed = GameConfig.BASE_SPEED *
                Math.pow(GameConfig.SPEED_MULTIPLIER, 9); // Level 10 = exponent 9

        assertEquals(expectedSpeed, levelManager.getGameSpeed(), 0.5,
                "Level 10 speed should be ~387ms");
    }

    @Test
    void testAddZeroLines() {
        boolean speedChanged = levelManager.addLines(0);

        assertEquals(1, levelManager.getCurrentLevel(),
                "Level should not change when 0 lines added");
        assertFalse(speedChanged,
                "Speed should not change when 0 lines added");
    }

    @Test
    void testAddNegativeLines() {
        boolean speedChanged = levelManager.addLines(-5);

        assertEquals(1, levelManager.getCurrentLevel(),
                "Level should not change with negative lines");
        assertFalse(speedChanged,
                "Speed should not change with negative lines");
    }

    @Test
    void testRepeatedSingleLineClearsExactly10Times() {
        for (int i = 1; i <= 10; i++) {
            levelManager.addLines(1);
        }

        assertEquals(2, levelManager.getCurrentLevel(),
                "Should level up after exactly 10 single-line clears");
    }

    @Test
    void testResetResetsLevel() {
        levelManager.addLines(25); // Advance to level 3
        assertEquals(3, levelManager.getCurrentLevel());

        levelManager.reset();

        assertEquals(1, levelManager.getCurrentLevel(),
                "Level should reset to 1");
    }

    @Test
    void testResetResetsSpeed() {
        levelManager.addLines(25); // Advance to level 3
        double fastSpeed = levelManager.getGameSpeed();

        levelManager.reset();

        assertEquals(GameConfig.BASE_SPEED, levelManager.getGameSpeed(), 0.01,
                "Speed should reset to BASE_SPEED");
    }

    @Test
    void testResetAllowsLevelUpAgain() {
        levelManager.addLines(10); // Level 2
        levelManager.reset();

        levelManager.addLines(10); // Should level up again

        assertEquals(2, levelManager.getCurrentLevel(),
                "Should be able to level up again after reset");
    }

}