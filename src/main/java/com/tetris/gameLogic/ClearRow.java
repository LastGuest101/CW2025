package com.tetris.gameLogic;

import java.util.ArrayList;
import java.util.List;

/**
 * A record representing the result of a row-clearing operation.
 * <p>
 * When the game checks for completed lines, this class packages all relevant data:
 * how many lines were cleared, the updated state of the board, the score earned,
 * and specific details about which rows were removed (useful for visual effects/animations).
 *
 * @author Jacob Villegas
 */
public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;
    private final List<Integer> clearedIndices;
    private final List<int[]> clearedRowsData;

    /**
     * Constructs a comprehensive ClearRow result.
     *
     * @param linesRemoved    The total count of rows cleared in this step.
     * @param newMatrix       The new state of the board matrix after clearing and shifting rows.
     * @param scoreBonus      The points awarded for this specific clear action.
     * @param clearedIndices  A list of row indices (0 to height-1) that were cleared.
     * @param clearedRowsData A list containing the actual block data of the cleared rows
     * (useful for creating "dissolving" animations).
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus, List<Integer> clearedIndices, List<int[]> clearedRowsData) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
        this.clearedIndices = clearedIndices;
        this.clearedRowsData = clearedRowsData;
    }

    /**
     * Constructs a simplified ClearRow result (primarily for testing).
     * <p>
     * Initializes score bonus to 0 and clears data lists to empty.
     *
     * @param linesRemoved The total count of rows cleared.
     * @param newMatrix    The new state of the board matrix.
     */
    // Constructor for tests (defaults to empty)
    public ClearRow(int linesRemoved, int[][] newMatrix) {
        this(linesRemoved, newMatrix, 0, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Gets the number of lines that were removed.
     *
     * @return The count of cleared lines (e.g., 1, 2, 3, or 4 for a Tetris).
     */
    public int getLinesRemoved() {
        return linesRemoved; }
    /**
     * Gets a safe copy of the new board matrix.
     * <p>
     * Returns a copy to ensure immutability of the internal state.
     *
     * @return A 2D array representing the board after lines have been cleared and dropped.
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix); }
    /**
     * Gets the score calculated for this specific clear.
     *
     * @return The points earned.
     */
    public int getScoreBonus() {
        return scoreBonus; }

    /**
     * Gets the indices of the rows that were cleared.
     * <p>
     * For example, if the bottom two rows were cleared, this might return [18, 19].
     *
     * @return A list of integer row indices.
     */
    public List<Integer> getClearedIndices() {
        return clearedIndices; }

    /**
     * Gets the raw data of the rows that were removed before they were deleted.
     * <p>
     * This preserves the block colors/types that existed in the cleared lines,
     * which is essential for rendering "explosion" or "fade out" animations.
     *
     * @return A list of integer arrays representing the row content.
     */
    public List<int[]> getClearedRowsData() {
        return clearedRowsData; }
}