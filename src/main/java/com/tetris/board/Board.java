package com.tetris.board;

import com.tetris.data.ViewData;
import com.tetris.gameLogic.ClearRow;
import com.tetris.gameLogic.Score;

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
