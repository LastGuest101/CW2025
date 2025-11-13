package com.comp2042;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class MatrixOperations {


    //We don't want to instantiate this utility class
    private MatrixOperations(){

    }

    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0 && (checkOutOfBound(matrix, targetX, targetY) || matrix[targetY][targetX] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }
    /*
    Checks if a Tetris brick at position (x, y) overlaps existing blocks or goes outside the board.
    true = collision (intersecting or out-of-bounds)
    false = safe to place
     */

    private static boolean checkOutOfBound(int[][] matrix, int targetX, int targetY) {
        boolean returnValue = true;
        if (targetX >= 0 && targetY < matrix.length && targetX < matrix[targetY].length) {
            returnValue = false;
        }
        return returnValue;
    }
    /*
    Checks whether a given position (targetX, targetY) is outside the game board (the matrix).
    Returns true → the position is out of bounds.
    Returns false → the position is inside the board (valid).
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
    /*
   	Creates a deep copy of a 2D int array
    Returns	A new 2D array with identical content but independent from the original
    Used to Safely test rotations, moves, or updates in Tetris without altering the original data
     */

    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] copy = copy(filledFields);
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0) {
                    copy[targetY][targetX] = brick[j][i];
                }
            }
        }
        return copy;
    }

    /*
     creates a new game board showing what the playfield
     looks like with the brick added at (x, y) — without changing the existing game state.
     */

    public static ClearRow checkRemoving(final int[][] matrix) {
        int[][] tmp = new int[matrix.length][matrix[0].length];
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

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
                clearedRows.add(i);
            } else {
                newRows.add(tmpRow);
            }
            /*
            Gets a copy of the current row being checked initialised as full unless zero value is
            found on its row and is added to clear and new rows respectivly.
             */
        }
        for (int i = matrix.length - 1; i >= 0; i--) {
            int[] row = newRows.pollLast();
            if (row != null) {
                tmp[i] = row;
            } else {
                break;
            }
        }
        /*
        Places all non-full rows into the new temporary board from bottom to top,
        starting with the last row in newRows and moving upward.
        Empty rows remain at the top of tmp.
         */
        int scoreBonus = 50 * clearedRows.size() * clearedRows.size();
        return new ClearRow(clearedRows.size(), tmp, scoreBonus);
    }
    /*
    Used to find full rows and the number of full rows,
    make a board without the full rows
    and the score earned from the number of full rows.
     */

    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}
