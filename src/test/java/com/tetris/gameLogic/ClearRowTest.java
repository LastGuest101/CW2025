package com.tetris.gameLogic;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ClearRowTest {
    @Test
    void noClearTest() {
        int[][] initialMatrix = {
                {1, 1, 1, 0},
                {1, 1, 0, 1},
                {1, 0, 1, 1},
                {0, 1, 1, 1}
        };
        ClearRow clearRow = MatrixOperations.checkRemoving(initialMatrix);
        ScoringSystem ScoringSystem = new ScoringSystem();
        int linesRemoved = clearRow.getLinesRemoved();
        int [][] newMatrix = clearRow.getNewMatrix();
        int scoreBonus = ScoringSystem.calculateScore(linesRemoved, 1);

        assertEquals(0, linesRemoved);
        assertArrayEquals(initialMatrix, newMatrix);
        assertEquals(0, scoreBonus);
    }

    @Test
    void oneLineClearTest() {
        int[][] initialMatrix = {
                {1, 0, 1, 0},
                {1, 1, 0, 1},
                {1, 1, 1, 1},
                {0, 1, 1, 1}
        };

        int[][] expectedMatrix = {
                {0, 0, 0, 0},
                {1, 0, 1, 0},
                {1, 1, 0, 1},
                {0, 1, 1, 1}
        };
        ClearRow clearRow = MatrixOperations.checkRemoving(initialMatrix);
        ScoringSystem ScoringSystem = new ScoringSystem();
        int linesRemoved = clearRow.getLinesRemoved();
        int [][] newMatrix = clearRow.getNewMatrix();
        int scoreBonus = ScoringSystem.calculateScore(linesRemoved, 1);

        assertEquals(1, linesRemoved);
        assertArrayEquals(expectedMatrix, newMatrix);
        assertEquals(100, scoreBonus);
    }

    @Test
    void multipleLineClearTest() {
        int[][] initialMatrix = {
                {1, 0, 1, 0},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1}
        };

        int[][] expectedMatrix = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {1, 0, 1, 0}
        };
        ClearRow clearRow = MatrixOperations.checkRemoving(initialMatrix);
        ScoringSystem ScoringSystem = new ScoringSystem();
        int linesRemoved = clearRow.getLinesRemoved();
        int [][] newMatrix = clearRow.getNewMatrix();
        int scoreBonus = ScoringSystem.calculateScore(linesRemoved, 1);

        assertEquals(3, linesRemoved);
        assertArrayEquals(expectedMatrix, newMatrix);
        assertEquals(500, scoreBonus);
    }

    @Test
    void bottomRowClearTest() {
        int[][] initialMatrix = {
                {0, 0, 0, 0},
                {1, 0, 1, 0},
                {1, 1, 0, 1},
                {1, 1, 1, 1}
        };

        int[][] expectedMatrix = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {1, 0, 1, 0},
                {1, 1, 0, 1}
        };
        ClearRow clearRow = MatrixOperations.checkRemoving(initialMatrix);
        ScoringSystem ScoringSystem = new ScoringSystem();
        int linesRemoved = clearRow.getLinesRemoved();
        int [][] newMatrix = clearRow.getNewMatrix();
        int scoreBonus = ScoringSystem.calculateScore(linesRemoved, 1);

        assertEquals(1, linesRemoved);
        assertArrayEquals(expectedMatrix, newMatrix);
        assertEquals(100, scoreBonus);
    }

    @Test
    void topLineClearTest() {
        int[][] initialMatrix = {
                {1, 1, 1, 1},
                {0, 1, 0, 0},
                {1, 0, 1, 0},
                {0, 1, 0, 1}
        };

        int[][] expectedMatrix = {
                {0, 0, 0, 0},
                {0, 1, 0, 0},
                {1, 0, 1, 0},
                {0, 1, 0, 1}
        };
        ClearRow clearRow = MatrixOperations.checkRemoving(initialMatrix);
        ScoringSystem ScoringSystem = new ScoringSystem();
        int linesRemoved = clearRow.getLinesRemoved();
        int [][] newMatrix = clearRow.getNewMatrix();
        int scoreBonus = ScoringSystem.calculateScore(linesRemoved, 1);

        assertEquals(1, linesRemoved);
        assertArrayEquals(expectedMatrix, newMatrix);
        assertEquals(100, scoreBonus);
    }

    @Test
    void nonContiguousRowsClearTest() {
        int[][] initialMatrix = {
                {1, 1, 1, 1},
                {0, 1, 0, 1},
                {1, 1, 1, 1},
                {1, 0, 1, 0}
        };

        int[][] expectedMatrix = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 1, 0, 1},
                {1, 0, 1, 0}
        };

        ClearRow clearRow = MatrixOperations.checkRemoving(initialMatrix);
        ScoringSystem ScoringSystem = new ScoringSystem();
        int linesRemoved = clearRow.getLinesRemoved();
        int [][] newMatrix = clearRow.getNewMatrix();
        int scoreBonus = ScoringSystem.calculateScore(linesRemoved, 1);

        assertEquals(2, clearRow.getLinesRemoved());
        assertArrayEquals(expectedMatrix, newMatrix);
        assertEquals(300, scoreBonus);
    }

    @Test
    void fourLineTetrisTest() {
        int[][] initialMatrix = {
                {1,1,1,1},
                {1,1,1,1},
                {1,1,1,1},
                {1,1,1,1}
        };

        int[][] expectedMatrix = {
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0}
        };

        ClearRow clearRow = MatrixOperations.checkRemoving(initialMatrix);
        ScoringSystem ScoringSystem = new ScoringSystem();
        int linesRemoved = clearRow.getLinesRemoved();
        int [][] newMatrix = clearRow.getNewMatrix();
        int scoreBonus = ScoringSystem.calculateScore(linesRemoved, 1);

        assertEquals(4, clearRow.getLinesRemoved());
        assertArrayEquals(expectedMatrix, newMatrix);
        assertEquals(800, scoreBonus);
    }
}