package com.tetris.ui;

import com.tetris.data.DownData;
import com.tetris.data.ViewData;
import javafx.beans.property.IntegerProperty;
/**
 * Defines the contract for the user interface of the Tetris game.
 * <p>
 * This interface decouples the game logic (Controller) from the specific UI implementation (JavaFX).
 * The {@link com.tetris.gameLogic.GameController} interacts only with this interface,
 * allowing the UI to be swapped or modified without breaking the core logic.
 *
 * @author Jacob Villegas
 */
public interface GameView {
    void initGameView(int[][] boardMatrix, ViewData brick);
    void refreshGameBackground(int[][] board);
    void updateView(DownData data);
    void gameOver();
    void bindScore(IntegerProperty scoreProperty);
    void bindHighScore(IntegerProperty scoreProperty);
    void setEventListener(InputEventListener listener);
    void setFreezeStatus(boolean isFrozen, int[][] currentBoard, ViewData currentBrick);
    void bindLevel(IntegerProperty levelProperty);
}