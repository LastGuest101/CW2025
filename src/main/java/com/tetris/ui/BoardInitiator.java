package com.tetris.ui;

import com.tetris.data.ViewData;
import com.tetris.gameLogic.GameConfig;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the initial setup and layering of the game board's visual components.
 * <p>
 * This class is responsible for:
 * <ul>
 * <li>Creating the {@link StackPane} structure that allows the "Ghost" piece and Active piece
 * to float transparently over the static background grid.</li>
 * <li>Instantiating the pools of {@link Rectangle} objects used for rendering.</li>
 * <li>Setting up the layout for the "Next Brick" preview window.</li>
 * </ul>
 * This is effectively a "Factory" class for the {@link BoardVisuals} object.
 *
 * @author Jacob Villegas
 */
public class BoardInitiator {

    /**
     * Configures the Z-ordering (layering) of the game panels.
     * <p>
     * Tetris requires three distinct layers to render correctly:
     * <ol>
     * <li><b>Bottom:</b> The {@code gamePanel} (Static, locked blocks).</li>
     * <li><b>Middle:</b> The {@code ghostPanel} (The shadow indicating where the piece will land).</li>
     * <li><b>Top:</b> The {@code brickPanel} (The currently falling active piece).</li>
     * </ol>
     * This method creates a {@link StackPane} to hold these layers on top of each other
     * and injects it into the main layout container.
     *
     * @param gamePanel  The GridPane containing the static background blocks.
     * @param brickPanel The GridPane containing the active moving block.
     * @return The newly created {@code ghostPanel}, which needs to be passed to the initialization logic.
     */
    public GridPane setupLayers(GridPane gamePanel, GridPane brickPanel) {
        StackPane gameArea = new StackPane();
        gameArea.setAlignment(Pos.TOP_LEFT);

        if (gamePanel.getParent() instanceof BorderPane gameBoardContainer) {
            gameBoardContainer.setCenter(null);
            gameBoardContainer.setCenter(gameArea);
        }

        gamePanel.setPadding(Insets.EMPTY);
        if (!gameArea.getChildren().contains(gamePanel)) {
            gameArea.getChildren().add(gamePanel);
        }

        // Create the Ghost layer
        GridPane ghostPanel = new GridPane();
        ghostPanel.setVgap(1);
        ghostPanel.setHgap(1);
        ghostPanel.setPadding(Insets.EMPTY);
        ghostPanel.setManaged(false);
        gameArea.getChildren().add(ghostPanel);

        if (brickPanel.getParent() != null && brickPanel.getParent() != gameArea) {
            ((Pane) brickPanel.getParent()).getChildren().remove(brickPanel);
        }
        if (!gameArea.getChildren().contains(brickPanel)) {
            gameArea.getChildren().add(brickPanel);
        }
        brickPanel.setPadding(Insets.EMPTY);
        brickPanel.setManaged(false);

        return ghostPanel;
    }

    /**
     * Instantiates the grid of Rectangle objects for the board, ghost, and active pieces.
     * <p>
     * This method fills the empty GridPanes with JavaFX Rectangles.
     * <br>
     * <b>Note on Hidden Rows:</b> Standard Tetris boards have 2-4 hidden rows at the top
     * where pieces spawn. This loop starts at {@code i = 2}, effectively hiding the top
     * two rows from the visual grid while keeping them in the logical matrix.
     *
     * @param gamePanel     The pane for static blocks.
     * @param ghostPanel    The pane for the ghost piece.
     * @param brickPanel    The pane for the active piece.
     * @param nextBrickPane The pane for the "Next Piece" sidebar.
     * @param boardMatrix   The logical data source (used to determine dimensions).
     * @param brick         The current brick data (used to determine 4x4 or 3x3 grid size).
     * @return A {@link BoardVisuals} record containing references to all created Rectangles.
     */
    public BoardVisuals initBoard(GridPane gamePanel, GridPane ghostPanel, GridPane brickPanel, Pane nextBrickPane, int[][] boardMatrix, ViewData brick) {
        // Initialize Main Board Grid
        Rectangle[][] displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        gamePanel.getChildren().clear();

        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rect = new Rectangle(GameConfig.BRICK_SIZE, GameConfig.BRICK_SIZE);
                displayMatrix[i][j] = rect;
                gamePanel.add(rect, j, i - 2);
            }
        }

        // Initialize Current Brick & Ghost
        brickPanel.getChildren().clear();
        ghostPanel.getChildren().clear();
        Rectangle[][] brickRects = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        Rectangle[][] ghostRects = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];

        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                // Ghost Node
                Rectangle ghostRect = new Rectangle(GameConfig.BRICK_SIZE, GameConfig.BRICK_SIZE);
                ghostRect.setVisible(false);
                ghostRects[i][j] = ghostRect;
                ghostPanel.add(ghostRect, j, i);

                // Real Brick Node
                Rectangle brickRect = new Rectangle(GameConfig.BRICK_SIZE, GameConfig.BRICK_SIZE);
                brickRect.setVisible(false);
                brickRects[i][j] = brickRect;
                brickPanel.add(brickRect, j, i);
            }
        }
        List<Rectangle[][]> nextBrickGrids = initNextBrickGrids(nextBrickPane);
        return new BoardVisuals(displayMatrix, brickRects, ghostRects, nextBrickGrids);
    }

    /**
     * Initialises the "Next Brick" preview area.
     * <p>
     * It creates 3 separate 4x4 grids of rectangles, spaced out horizontally
     * in the preview pane.
     *
     * @param nextBrickPane The UI container for the sidebar preview.
     * @return A list of the 3 grids (each grid is a 4x4 array of Rectangles).
     */
    private List<Rectangle[][]> initNextBrickGrids(Pane nextBrickPane) {
        nextBrickPane.getChildren().clear();
        List<Rectangle[][]> grids = new ArrayList<>();

        int nextCount = 3;
        int brickWidth = 4 * GameConfig.BRICK_SIZE;
        int gap = 20;
        double startX = (nextBrickPane.getPrefWidth() - ((brickWidth * nextCount) + (gap * (nextCount - 1)))) / 2;

        for (int n = 0; n < nextCount; n++) {
            Rectangle[][] grid = new Rectangle[4][4];
            double brickOffsetX = startX + (n * (brickWidth + gap));

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    Rectangle rect = new Rectangle(GameConfig.BRICK_SIZE, GameConfig.BRICK_SIZE);
                    rect.setX(brickOffsetX + (j * GameConfig.BRICK_SIZE));
                    rect.setY(i * GameConfig.BRICK_SIZE);
                    rect.setVisible(false);
                    nextBrickPane.getChildren().add(rect);
                    grid[i][j] = rect;
                }
            }
            grids.add(grid);
        }
        return grids;
    }
}