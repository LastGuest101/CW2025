package com.tetris.gameLogic;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the MatrixOperations class.
 * Tests collision detection, matrix manipulation, row clearing, and utility methods.
 *
 * @author Jacob Villegas
 */
class MatrixOperationsTest {

    // ========== Collision Detection Tests (intersect) ==========

    @Test
    void testIntersectNoBoundaryNoCollision() {
        int[][] board = new int[20][10];
        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        boolean result = MatrixOperations.intersect(board, brick, 4, 5);

        assertFalse(result,
                "Brick at valid position should not intersect");
    }

    @Test
    void testIntersectLeftWall() {
        int[][] board = new int[20][10];
        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        boolean result = MatrixOperations.intersect(board, brick, -1, 5);

        assertTrue(result,
                "Brick overlapping left wall should intersect");
    }

    @Test
    void testIntersectRightWall() {
        int[][] board = new int[20][10];
        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        boolean result = MatrixOperations.intersect(board, brick, 9, 5);

        assertTrue(result,
                "Brick overlapping right wall should intersect");
    }

    @Test
    void testIntersectBottomFloor() {
        int[][] board = new int[20][10];
        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        boolean result = MatrixOperations.intersect(board, brick, 4, 19);

        assertTrue(result,
                "Brick overlapping bottom should intersect");
    }

    @Test
    void testIntersectWithExistingBlock() {
        int[][] board = new int[20][10];
        board[10][5] = 1; // Occupied cell
        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        boolean result = MatrixOperations.intersect(board, brick, 4, 9);

        assertTrue(result,
                "Brick overlapping existing block should intersect");
    }

    @Test
    void testIntersectAtTopEdge() {
        int[][] board = new int[20][10];
        board[0][4] = 1; // Block at very top
        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        boolean result = MatrixOperations.intersect(board, brick, 4, 0);

        assertTrue(result,
                "Brick hitting top row blocks should intersect");
    }

    @Test
    void testCopyCreatesIndependentMatrix() {
        int[][] original = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        int[][] copy = MatrixOperations.copy(original);

        assertNotSame(original, copy,
                "Copy should be a different object reference");
    }

    @Test
    void testCopyHasIdenticalValues() {
        int[][] original = {
                {1, 2, 3},
                {4, 5, 6}
        };

        int[][] copy = MatrixOperations.copy(original);

        assertArrayEquals(original, copy,
                "Copy should have identical values");
    }

    @Test
    void testCopyIsDeep() {
        int[][] original = {
                {1, 2, 3},
                {4, 5, 6}
        };

        int[][] copy = MatrixOperations.copy(original);
        copy[0][0] = 999; // Modify copy

        assertEquals(1, original[0][0],
                "Modifying copy should not affect original (deep copy)");
    }

    @Test
    void testCopyEmptyMatrix() {
        int[][] original = new int[0][0];

        int[][] copy = MatrixOperations.copy(original);

        assertEquals(0, copy.length,
                "Empty matrix copy should also be empty");
    }

    @Test
    void testCopyLargeMatrix() {
        int[][] original = new int[20][10];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                original[i][j] = i * 10 + j;
            }
        }

        int[][] copy = MatrixOperations.copy(original);

        assertArrayEquals(original, copy,
                "Large matrix copy should be identical");
    }

    @Test
    void testMergeSimpleBrick() {
        int[][] board = new int[5][5];
        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        int[][] result = MatrixOperations.merge(board, brick, 1, 1);

        assertEquals(1, result[1][1], "Cell [1][1] should be filled");
        assertEquals(1, result[1][2], "Cell [1][2] should be filled");
        assertEquals(1, result[2][1], "Cell [2][1] should be filled");
        assertEquals(1, result[2][2], "Cell [2][2] should be filled");
    }

    @Test
    void testMergeDoesNotModifyOriginal() {
        int[][] board = new int[5][5];
        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        MatrixOperations.merge(board, brick, 1, 1);

        assertEquals(0, board[1][1],
                "Original board should not be modified");
    }

    @Test
    void testMergeWithExistingBlocks() {
        int[][] board = new int[5][5];
        board[0][0] = 2; // Existing block
        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        int[][] result = MatrixOperations.merge(board, brick, 1, 1);

        assertEquals(2, result[0][0],
                "Existing block should remain");
        assertEquals(1, result[1][1],
                "New brick should be added");
    }

    @Test
    void testMergePartiallyOutOfBounds() {
        int[][] board = new int[5][5];
        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        // Brick partially above board (y = -1)
        int[][] result = MatrixOperations.merge(board, brick, 1, -1);

        assertEquals(1, result[0][1],
                "Only visible part should merge");
        assertEquals(0, result[1][1],
                "Other cells should remain empty");
    }

    @Test
    void testCheckRemovingSingleRow() {
        int[][] board = new int[5][5];
        // Fill bottom row
        for (int i = 0; i < 5; i++) {
            board[4][i] = 1;
        }

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(1, result.getLinesRemoved(),
                "Should detect 1 full row");
        assertEquals(0, result.getNewMatrix()[4][0],
                "Bottom row should be empty after clear");
    }

    @Test
    void testCheckRemovingMultipleRows() {
        int[][] board = new int[5][5];
        // Fill bottom 3 rows
        for (int i = 2; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                board[i][j] = 1;
            }
        }

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(3, result.getLinesRemoved(),
                "Should detect 3 full rows");
    }

    @Test
    void testCheckRemovingNoRows() {
        int[][] board = new int[5][5];
        board[4][0] = 1; // Partial row

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(0, result.getLinesRemoved(),
                "Partial rows should not be cleared");
    }

    @Test
    void testCheckRemovingReturnsCorrectIndices() {
        int[][] board = new int[5][5];
        for (int i = 0; i < 5; i++) {
            board[3][i] = 1; // Fill row 3
        }

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertTrue(result.getClearedIndices().contains(3),
                "Cleared indices should contain row 3");
        assertEquals(1, result.getClearedIndices().size(),
                "Should have exactly 1 cleared index");
    }

    @Test
    void testCheckRemovingBoardShiftsDown() {
        int[][] board = new int[5][5];
        board[2][2] = 2; // Block above clear
        for (int i = 0; i < 5; i++) {
            board[3][i] = 1; // Full row
        }

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(2, result.getNewMatrix()[3][2],
                "Block should shift down after clear");
    }

    @Test
    void testCheckRemovingEmptyRowsAddedAtTop() {
        int[][] board = new int[5][5];
        // Fill all rows
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                board[i][j] = 1;
            }
        }

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(5, result.getLinesRemoved(),
                "All 5 rows should be cleared");
        assertEquals(0, result.getNewMatrix()[0][0],
                "Top rows should be empty");
    }

    @Test
    void testCheckRemovingClearedRowDataCapture() {
        int[][] board = new int[5][5];
        // Fill row 3 with specific colors
        for (int i = 0; i < 5; i++) {
            board[3][i] = i + 1; // Colors: 1, 2, 3, 4, 5
        }

        ClearRow result = MatrixOperations.checkRemoving(board);

        List<int[]> clearedData = result.getClearedRowsData();
        assertEquals(1, clearedData.size(),
                "Should capture 1 cleared row");
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, clearedData.get(0),
                "Cleared row data should match original colors");
    }

    @Test
    void testCheckRemovingNonContiguousRows() {
        int[][] board = new int[5][5];
        // Fill rows 1 and 3 (not adjacent)
        for (int i = 0; i < 5; i++) {
            board[1][i] = 1;
            board[3][i] = 1;
        }

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(2, result.getLinesRemoved(),
                "Should clear non-contiguous rows");
        assertTrue(result.getClearedIndices().contains(1),
                "Should include row 1");
        assertTrue(result.getClearedIndices().contains(3),
                "Should include row 3");
    }

    @Test
    void testCheckRemovingPartiallyFilledRows() {
        int[][] board = new int[5][5];
        board[4][0] = 1;
        board[4][1] = 1;
        board[4][2] = 1;
        // Row 4 has 3/5 blocks (not full)

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(0, result.getLinesRemoved(),
                "Partially filled rows should not clear");
    }

    @Test
    void testCheckRemovingTopRowClear() {
        int[][] board = new int[5][5];
        // Fill top row
        for (int i = 0; i < 5; i++) {
            board[0][i] = 1;
        }

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(1, result.getLinesRemoved(),
                "Top row should be clearable");
        assertEquals(0, result.getNewMatrix()[0][0],
                "Top row should be empty after clear");
    }

    @Test
    void testDeepCopyListEmptyList() {
        List<int[][]> original = new ArrayList<>();

        List<int[][]> copy = MatrixOperations.deepCopyList(original);

        assertEquals(0, copy.size(),
                "Empty list copy should also be empty");
    }

    @Test
    void testDeepCopyListMultipleMatrices() {
        List<int[][]> original = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            original.add(new int[][]{{i, i + 1}, {i + 2, i + 3}});
        }

        List<int[][]> copy = MatrixOperations.deepCopyList(original);

        assertEquals(3, copy.size(),
                "Copy should have same number of matrices");
        assertArrayEquals(original.get(1), copy.get(1),
                "Second matrix should be identical");
    }

    @Test
    void testIntersectWith1x1Brick() {
        int[][] board = new int[5][5];
        int[][] brick = {{1}};

        boolean result = MatrixOperations.intersect(board, brick, 2, 2);

        assertFalse(result,
                "1x1 brick at valid position should not intersect");
    }

    @Test
    void testMergeWith1x1Brick() {
        int[][] board = new int[5][5];
        int[][] brick = {{1}};

        int[][] result = MatrixOperations.merge(board, brick, 2, 2);

        assertEquals(1, result[2][2],
                "1x1 brick should merge correctly");
    }

    @Test
    void testCheckRemovingFullBoard() {
        int[][] board = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = 1;
            }
        }

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(3, result.getLinesRemoved(),
                "All rows should be cleared");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(0, result.getNewMatrix()[i][j],
                        "Board should be completely empty");
            }
        }
    }

    @Test
    void testIntersectExactBoundary() {
        int[][] board = new int[10][10];
        int[][] brick = {{1}};

        // Test exact right boundary
        assertFalse(MatrixOperations.intersect(board, brick, 9, 5),
                "Brick at rightmost column should be valid");

        // Test one past right boundary
        assertTrue(MatrixOperations.intersect(board, brick, 10, 5),
                "Brick past right boundary should intersect");
    }
}