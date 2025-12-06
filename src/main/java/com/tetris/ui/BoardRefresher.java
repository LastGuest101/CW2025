package com.tetris.ui;

import com.tetris.data.ViewData;
import com.tetris.gameLogic.GameConfig;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class BoardRefresher {

    private final GridPane brickPanel;
    private final GridPane ghostPanel;
    private final BoardVisuals visuals;
    private final BoardStyler styler;

    public BoardRefresher(GridPane brickPanel, GridPane ghostPanel, BoardVisuals visuals, BoardStyler styler) {
        this.brickPanel = brickPanel;
        this.ghostPanel = ghostPanel;
        this.visuals = visuals;
        this.styler = styler;
    }

    public void refreshBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                styler.styleRectangle(visuals.displayMatrix[i][j], board[i][j], true);
            }
        }
    }

    public void refreshBrick(ViewData brick) {
        int cellSize = GameConfig.BRICK_SIZE + 1;

        double xPos = brick.getxPosition() * cellSize;
        double yPos = (brick.getyPosition() * cellSize) - 42;
        double ghostYPos = (brick.getGhostYPosition() * cellSize) - 42;

        brickPanel.setTranslateX(xPos);
        brickPanel.setTranslateY(yPos);
        ghostPanel.setTranslateX(xPos);
        ghostPanel.setTranslateY(ghostYPos);

        // Update Current Brick and Ghost
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                int color = brick.getBrickData()[i][j];

                // Update Real Brick Style
                styler.styleRectangle(visuals.brickRects[i][j], color, false);

                // Update Ghost Style
                if (color != 0) {
                    styler.styleGhost(visuals.ghostRects[i][j]);
                } else {
                    visuals.ghostRects[i][j].setVisible(false);
                }
            }
        }

        // Update Next Brick Previews
        int[][][] nextBricks = brick.getNextBricksData();
        for (int n = 0; n < visuals.nextBrickGrids.size(); n++) {
            if (n < nextBricks.length) {
                int[][] matrix = nextBricks[n];
                Rectangle[][] grid = visuals.nextBrickGrids.get(n);
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        styler.styleRectangle(grid[i][j], matrix[i][j], false);
                    }
                }
            }
        }
    }
}