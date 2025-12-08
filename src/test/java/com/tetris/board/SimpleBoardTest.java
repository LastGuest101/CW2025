package com.tetris.board;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tetris.bricks.Brick;
import com.tetris.bricks.BrickGenerator;
import com.tetris.bricks.IBrick;
import com.tetris.bricks.JBrick;
import com.tetris.bricks.LBrick;
import com.tetris.bricks.OBrick;
import com.tetris.bricks.SBrick;
import com.tetris.bricks.TBrick;
import com.tetris.bricks.ZBrick;
import com.tetris.data.ViewData;
import com.tetris.gameLogic.ClearRow;

/**
 * Unit tests for the SimpleBoard class.
 * Tests brick movement, rotation, collision detection, row clearing, and game state management.
 *
 * @author Jacob Villegas
 */
class SimpleBoardTest {

    private SimpleBoard board;
    private MockBrickGenerator mockGenerator;

    /**
     * Mock brick generator for predictable testing.
     * Follows the same interface as RandomBrickGenerator but allows
     * setting specific brick sequences.
     */
    private static class MockBrickGenerator implements BrickGenerator {
        private final Deque<Brick> queue = new ArrayDeque<>();

        /**
         * Sets a specific brick to be returned next.
         */
        public void setNextBrick(Brick brick) {
            queue.clear();
            queue.add(brick);
            // Add some default bricks for preview
            queue.add(new IBrick());
            queue.add(new JBrick());
            queue.add(new LBrick());
        }

        /**
         * Sets a sequence of bricks.
         */
        public void setNextBricks(List<Brick> bricks) {
            queue.clear();
            queue.addAll(bricks);
        }

        @Override
        public Brick getBrick() {
            if (queue.isEmpty()) {
                return new IBrick(); // Default fallback
            }
            return queue.poll();
        }

        @Override
        public Brick getNextBrick() {
            if (queue.isEmpty()) {
                return new IBrick(); // Default fallback
            }
            return queue.peek();
        }

        @Override
        public List<Brick> getNextBricks(int count) {
            return queue.stream()
                    .limit(count)
                    .collect(Collectors.toList());
        }
    }

    @BeforeEach
    void setUp() {
        mockGenerator = new MockBrickGenerator();
        mockGenerator.setNextBrick(new IBrick()); // Start with I-brick by default
        board = new SimpleBoard(10, 20, mockGenerator);
        board.createNewBrick();
    }

    @Test
    void testInitialBoardEmpty() {
        board = new SimpleBoard(10, 20, mockGenerator);
        int[][] matrix = board.getBoardMatrix();

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(0, matrix[i][j],
                        "Initial board should be empty at [" + i + "][" + j + "]");
            }
        }
    }

    @Test
    void testInitialScoreZero() {
        board = new SimpleBoard(10, 20, mockGenerator);

        assertEquals(0, board.getScore().scoreProperty().get(),
                "Initial score should be 0");
    }


    @Test
    void testCreateNewBrickSpawnsAtTop() {
        mockGenerator.setNextBrick(new IBrick());
        board.createNewBrick();

        ViewData viewData = board.getViewData();

        assertEquals(4, viewData.getxPosition(),
                "Brick should spawn at X = 4 (center)");
        assertEquals(-1, viewData.getyPosition(),
                "Brick should spawn at Y = -1 (spawn zone)");
    }

    @Test
    void testCreateNewBrickReturnsGameOverWhenBlocked() {
        // Fill top rows to block spawn
        int[][] matrix = board.getBoardMatrix();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 10; j++) {
                matrix[i][j] = 1;
            }
        }

        mockGenerator.setNextBrick(new IBrick());
        boolean gameOver = board.createNewBrick();

        assertTrue(gameOver,
                "Creating brick when spawn blocked should return true (game over)");
    }

    @Test
    void testCreateNewBrickReturnsNoGameOverWhenClear() {
        mockGenerator.setNextBrick(new IBrick());
        boolean gameOver = board.createNewBrick();

        assertFalse(gameOver,
                "Creating brick in clear spawn zone should return false");
    }


    @Test
    void testMoveBrickDownValid() {
        boolean moved = board.moveBrickDown();

        assertTrue(moved,
                "Brick should move down when path is clear");
    }

    @Test
    void testMoveBrickDownToBottom() {
        // Move brick down until it hits bottom
        int moveCount = 0;
        while (board.moveBrickDown()) {
            moveCount++;
        }

        assertTrue(moveCount > 0,
                "Brick should move multiple times before hitting bottom");
    }

    @Test
    void testMoveBrickLeftValid() {
        board.moveBrickDown(); // Move away from spawn zone
        boolean moved = board.moveBrickLeft();

        assertTrue(moved,
                "Brick should move left when path is clear");
    }

    @Test
    void testMoveBrickLeftBlockedByWall() {
        board.moveBrickDown(); // Move to visible area

        // Move left until hitting wall
        while (board.moveBrickLeft()) {
            // Keep moving
        }

        boolean blocked = !board.moveBrickLeft();

        assertTrue(blocked,
                "Brick should be blocked by left wall");
    }

    @Test
    void testMoveBrickRightValid() {
        board.moveBrickDown(); // Move away from spawn zone
        boolean moved = board.moveBrickRight();

        assertTrue(moved,
                "Brick should move right when path is clear");
    }

    @Test
    void testMoveBrickRightBlockedByWall() {
        board.moveBrickDown(); // Move to visible area

        // Move right until hitting wall
        while (board.moveBrickRight()) {
            // Keep moving
        }

        boolean blocked = !board.moveBrickRight();

        assertTrue(blocked,
                "Brick should be blocked by right wall");
    }

    @Test
    void testMoveBrickDownBlockedByFloor() {
        // Move brick all the way down
        while (board.moveBrickDown()) {
            // Keep moving
        }

        boolean blockedAtBottom = !board.moveBrickDown();

        assertTrue(blockedAtBottom,
                "Brick should be blocked at bottom");
    }

    @Test
    void testMoveBrickBlockedByExistingBlocks() {
        // Place a block in the path
        int[][] matrix = board.getBoardMatrix();
        matrix[10][5] = 1;

        // Move brick to position above the block
        for (int i = 0; i < 9; i++) {
            board.moveBrickDown();
        }

        // Try to move into the block
        boolean blocked = !board.moveBrickDown();

        assertTrue(blocked,
                "Brick should be blocked by existing blocks");
    }

    @Test
    void testMoveBrickPositionUpdates() {
        ViewData before = board.getViewData();
        int yBefore = before.getyPosition();

        board.moveBrickDown();

        ViewData after = board.getViewData();
        int yAfter = after.getyPosition();

        assertEquals(yBefore + 1, yAfter,
                "Y position should increase by 1 after moving down");
    }

    @Test
    void testRotateLeftBrickValid() {
        mockGenerator.setNextBrick(new IBrick());
        board.createNewBrick();
        board.moveBrickDown(); // Move to visible area

        boolean rotated = board.rotateLeftBrick();

        assertTrue(rotated,
                "Brick should rotate when space is clear");
    }

    @Test
    void testRotateLeftBrickWithWallKick() {
        mockGenerator.setNextBrick(new IBrick());
        board.createNewBrick();

        while (board.moveBrickLeft()) {
            // Keep moving
        }
        board.moveBrickDown(); // Move to visible area

        // Try to rotate (should use wall kick)
        boolean rotated = board.rotateLeftBrick();

        assertTrue(rotated,
                "Brick should rotate using wall kick when against wall");
    }

    @Test
    void testRotateLeftBrickBlockedByBlocks() {
        int[][] matrix = board.getBoardMatrix();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                matrix[i][j] = 1;
            }
        }

        mockGenerator.setNextBrick(new IBrick());
        board.createNewBrick();

        boolean rotated = board.rotateLeftBrick();

        assertFalse(rotated,
                "Brick should not rotate when completely blocked");
    }

    @Test
    void testRotateOBrickAlwaysSucceeds() {
        mockGenerator.setNextBrick(new OBrick());
        board.createNewBrick();
        board.moveBrickDown(); // Move to visible area

        boolean rotated = board.rotateLeftBrick();

        assertTrue(rotated,
                "O-brick rotation should always succeed (no shape change)");
    }

    @Test
    void testMergeBrickToBackground() {
        mockGenerator.setNextBrick(new OBrick());
        board.createNewBrick();

        // Move brick to bottom
        while (board.moveBrickDown()) {
            // Keep moving
        }

        ViewData beforeMerge = board.getViewData();
        int xPos = beforeMerge.getxPosition();
        int yPos = beforeMerge.getyPosition();

        board.mergeBrickToBackground();

        int[][] matrix = board.getBoardMatrix();
        boolean hasBlocks = false;

        // Check if any cells are filled in the brick's area
        for (int i = yPos; i < yPos + 2 && i < 20; i++) {
            for (int j = xPos; j < xPos + 2 && j < 10; j++) {
                if (i >= 0 && matrix[i][j] != 0) {
                    hasBlocks = true;
                    break;
                }
            }
        }

        assertTrue(hasBlocks,
                "Brick should be merged into background matrix");
    }


    @Test
    void testClearRowsWithNoFullRows() {
        ClearRow result = board.clearRows(1);

        assertEquals(0, result.getLinesRemoved(),
                "Should not clear any rows when none are full");
    }

    @Test
    void testClearRowsWithSingleFullRow() {
        int[][] matrix = board.getBoardMatrix();

        // Fill bottom row
        for (int i = 0; i < 10; i++) {
            matrix[19][i] = 1;
        }

        ClearRow result = board.clearRows(1);

        assertEquals(1, result.getLinesRemoved(),
                "Should clear 1 full row");

        // Check bottom row is now empty
        int[][] newMatrix = result.getNewMatrix();
        for (int i = 0; i < 10; i++) {
            assertEquals(0, newMatrix[19][i],
                    "Bottom row should be empty after clear");
        }
    }

    @Test
    void testClearRowsWithMultipleFullRows() {
        int[][] matrix = board.getBoardMatrix();

        // Fill bottom 3 rows
        for (int i = 17; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                matrix[i][j] = 1;
            }
        }

        ClearRow result = board.clearRows(1);

        assertEquals(3, result.getLinesRemoved(),
                "Should clear 3 full rows");
    }

    @Test
    void testClearRowsReturnsCorrectScore() {
        int[][] matrix = board.getBoardMatrix();

        // Fill bottom row
        for (int i = 0; i < 10; i++) {
            matrix[19][i] = 1;
        }

        ClearRow result = board.clearRows(2); // Level 2

        // Single line = 100 × level = 100 × 2 = 200
        assertEquals(200, result.getScoreBonus(),
                "Score should be 200 for 1 line at level 2");
    }

    @Test
    void testClearRowsShiftsRowsDown() {
        int[][] matrix = board.getBoardMatrix();

        // Place marker block above clear zone
        matrix[17][5] = 9;

        // Fill bottom row
        for (int i = 0; i < 10; i++) {
            matrix[19][i] = 1;
        }

        ClearRow result = board.clearRows(1);
        int[][] newMatrix = result.getNewMatrix();

        assertEquals(9, newMatrix[18][5],
                "Block should shift down after row clear");
    }

    @Test
    void testClearRowsReturnsClearedIndices() {
        int[][] matrix = board.getBoardMatrix();

        // Fill row 18
        for (int i = 0; i < 10; i++) {
            matrix[18][i] = 1;
        }

        ClearRow result = board.clearRows(1);

        assertTrue(result.getClearedIndices().contains(18),
                "Cleared indices should contain row 18");
    }

    @Test
    void testClearRowsReturnsRowData() {
        int[][] matrix = board.getBoardMatrix();

        // Fill bottom row with specific colors
        for (int i = 0; i < 10; i++) {
            matrix[19][i] = i + 1;
        }

        ClearRow result = board.clearRows(1);
        List<int[]> rowData = result.getClearedRowsData();

        assertEquals(1, rowData.size(),
                "Should return data for 1 cleared row");
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, rowData.get(0),
                "Row data should match original colors");
    }


    @Test
    void testGhostPositionAtBottom() {
        mockGenerator.setNextBrick(new OBrick());
        board.createNewBrick();

        ViewData viewData = board.getViewData();
        int ghostY = viewData.getGhostYPosition();

        // Move brick to ghost position
        while (board.moveBrickDown()) {
            // Keep moving
        }

        ViewData finalData = board.getViewData();

        assertEquals(ghostY, finalData.getGhostYPosition(),
                "Ghost position should match final brick position");
    }

    @Test
    void testGhostPositionUpdatesOnMove() {
        mockGenerator.setNextBrick(new OBrick());
        board.createNewBrick();

        ViewData before = board.getViewData();
        int ghostBefore = before.getGhostYPosition();

        // Move brick down
        board.moveBrickDown();

        ViewData after = board.getViewData();
        int ghostAfter = after.getGhostYPosition();

        assertEquals(ghostBefore, ghostAfter,
                "Ghost position should not change when brick moves down toward it");
    }

    @Test
    void testGhostPositionWithBlockedPath() {
        // Place block in middle of board
        int[][] matrix = board.getBoardMatrix();
        for (int i = 0; i < 10; i++) {
            matrix[10][i] = 1;
        }

        mockGenerator.setNextBrick(new OBrick());
        board.createNewBrick();

        ViewData viewData = board.getViewData();

        assertTrue(viewData.getGhostYPosition() < 10,
                "Ghost should stop above blocking row");
    }

    @Test
    void testNewGameClearsBoard() {
        // Fill board with blocks
        int[][] matrix = board.getBoardMatrix();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                matrix[i][j] = 1;
            }
        }

        board.newGame();

        int[][] newMatrix = board.getBoardMatrix();
        boolean isEmpty = true;

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                if (newMatrix[i][j] != 0) {
                    isEmpty = false;
                    break;
                }
            }
        }

        assertTrue(isEmpty,
                "Board should be empty after new game");
    }

    @Test
    void testNewGameResetsScore() {
        board.getScore().add(5000);

        board.newGame();

        assertEquals(0, board.getScore().scoreProperty().get(),
                "Score should reset to 0 on new game");
    }

    @Test
    void testNewGameSpawnsFirstBrick() {
        board.newGame();

        ViewData viewData = board.getViewData();

        assertNotNull(viewData.getBrickData(),
                "New game should spawn a brick");
        assertEquals(4, viewData.getxPosition(),
                "Brick should spawn at center X");
    }

    @Test
    void testMovementInSpawnZone() {
        // Brick starts at Y = -1 (spawn zone)
        boolean canMoveLeft = board.moveBrickLeft();
        boolean canMoveRight = board.moveBrickRight();

        assertTrue(canMoveLeft || canMoveRight,
                "Brick should be able to move in spawn zone");
    }

    @Test
    void testRotationInSpawnZone() {
        mockGenerator.setNextBrick(new IBrick());
        board.createNewBrick();

        boolean canRotate = board.rotateLeftBrick();

        assertTrue(canRotate,
                "Brick should be able to rotate in spawn zone");
    }

    @Test
    void testMultipleClearsInOneMove() {
        int[][] matrix = board.getBoardMatrix();

        // Fill bottom 4 rows (Tetris!)
        for (int i = 16; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                matrix[i][j] = 1;
            }
        }

        ClearRow result = board.clearRows(1);

        assertEquals(4, result.getLinesRemoved(),
                "Should clear all 4 rows simultaneously (Tetris)");
    }

    @Test
    void testDifferentBrickTypes() {
        List<Brick> allBricks = Arrays.asList(
                new IBrick(), new JBrick(), new LBrick(), new OBrick(),
                new SBrick(), new TBrick(), new ZBrick()
        );

        for (Brick brick : allBricks) {
            mockGenerator.setNextBrick(brick);
            board.createNewBrick();

            ViewData viewData = board.getViewData();

            assertNotNull(viewData.getBrickData(),
                    brick.getClass().getSimpleName() + " should spawn correctly");
        }
    }
}