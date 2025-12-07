package com.tetris.ui;

import javafx.scene.shape.Rectangle;
import java.util.List;

/**
 * A container class holding references to the visual components of the board.
 * <p>
 * This class acts as a central registry for all the JavaFX {@link Rectangle} nodes
 * currently active in the game scene. Instead of passing four separate lists/arrays
 * between the initialization and refresh logic, this single object is passed.
 *
 * @author Jacob Villegas
 */
public class BoardVisuals {
    /**
     * The grid of rectangles representing the static background board.
     * <p>
     * Dimensions: [BoardHeight][BoardWidth] (e.g., 22x10).
     */
    public Rectangle[][] displayMatrix;

    /**
     * The grid of rectangles representing the currently falling active piece.
     * <p>
     * Dimensions: [4][4] (Maximum size of a Tetris block rotation box).
     */
    public Rectangle[][] brickRects;

    /**
     * The grid of rectangles representing the ghost/shadow piece.
     * <p>
     * Dimensions: [4][4].
     */
    public Rectangle[][] ghostRects;

    /**
     * A list containing the grids for the "Next Piece" previews.
     * <p>
     * Each element in the list is a [4][4] array of Rectangles.
     */
    public List<Rectangle[][]> nextBrickGrids;

    /**
     * Constructs a new BoardVisuals container.
     *
     * @param displayMatrix  The main game grid.
     * @param brickRects     The active piece grid.
     * @param ghostRects     The ghost piece grid.
     * @param nextBrickGrids The list of preview grids.
     */
    public BoardVisuals(Rectangle[][] displayMatrix, Rectangle[][] brickRects, Rectangle[][] ghostRects, List<Rectangle[][]> nextBrickGrids) {
        this.displayMatrix = displayMatrix;
        this.brickRects = brickRects;
        this.ghostRects = ghostRects;
        this.nextBrickGrids = nextBrickGrids;
    }
}