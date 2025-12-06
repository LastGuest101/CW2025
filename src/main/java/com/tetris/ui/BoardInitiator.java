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

public class BoardInitiator {

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