package com.tetris.gameLogic;

import com.tetris.board.Board;
import com.tetris.board.SimpleBoard;
import com.tetris.data.DownData;
import com.tetris.data.ViewData;
import com.tetris.logic.bricks.RandomBrickGenerator;
import com.tetris.ui.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10, new RandomBrickGenerator());

    private final GuiController viewGuiController;

    private Timeline gameLoop;

    private static final double GAME_SPEED_MILLIS = 400;

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());

        this.gameLoop = new Timeline(new KeyFrame(
                Duration.millis(GAME_SPEED_MILLIS),
                ae -> {
                    // 2. Perform the logic
                    DownData data = onDownEvent(new MoveEvent(EventType.DOWN, EventSource.THREAD));
                    // 3. Push the result to the View
                    viewGuiController.updateView(data);
                }
        ));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean moved = board.moveBrickDown();
        if (moved) {
            handleSuccessfulMoveDown(event);
            return new DownData(null, board.getViewData());
        }
        return handleIntersect();
    }
    /*
    checks if block has moved down or has intersected.
     */

    private void handleSuccessfulMoveDown(MoveEvent event) {
        if (event.getEventSource() == EventSource.USER) {
            board.getScore().add(1);
        }
    }
    /*
    Adds down score if user caused the down movement
     */

    private DownData handleIntersect() {
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();
        if (clearRow != null && clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
        }
        if (board.createNewBrick()) {
            gameLoop.stop();
            viewGuiController.gameOver();
        }
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        return new DownData(clearRow, board.getViewData());
    }
    /*
    Checks for any clear rows and sees if spawning a brick is valid or not to check
    if they should end the game or not.
     */

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }


    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        gameLoop.play();
    }

    public void stopGame() {
        if (viewGuiController != null) {
            stopTimeLine();
        }
    }

    public void stopTimeLine() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }
    /*
    Used to stop the timeline when the game is closed
     */



}
