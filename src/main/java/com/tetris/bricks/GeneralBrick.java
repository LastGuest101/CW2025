package com.tetris.bricks;

import com.tetris.gameLogic.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * The base implementation for all Tetris brick types.
 * <p>
 * This class provides the common storage and retrieval logic for brick shapes.
 * Instead of every brick implementing its own "get" method, they all inherit from this class
 * and simply populate the {@code brickMatrix} list with their specific rotation states.
 *
 * @author Jacob Villegas
 */
public class GeneralBrick implements Brick
{
    /**
     * A list of 2D arrays representing the brick's shape at different rotation angles.
     * <p>
     * Index 0 is the default spawn orientation.
     * Index 1 is rotated 90° clockwise.
     */
    protected final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Retrieves the list of all rotation states for this brick.
     * <p>
     * This method returns a deep copy of the internal matrix list to ensure
     * that the game logic or UI cannot accidentally modify the fundamental
     * definition of the brick shape (e.g., deleting a block from the "T" shape permanently).
     *
     * @return A safe, independent list of 2D integer arrays.
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}