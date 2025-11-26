package com.tetris.ui;

import com.tetris.data.DownData;
import com.tetris.data.ViewData;
import javafx.beans.property.IntegerProperty;

public interface GameView {
    void initGameView(int[][] boardMatrix, ViewData brick);
    void refreshGameBackground(int[][] board);
    void updateView(DownData data);
    void gameOver();
    void bindScore(IntegerProperty scoreProperty);
    void setEventListener(InputEventListener listener);
}