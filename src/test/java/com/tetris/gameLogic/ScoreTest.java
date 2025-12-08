package com.tetris.gameLogic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Score and ScoringSystem classes.
 * Tests basic score tracking and the combo-based scoring system.
 *
 * @author Jacob Villegas
 */
class ScoreTest {

    @Test
    void initialiseScoreTest() {
        Score score = new Score();
        int result = score.scoreProperty().get();
        assertEquals(0, result);
    }

    @Test
    void addSingleScore() {
        Score score = new Score();
        score.add(5);
        int result = score.scoreProperty().get();
        assertEquals(5, result);
    }

    @Test
    void addMultipleScore() {
        Score score = new Score();
        score.add(5);
        score.add(4);
        score.add(11);
        int result = score.scoreProperty().get();
        assertEquals(20, result);
    }

    @Test
    void resetScore() {
        Score score = new Score();
        score.add(100);
        score.reset();
        int result = score.scoreProperty().get();
        assertEquals(0, result);
    }


    private ScoringSystem scoringSystem;

    @BeforeEach
    void setUp() {
        scoringSystem = new ScoringSystem();
    }

    @Test
    void testSingleLineClearLevel1() {
        int score = scoringSystem.calculateScore(1, 1);

        assertEquals(100, score,
                "Single line at level 1 should be 100 points (100 × 1 + 0 combo)");
    }

    @Test
    void testDoubleLineClearLevel1() {
        int score = scoringSystem.calculateScore(2, 1);

        assertEquals(300, score,
                "Double line at level 1 should be 300 points (300 × 1 + 0 combo)");
    }

    @Test
    void testTripleLineClearLevel1() {
        int score = scoringSystem.calculateScore(3, 1);

        assertEquals(500, score,
                "Triple line at level 1 should be 500 points (500 × 1 + 0 combo)");
    }

    @Test
    void testTetrisClearLevel1() {
        int score = scoringSystem.calculateScore(4, 1);

        assertEquals(800, score,
                "Tetris (4 lines) at level 1 should be 800 points (800 × 1 + 0 combo)");
    }

    @Test
    void testZeroLinesClearReturnsZero() {
        int score = scoringSystem.calculateScore(0, 1);

        assertEquals(0, score,
                "Clearing 0 lines should return 0 points");
    }


    @Test
    void testSingleLineClearLevel2() {
        int score = scoringSystem.calculateScore(1, 2);

        assertEquals(200, score,
                "Single line at level 2 should be 200 points (100 × 2)");
    }

    @Test
    void testSingleLineClearLevel5() {
        int score = scoringSystem.calculateScore(1, 5);

        assertEquals(500, score,
                "Single line at level 5 should be 500 points (100 × 5)");
    }

    @Test
    void testTetrisClearLevel3() {
        int score = scoringSystem.calculateScore(4, 3);

        assertEquals(2400, score,
                "Tetris at level 3 should be 2400 points (800 × 3)");
    }

    @Test
    void testDoubleLineClearLevel10() {
        int score = scoringSystem.calculateScore(2, 10);

        assertEquals(3000, score,
                "Double line at level 10 should be 3000 points (300 × 10)");
    }


    @Test
    void testFirstClearStartsCombo() {
        int score = scoringSystem.calculateScore(1, 1);

        assertEquals(100, score,
                "First clear should have no combo bonus (100 × 1 + 50 × 0 × 1)");
    }

    @Test
    void testSecondConsecutiveClearAddsCombo() {
        scoringSystem.calculateScore(1, 1); // First clear (combo = 0)
        int score = scoringSystem.calculateScore(1, 1); // Second clear (combo = 1)

        // Base: 100 × 1 = 100
        // Combo: 50 × 1 × 1 = 50
        // Total: 150
        assertEquals(150, score,
                "Second consecutive clear should add combo bonus (100 + 50)");
    }

    @Test
    void testThirdConsecutiveClearIncreasesCombo() {
        scoringSystem.calculateScore(1, 1); // Combo = 0
        scoringSystem.calculateScore(1, 1); // Combo = 1
        int score = scoringSystem.calculateScore(1, 1); // Combo = 2

        // Base: 100 × 1 = 100
        // Combo: 50 × 2 × 1 = 100
        // Total: 200
        assertEquals(200, score,
                "Third consecutive clear should have combo = 2 (100 + 100)");
    }

    @Test
    void testComboWithLevelMultiplier() {
        scoringSystem.calculateScore(1, 3); // First clear at level 3 (combo = 0)
        int score = scoringSystem.calculateScore(1, 3); // Second clear (combo = 1)

        // Base: 100 × 3 = 300
        // Combo: 50 × 1 × 3 = 150
        // Total: 450
        assertEquals(450, score,
                "Combo bonus should also multiply by level (300 + 150)");
    }

    @Test
    void testComboResetsAfterZeroLines() {
        scoringSystem.calculateScore(1, 1); // Combo = 0
        scoringSystem.calculateScore(1, 1); // Combo = 1
        scoringSystem.calculateScore(0, 1); // Reset combo to -1

        int score = scoringSystem.calculateScore(1, 1); // Should be back to combo = 0

        assertEquals(100, score,
                "Combo should reset after clearing 0 lines (no bonus)");
    }

    @Test
    void testHighComboChain() {
        scoringSystem.calculateScore(1, 1); // Combo = 0
        scoringSystem.calculateScore(1, 1); // Combo = 1
        scoringSystem.calculateScore(1, 1); // Combo = 2
        scoringSystem.calculateScore(1, 1); // Combo = 3
        int score = scoringSystem.calculateScore(1, 1); // Combo = 4

        // Base: 100 × 1 = 100
        // Combo: 50 × 4 × 1 = 200
        // Total: 300
        assertEquals(300, score,
                "5th consecutive clear should have combo = 4 (100 + 200)");
    }

    @Test
    void testComboWithTetris() {
        scoringSystem.calculateScore(4, 2); // First Tetris (combo = 0)
        int score = scoringSystem.calculateScore(4, 2); // Second Tetris (combo = 1)

        // Base: 800 × 2 = 1600
        // Combo: 50 × 1 × 2 = 100
        // Total: 1700
        assertEquals(1700, score,
                "Back-to-back Tetris should add combo bonus (1600 + 100)");
    }

    @Test
    void testNegativeLinesReturnsZero() {
        int score = scoringSystem.calculateScore(-1, 1);

        assertEquals(0, score,
                "Negative lines should return 0 points (invalid input)");
    }

    @Test
    void testFiveLinesClearReturnsZero() {
        int score = scoringSystem.calculateScore(5, 1);

        assertEquals(0, score,
                "More than 4 lines should return 0 (impossible in standard Tetris)");
    }


    @Test
    void testComboAtLevelZero() {
        scoringSystem.calculateScore(1, 0); // Combo = 0, but level = 0
        int score = scoringSystem.calculateScore(1, 0); // Combo = 1, level = 0

        assertEquals(0, score,
                "Combo bonus at level 0 should still be 0");
    }


    @Test
    void testResetClearsCombo() {
        scoringSystem.calculateScore(1, 1); // Combo = 0
        scoringSystem.calculateScore(1, 1); // Combo = 1

        scoringSystem.reset();

        int score = scoringSystem.calculateScore(1, 1);

        assertEquals(100, score,
                "Reset should clear combo (no bonus on next clear)");
    }

    @Test
    void testResetBeforeAnyClears() {
        scoringSystem.reset();

        int score = scoringSystem.calculateScore(1, 1);

        assertEquals(100, score,
                "Reset before any clears should not affect first clear");
    }

    @Test
    void testMultipleResets() {
        scoringSystem.calculateScore(1, 1);
        scoringSystem.reset();
        scoringSystem.reset();
        scoringSystem.reset();

        int score = scoringSystem.calculateScore(1, 1);

        assertEquals(100, score,
                "Multiple resets should not break combo system");
    }

    // ========== Integration Tests (Score + ScoringSystem) ==========

    @Test
    void testScoreIntegrationWithScoringSystem() {
        Score score = new Score();
        ScoringSystem system = new ScoringSystem();

        // Simulate 3 consecutive line clears at level 2
        score.add(system.calculateScore(1, 2)); // 200 (100 × 2)
        score.add(system.calculateScore(1, 2)); // 300 (200 + 100 combo)
        score.add(system.calculateScore(1, 2)); // 400 (200 + 200 combo)

        assertEquals(900, score.scoreProperty().get(),
                "Total score should be 200 + 300 + 400 = 900");
    }

    @Test
    void testComboBreakInIntegration() {
        Score score = new Score();
        ScoringSystem system = new ScoringSystem();

        score.add(system.calculateScore(1, 1)); // 100
        score.add(system.calculateScore(1, 1)); // 150 (combo = 1)
        score.add(system.calculateScore(0, 1)); // 0 (combo reset)
        score.add(system.calculateScore(1, 1)); // 100 (combo = 0 again)

        assertEquals(350, score.scoreProperty().get(),
                "Score should be 100 + 150 + 0 + 100 = 350");
    }

    @Test
    void testHighScoreUpdateWithScoringSystem() {
        Score score = new Score();
        ScoringSystem system = new ScoringSystem();

        // Simulate high-scoring game
        score.add(system.calculateScore(4, 5)); // Tetris at level 5: 4000

        assertEquals(4000, score.highScoreProperty().get(),
                "High score should auto-update when current score exceeds it");
    }

    // ========== Formula Verification Tests ==========

    @Test
    void testFormulaAccuracy() {
        // Manual calculation: (500 × 3) + (50 × 2 × 3)
        // = 1500 + 300 = 1800
        scoringSystem.calculateScore(1, 3); // Combo = 0
        scoringSystem.calculateScore(1, 3); // Combo = 1
        int score = scoringSystem.calculateScore(3, 3); // Combo = 2, triple clear

        assertEquals(1800, score,
                "Formula: (BaseScore × Level) + (50 × ComboCount × Level)");
    }

    @Test
    void testMaximumScoreSingleMove() {
        // Maximum possible score in one move:
        // Tetris (4 lines) at level 10 with combo = 9
        for (int i = 0; i < 9; i++) {
            scoringSystem.calculateScore(1, 10); // Build combo to 9
        }

        int score = scoringSystem.calculateScore(4, 10);

        // (800 × 10) + (50 × 9 × 10) = 8000 + 4500 = 12500
        assertEquals(12500, score,
                "Maximum score: Tetris at level 10 with 9 combo");
    }
}