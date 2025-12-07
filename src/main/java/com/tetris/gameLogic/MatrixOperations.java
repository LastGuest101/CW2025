package com.tetris.gameLogic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class MatrixOperations {


    /**
     * A utility class containing static methods for 2D array manipulations used in Tetris.
     * <p>
     * This class handles the low-level logic for:
     * <ul>
     * <li>Collision detection (intersection)</li>
     * <li>Matrix copying (deep copies to prevent reference issues)</li>
     * <li>Merging active bricks into the background grid</li>
     * <li>Detecting and clearing full rows</li>
     * </ul>
     * This class is designed to be stateless; it does not hold game data but operates on data passed to it.
     *
     */

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This is a utility class, so all methods are static.
     */
    private MatrixOperations(){

    }

    /**
     * Checks if a Tetris brick at a specific position overlaps with existing blocks or goes out of bounds.
     * <p>
     * This is the core collision detection method. It iterates through the non-empty cells
     * of the brick and checks if the corresponding target coordinates in the board matrix
     * are occupied or outside the board dimensions.
     *
     * @param matrix The current state of the game board.
     * @param brick  The 2D shape matrix of the falling piece.
     * @param x      The target X coordinate (column) for the piece's top-left corner.
     * @param y      The target Y coordinate (row) for the piece's top-left corner.
     * @return {@code true} if there is a collision or boundary violation; {@code false} if the position is safe.
     */
    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;

                // Only check valid brick parts
                if (brick[j][i] != 0) {
                    // Check Side Walls (Left/Right)
                    if (targetX < 0 || targetX >= matrix[0].length) {
                        return true;
                    }
                    // Check Bottom Floor
                    if (targetY >= matrix.length) {
                        return true;
                    }
                    // Check Collision with existing blocks
                    //  check collision if we are INSIDE the board (y >= 0).
                    // We allow 'y < 0' (the sky) so pieces can rotate at the spawn point.
                    if (targetY >= 0 && matrix[targetY][targetX] != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Helper method to check if a specific coordinate is outside the matrix boundaries.
     *
     * @param matrix  The game board.
     * @param targetX The X coordinate to check.
     * @param targetY The Y coordinate to check.
     * @return {@code true} if the coordinates are invalid/out of bounds; {@code false} otherwise.
     */
    private static boolean checkOutOfBound(int[][] matrix, int targetX, int targetY) {
        // Simple safety check for negative indexes
        if (targetX < 0 || targetY < 0 || targetY >= matrix.length || targetX >= matrix[targetY].length) {
            return true; // Treat "Out of Bounds" as a collision
        }
        return false;
    }

    /**
     * Creates a deep copy of a 2D integer array.
     * <p>
     * This ensures that modifications to the new array do not affect the original array.
     * This is essential for testing "what-if" scenarios (like previewing a rotation)
     * without corrupting the actual game state.
     *
     * @param original The source matrix.
     * @return A new, independent 2D array with identical contents.
     */
    public static int[][] copy(int[][] original) {
        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] aMatrix = original[i];
            int aLength = aMatrix.length;
            myInt[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
        }
        return myInt;
    }

    /**
     * Merges a brick into the game board and returns the new board state.
     * <p>
     * This method copies the board, stamps the brick onto it at the specified coordinates,
     * and returns the result. It handles boundary checks to ensure it doesn't crash
     * if a piece is partially above the board (game over scenario).
     *
     * @param filledFields The current background matrix.
     * @param brick        The shape to lock into place.
     * @param x            The X position.
     * @param y            The Y position.
     * @return A new matrix containing the old blocks plus the new brick.
     */
    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] copy = copy(filledFields);
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;

                if (targetY >= 0 && targetY < copy.length && targetX >= 0 && targetX < copy[0].length) {
                    if (brick[j][i] != 0) {
                        copy[targetY][targetX] = brick[j][i];
                    }
                }
            }
        }
        return copy;
    }

    /**
     * Scans the matrix for full rows, removes them, and shifts blocks down.
     *
     * <p>
     * This method:
     * <ol>
     * <li>Identifies rows that are completely filled (no 0s).</li>
     * <li>Captures the data of cleared rows (for animations).</li>
     * <li>Creates a new matrix where valid rows are shifted to the bottom.</li>
     * <li>Fills the top with empty rows to replace the cleared ones.</li>
     * </ol>
     *
     * @param matrix The current board state containing locked pieces.
     * @return A {@link ClearRow} result object containing the new matrix, number of cleared lines, and cleared data.
     */
    public static ClearRow checkRemoving(final int[][] matrix) {
        int[][] tmp = new int[matrix.length][matrix[0].length];
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRowsIndices = new ArrayList<>();
        List<int[]> clearedRowsData = new ArrayList<>(); // NEW LIST

        for (int i = 0; i < matrix.length; i++) {
            int[] tmpRow = new int[matrix[i].length];
            boolean rowToClear = true;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    rowToClear = false;
                }
                tmpRow[j] = matrix[i][j];
            }
            if (rowToClear) {
                clearedRowsIndices.add(i);
                clearedRowsData.add(tmpRow); // CAPTURE THE COLOR DATA
            } else {
                newRows.add(tmpRow);
            }
        }
        for (int i = matrix.length - 1; i >= 0; i--) {
            int[] row = newRows.pollLast();
            if (row != null) {
                tmp[i] = row;
            } else {
                break;
            }
        }

        return new ClearRow(clearedRowsIndices.size(), tmp, 0, clearedRowsIndices, clearedRowsData);
    }
    /**
     * Creates a deep copy of a list of matrices.
     * <p>
     *
     * @param list The list of 2D arrays to copy.
     * @return A new list containing independent copies of the arrays.
     */

    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}
