package com.tetris.data;

import com.tetris.gameLogic.MatrixOperations;

/**
 * An immutable Data Transfer Object (DTO) containing the state of moving elements.
 * <p>
 * While the board matrix stores the static (locked) blocks, this class captures
 * the dynamic elements that change every frame:
 * <ul>
 * <li>The currently falling brick (shape and position).</li>
 * <li>The "Ghost" piece (where the block will land).</li>
 * <li>The queue of upcoming bricks.</li>
 * </ul>
 * This separation allows the UI to render a frame safely without modifying the actual game state.
 *
 * @author Jacob Villegas
 */
public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int ghostYPosition;
    private final int[][][] nextBricksData;

    /**
     * Constructs a new snapshot of the moving game elements.
     *
     * @param brickData      The 2D matrix representing the shape of the currently falling piece.
     * @param xPosition      The current X coordinate (column) of the piece.
     * @param yPosition      The current Y coordinate (row) of the piece.
     * @param ghostYPosition The calculated Y coordinate where the piece would land if dropped instantly.
     * @param nextBricksData A 3D array containing the shape matrices of the next upcoming pieces.
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int ghostYPosition, int[][][] nextBricksData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.ghostYPosition = ghostYPosition;
        this.nextBricksData = nextBricksData;
    }

    /**
     * Retrieves a safe copy of the current brick's shape matrix.
     * <p>
     * Returns a deep copy to ensure the UI cannot accidentally modify the logic's
     * master reference of the shape.
     *
     * @return A 2D integer array representing the brick shape.
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Gets the current horizontal position of the falling piece.
     *
     * @return The column index.
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Gets the current vertical position of the falling piece.
     *
     * @return The row index.
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * Gets the Y coordinate for the "Ghost" piece.
     * <p>
     * This is used by the UI to draw a semi-transparent shadow at the bottom of the
     * column, helping the player aim.
     *
     * @return The row index where the piece would land.
     */
    public int getGhostYPosition() {
        return ghostYPosition;
    }

    /**
     * Retrieves the data for the upcoming pieces in the queue.
     * <p>
     * The data is structured as an array of 2D matrices (a 3D array),
     * where index 0 is the immediate next piece, index 1 is the one after that, etc.
     *
     * @return A 3D array of integer shape data.
     */
    public int[][][] getNextBricksData() {
        return nextBricksData;
    }
}