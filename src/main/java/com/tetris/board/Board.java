package com.tetris.board;

import com.tetris.data.ViewData;
import com.tetris.gameLogic.ClearRow;
import com.tetris.gameLogic.Score;
/**
 * Defines the essential behavior of a Tetris game board.
 * <p>
 * This interface serves as the <b>Model</b> in the application's architecture.
 * It abstracts the complex logic of grid management, collision detection, and piece manipulation
 * into a set of high-level operations (move, rotate, clear).
 * <p>
 *
 * @author Jacob Villegas
 */
public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows(int currentLevel);

    Score getScore();

    void newGame();
}
