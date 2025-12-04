package com.tetris.ui;

import javafx.scene.shape.Rectangle;
import java.util.List;

public class BoardVisuals {
    public Rectangle[][] displayMatrix;      // The background grid
    public Rectangle[][] brickRects;         // The falling brick
    public Rectangle[][] ghostRects;         // The ghost piece
    public List<Rectangle[][]> nextBrickGrids; // The preview grids

    public BoardVisuals(Rectangle[][] displayMatrix, Rectangle[][] brickRects, Rectangle[][] ghostRects, List<Rectangle[][]> nextBrickGrids) {
        this.displayMatrix = displayMatrix;
        this.brickRects = brickRects;
        this.ghostRects = ghostRects;
        this.nextBrickGrids = nextBrickGrids;
    }
}