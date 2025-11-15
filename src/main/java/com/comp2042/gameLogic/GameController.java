package com.comp2042.gameLogic;

import com.comp2042.board.Board;
import com.comp2042.board.SimpleBoard;
import com.comp2042.data.DownData;
import com.comp2042.data.ViewData;
import com.comp2042.ui.EventSource;
import com.comp2042.ui.GuiController;
import com.comp2042.ui.InputEventListener;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean moved = board.moveBrickDown();
        if (moved) {
            handleSuccessfulMoveDown(event);
            return new DownData(null, board.getViewData());
        }
        return handleInsersect();
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

    private DownData handleInsersect() {
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();
        if (clearRow != null && clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
        }
        if (board.createNewBrick()) {
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
    }
}
