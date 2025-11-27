package com.tetris.board;

import com.tetris.data.ViewData;
import com.tetris.gameLogic.*;
import com.tetris.logic.bricks.Brick;
import com.tetris.logic.bricks.BrickGenerator;
import com.tetris.logic.bricks.RandomBrickGenerator;

import java.awt.*;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private static final int SPAWN_X = 4;
    private static final int SPAWN_Y = -1;

    public SimpleBoard(int width, int height, BrickGenerator RandomBrickGenerator) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[height][width];
        this.brickGenerator = RandomBrickGenerator;
        this.brickRotator = new BrickRotator();
        score = new Score();
    }

    /*
     * Helper: Checks if placing the given shape at (x, y) is valid.
     */
    private boolean isMoveValid(int[][] shape, int targetX, int targetY) {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        boolean conflict = MatrixOperations.intersect(currentMatrix, shape, targetX, targetY);
        return !conflict;
    }

    /*
     * Helper: Attempts to move the current shape by delta X and delta Y.
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



    @Override
    public boolean moveBrickDown() {
        return attemptMove(0, 1);
    }

    @Override
    public boolean moveBrickLeft() {
        return attemptMove(-1, 0);
    }

    @Override
    public boolean moveBrickRight() {
        return attemptMove(1, 0);
    }

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

    /*
    Checks if the block rotating block is valid.
     */

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(SPAWN_X, SPAWN_Y);
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /*
    Spawns in new brick
     */

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        return new ViewData(brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), brickGenerator.getNextBrick().getShapeMatrix().get(0));
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow result = MatrixOperations.checkRemoving(currentGameMatrix);

        currentGameMatrix = result.getNewMatrix();
        int lines = result.getLinesRemoved();
        int score = 50 * lines * lines;

        return new ClearRow(lines, result.getNewMatrix(), score);

    }

    @Override
    public Score getScore() {
        return score;
    }


    @Override
    public void newGame() {
        currentGameMatrix = new int[height][width];
        score.reset();
        createNewBrick();
    }
}
