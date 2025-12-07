package com.tetris.gameLogic;

import com.tetris.bricks.Brick;
/**
 * Manages the rotation state of the currently active brick.
 * <p>
 * Tetris bricks usually have 4 possible rotation states (0°, 90°, 180°, 270°).
 * This class tracks which index in the rotation list is currently active and
 * helps calculate what the <i>next</i> rotation would look like before actually applying it.
 *
 */
public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * Previews the next rotation state without changing the current one.
     * <p>
     * This is crucial for validation. The game needs to see if the <i>next</i>
     * shape fits in the grid before committing to the move.
     *
     * @return A {@link NextShapeInfo} object containing the matrix of the next rotation
     * and its index.
     */
    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Retrieves the 2D matrix of the brick in its current rotation.
     *
     * @return A 2D integer array representing the shape (e.g., a 3x3 or 4x4 grid).
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Updates the current rotation index.
     * <p>
     * This is usually called after {@link #getNextShape()} has been validated
     * to confirm the rotation is legal.
     *
     * @param currentShape The index of the new rotation state.
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Assigns a new brick to be managed and resets the rotation state.
     * <p>
     * This is called when a new piece spawns at the top of the board.
     * The rotation index is reset to 0 (default spawn orientation).
     *
     * @param brick The new {@link Brick} object to control.
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }


}
