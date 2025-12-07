package com.tetris.ui;

import com.tetris.data.ViewData;
import com.tetris.gameLogic.GameConfig;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

/**
 * Updates the visual properties of the game board during the game loop.
 * <p>
 * While {@link BoardInitiator} creates the objects once at the start, this class
 * is called repeatedly (potentially 60 times a second) to move the active brick,
 * update the ghost position, and redraw the static background when lines are cleared.
 * <p>
 * It acts as the "Renderer" logic, translating data coordinates into screen pixel coordinates.
 *
 * @author Jacob Villegas
 */
public class BoardRefresher {

    private final GridPane brickPanel;
    private final GridPane ghostPanel;
    private final BoardVisuals visuals;
    private final BoardStyler styler;

    /**
     * Constructs a BoardRefresher with references to the UI components it needs to update.
     *
     * @param brickPanel The JavaFX container holding the active falling piece.
     * @param ghostPanel The JavaFX container holding the ghost piece.
     * @param visuals    The data object containing references to all the Rectangle nodes on screen.
     * @param styler     The utility class used to apply colors and CSS to the blocks.
     */
    public BoardRefresher(GridPane brickPanel, GridPane ghostPanel, BoardVisuals visuals, BoardStyler styler) {
        this.brickPanel = brickPanel;
        this.ghostPanel = ghostPanel;
        this.visuals = visuals;
        this.styler = styler;
    }

    /**
     * Redraws the static background grid.
     * <p>
     * This iterates through the entire board matrix and updates the color of every
     * block. This is typically only called when a piece lands or lines are cleared,
     * as the background is static during normal movement.
     * <p>
     *
     * @param board The 2D integer array representing the locked blocks.
     */
    public void refreshBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                styler.styleRectangle(visuals.displayMatrix[i][j], board[i][j], true);
            }
        }
    }

    /**
     * Updates the position and appearance of dynamic elements (Active Brick, Ghost, Next Queue).
     * <p>
     * This method calculates the pixel coordinates for the active piece based on its logical
     * (row, col) position and applies a translation to the {@code brickPanel} and {@code ghostPanel}.
     * It also refreshes the "Next Piece" preview grids.
     *
     * @param brick The snapshot of the current moving data (position, shape, ghost, etc.).
     */
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