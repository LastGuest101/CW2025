package com.comp2042.board;

import com.comp2042.data.ViewData;
import com.comp2042.gameLogic.ClearRow;
import com.comp2042.gameLogic.Score;

public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

    void newGame();
}
