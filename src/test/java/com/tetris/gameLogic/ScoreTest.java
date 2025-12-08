package com.tetris.gameLogic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Score} class to ensure correct point tracking.
 * <p>
 * This class verifies the maths and state operations of the scoring system:

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
}

