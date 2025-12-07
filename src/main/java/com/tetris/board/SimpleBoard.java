package com.tetris.board;

import com.tetris.data.ViewData;
import com.tetris.gameLogic.*;
import com.tetris.bricks.Brick;
import com.tetris.bricks.BrickGenerator;

import java.awt.*;
/**
 * Represents the core game logic board for Tetris.
 * <p>
 * This class manages the 2D grid of the game (the matrix), the active falling piece,
 * collision detection, movement validation, scoring, and the "ghost" piece calculations.
 * It serves as the bridge between the raw data and the GameController.
 * @author Jacob Villegas
 */
public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private final ScoringSystem scoringSystem;
    private static final int SPAWN_X = 4;
    private static final int SPAWN_Y = -1;

    /**
     * Constructs a new SimpleBoard with the specified dimensions and brick generator.
     *
     * @param width                The width of the game board (in blocks).
     * @param height               The height of the game board (in blocks).
     * @param RandomBrickGenerator The source for generating new Tetris bricks.
     */
    public SimpleBoard(int width, int height, BrickGenerator RandomBrickGenerator) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[height][width];
        this.brickGenerator = RandomBrickGenerator;
        this.brickRotator = new BrickRotator();
        score = new Score();
       this.scoringSystem = new ScoringSystem();
    }

    /**
     * Helper: Checks if placing the given shape at the target coordinates is valid.
     * <p>
     * It checks for boundary violations (walls, floor) and collisions with
     * existing locked blocks in the matrix.
     *
     * @param shape   The 2D array representation of the brick shape.
     * @param targetX The target X coordinate (column).
     * @param targetY The target Y coordinate (row).
     * @return {@code true} if the move is valid (no conflict); {@code false} otherwise.
     */
    private boolean isMoveValid(int[][] shape, int targetX, int targetY) {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        boolean conflict = MatrixOperations.intersect(currentMatrix, shape, targetX, targetY);
        return !conflict;
    }

    /**
     * Helper: Attempts to move the current active shape by a given delta.
     * <p>
     * If the move is valid, the {@code currentOffset} is updated.
     *
     * @param dx The change in X (horizontal).
     * @param dy The change in Y (vertical).
     * @return {@code true} if the move was successful; {@code false} if blocked.
     */
    private boolean attemptMove(int dx, int dy) {
        Point p = new Point(currentOffset);
        p.translate(dx, dy);

        if (isMoveValid(brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY())) {
            currentOffset = p;
            return true;
        }
        return false;
    }


    /**
     * Moves the active brick down by one unit.
     *
     * @return {@code true} if the brick moved down successfully; {@code false} if it hit the bottom or another piece.
     */
    @Override
    public boolean moveBrickDown() {
        return attemptMove(0, 1);
    }

    /**
     * Moves the active brick to the left by one unit.
     *
     * @return {@code true} if the move was successful; {@code false} if blocked by a wall or piece.
     */
    @Override
    public boolean moveBrickLeft() {
        return attemptMove(-1, 0);
    }

    /**
     * Moves the active brick to the right by one unit.
     *
     * @return {@code true} if the move was successful; {@code false} if blocked by a wall or piece.
     */
    @Override
    public boolean moveBrickRight() {
        return attemptMove(1, 0);
    }

    /**
     * Attempts to rotate the active brick to the left (counter-clockwise).
     * <p>
     * This method implements a basic "Wall Kick" system. If the rotation is blocked
     * by a wall or an adjacent piece, it attempts to shift the piece slightly (the "kick")
     * to find a valid position for the rotation.
     *
     * @return {@code true} if the rotation (with or without a kick) was successful; {@code false} if rotation is impossible.
     */
    @Override
    public boolean rotateLeftBrick() {
        NextShapeInfo nextShape = brickRotator.getNextShape();
        int[][] newShape = nextShape.getShape();
        int currentX = (int) currentOffset.getX();
        int currentY = (int) currentOffset.getY();

        // For normal rotations
        if (isMoveValid(newShape, currentX, currentY)) {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }

        // For wall rotations
        // If the rotation fails, shift the piece to the right and left and see if valid
        int[] kicks = {1, -1, 2, -2};

        for (int dx : kicks) {
            if (isMoveValid(newShape, currentX + dx, currentY)) {
                currentOffset.translate(dx, 0); // Apply the kick to the position
                brickRotator.setCurrentShape(nextShape.getPosition());
                return true;
            }
        }

        // If all kicks fail, the rotation is not allowed
        return false;
    }

    /**
     * Spawns a new brick at the top of the board.
     * <p>
     * Retrieves a new brick from the generator, sets it to the spawn coordinates,
     * and checks for immediate collision (Game Over state).
     *
     * @return {@code true} if the new brick overlaps with existing blocks (Game Over);
     * {@code false} if the spawn was clean.
     */
    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(SPAWN_X, SPAWN_Y);
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Retrieves the current state of the game board matrix.
     *
     * @return A 2D integer array representing the locked blocks on the board.
     */
    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    /**
     * Compiles all data required for the UI to render the game frame.
     *
     * @return A {@link ViewData} object containing the current shape, its position,
     * the ghost piece Y-coordinate, and previews of the next 3 bricks.
     */
    @Override
    public ViewData getViewData() {
        var nextBricks = brickGenerator.getNextBricks(3);

        int[][][] nextBricksData = new int[nextBricks.size()][][];
        for (int i = 0; i < nextBricks.size(); i++) {
            nextBricksData[i] = nextBricks.get(i).getShapeMatrix().get(0);
        }

        return new ViewData(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                getGhostY(),
                nextBricksData
        );
    }

    /**
     * Locks the current active brick into the background matrix.
     * <p>
     * This is typically called when the brick can no longer move down.
     */
    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Checks for completed rows, removes them, and updates the score.
     *
     * @param currentLevel The current game level (affects scoring multipliers).
     * @return A {@link ClearRow} object containing details about cleared lines, the new matrix state, and points earned.
     */
    @Override
    public ClearRow clearRows(int currentLevel) {
        ClearRow result = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = result.getNewMatrix();
        int lines = result.getLinesRemoved();
        int points = scoringSystem.calculateScore(lines, currentLevel);
        return new ClearRow(lines, result.getNewMatrix(), points,
                result.getClearedIndices(), result.getClearedRowsData());
    }

    /**
     * Gets the current game score object.
     *
     * @return The Score object tracking points.
     */
    @Override
    public Score getScore() {
        return score;
    }

    /**
     * Resets the board state for a new game.
     * <p>
     * Clears the matrix, resets the score and scoring system, and spawns the first brick.
     */
    @Override
    public void newGame() {
        currentGameMatrix = new int[height][width];
        score.reset();
        scoringSystem.reset();
        createNewBrick();
    }

    /**
     * Calculates the Y-coordinate where the current piece would land if dropped instantly.
     * <p>
     * This is used to render the "Ghost Piece" (shadow) to help player accuracy.
     *
     * @return The Y-coordinate for the ghost piece.
     * @author Jacob Villegas
     */
    private int getGhostY() {
        int currentX = (int) currentOffset.getX();
        int ghostY = (int) currentOffset.getY();
        int[][] shape = brickRotator.getCurrentShape();

        // Checks the piece is already in an invalid position (Game Over state),
        // doesn't try to calculate a ghost. Just return the current Y.
        if (!isMoveValid(shape, currentX, ghostY)) {
            return ghostY;
        }

        while (isMoveValid(shape, currentX, ghostY + 1)) {
            ghostY++;
        }
        return ghostY;
    }
}
