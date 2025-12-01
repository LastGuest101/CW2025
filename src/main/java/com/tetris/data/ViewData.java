package com.tetris.data;

import com.tetris.gameLogic.MatrixOperations;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int ghostYPosition;
    private final int[][][] nextBricksData;

    public ViewData(int[][] brickData, int xPosition, int yPosition, int ghostYPosition, int[][][] nextBricksData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.ghostYPosition = ghostYPosition;
        this.nextBricksData = nextBricksData;
    }

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int getGhostYPosition() {
        return ghostYPosition;
    }

    public int[][][] getNextBricksData() {
        return nextBricksData;
    }
}