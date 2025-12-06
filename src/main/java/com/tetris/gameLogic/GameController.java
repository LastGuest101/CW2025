package com.tetris.gameLogic;

import com.tetris.board.Board;
import com.tetris.board.SimpleBoard;
import com.tetris.data.DownData;
import com.tetris.data.ViewData;
import com.tetris.bricks.RandomBrickGenerator;
import com.tetris.ui.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(10, 25, new RandomBrickGenerator());

    private final GameView gameView;

    private Timeline gameLoop;

    private final SoundManager soundManager;

    private boolean isFrozen = false;

    private boolean hasUsedFreeze = false;

    private final static double FREEZE_DURATION = 15000;

    private double gameSpeed = 1000;

    private int totalLinesCleared = 0;

    private final IntegerProperty currentLevel = new SimpleIntegerProperty(1);

    public GameController(GameView view) {
        this.gameView = view; // Assign to interface field
        this.soundManager = new SoundManager();
        int savedHighScore = HighScoreManager.loadHighScore();
        // Get the saved highscore from the file
        board.getScore().highScoreProperty().set(savedHighScore);
        gameView.bindLevel(currentLevel);

        board.createNewBrick();
        gameView.setEventListener(this); // Calls are the same, but now loose coupled
        gameView.initGameView(board.getBoardMatrix(), board.getViewData());
        gameView.bindScore(board.getScore().scoreProperty());
        gameView.bindHighScore(board.getScore().highScoreProperty());


        this.gameLoop = new Timeline(new KeyFrame(
                Duration.millis(gameSpeed),
                ae -> {
                    if (!isFrozen) {
                        DownData data = onDownEvent(new MoveEvent(EventType.DOWN, EventSource.THREAD));
                        gameView.updateView(data);
                    }
                }
        ));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        soundManager.playMusic();
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
    @Override
    public DownData onSpaceEvent(MoveEvent event){

        while(board.moveBrickDown()){
            handleSuccessfulMoveDown(event);

        }

        return handleIntersect();
    }

    private void handleSuccessfulMoveDown(MoveEvent event) {
        if (event.getEventSource() == EventSource.USER) {
            board.getScore().add(1);
        }
    }
    /*
    Adds down score if user caused the down movement
     */

    private DownData handleIntersect() {
        soundManager.playDrop();
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows(currentLevel.get());

        if (clearRow != null && clearRow.getLinesRemoved() > 0) {
            soundManager.playClearLine();
            board.getScore().add(clearRow.getScoreBonus());
            updateLevel(clearRow.getLinesRemoved());
        }
        if (board.createNewBrick()) {
            gameLoop.stop();
            gameView.gameOver();
            int currentScore = board.getScore().scoreProperty().get();
            int currentHighScore = board.getScore().highScoreProperty().get();

            //  If we beat (or tied) the record, save it to disk!
            if (currentScore >= currentHighScore) {
                saveHighScore();
            }
            return new DownData(clearRow, board.getViewData()); // Return safely
        }
        // Only refresh if the game is NOT over
        gameView.refreshGameBackground(board.getBoardMatrix());

        // If Frozen, re-apply the visual freeze effect to the new piece immediately
        if (isFrozen) {
            gameView.setFreezeStatus(true, board.getBoardMatrix(), board.getViewData());
        }

        return new DownData(clearRow, board.getViewData());
    }
    /*
    Checks for any clear rows and sees if spawning a brick is valid or not to check
    if they should end the game or not.
     */

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        if (board.moveBrickLeft()) { // Check if it actually moved
            soundManager.playMove();
        }
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        if (board.moveBrickRight()) {
            soundManager.playMove();
        }
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        if (board.rotateLeftBrick()) {
            soundManager.playRotate();
        }
        return board.getViewData();
    }


    @Override
    public void createNewGame() {
        hasUsedFreeze = false;
        isFrozen = false;
        totalLinesCleared = 0;
        currentLevel.set(1);
        gameSpeed = 400;
        gameLoop.stop();
        board.newGame();
        gameView.refreshGameBackground(board.getBoardMatrix());
        gameView.setFreezeStatus(false, board.getBoardMatrix(), board.getViewData());
        gameLoop.play();
    }

    public void stopGame() {
        saveHighScore();

        if (gameView != null) {
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

    @Override
    public void onPauseEvent() {
        if (gameLoop.getStatus() == javafx.animation.Animation.Status.RUNNING) {
            gameLoop.pause();;
        } else {
            gameLoop.play();
        }
    }
    /*
    Used to pause the game by pausing the timeline thread.
     */

    @Override
    public void muteMusic(){
        soundManager.toggleMusic();
    }

    @Override
    public void onFreezeEvent() {
        // --- FIX: Prevent usage if game is Over, Paused, or in Menu ---
        if (gameLoop.getStatus() != javafx.animation.Animation.Status.RUNNING) {
            return;
        }

        // Existing checks
        if (isFrozen || hasUsedFreeze) {
            return;
        }

        isFrozen = true;
        hasUsedFreeze = true;

        soundManager.playFreeze();

        gameView.setFreezeStatus(true, board.getBoardMatrix(), board.getViewData());

        // Schedule un-freeze
        new Timeline(new KeyFrame(
                Duration.millis(FREEZE_DURATION),
                ae -> unfreeze()
        )).play();
    }

    private void unfreeze() {
        isFrozen = false;
        // Pass data to revert colors instantly
        gameView.setFreezeStatus(false, board.getBoardMatrix(), board.getViewData());
    }

    private void updateLevel(int linesRemoved) {
        totalLinesCleared += linesRemoved;

        // Level up every 10 lines
        int newLevelVal = (totalLinesCleared / 10) + 1;

        if (newLevelVal > currentLevel.get()) {
            currentLevel.set(newLevelVal);

            // FORMULA: Speed decreases by 10% every level (0.9x)
            // Level 1: 1000ms
            // Level 2: 900ms
            double newSpeed = 800 * Math.pow(0.8, newLevelVal - 1);

            // Clamp it so it doesn't crash the timeline (minimum 50ms)
            if (newSpeed < 50) newSpeed = 50;

            // Check if significant change needed
            if (Math.abs(newSpeed - gameSpeed) > 1.0) {
                gameSpeed = newSpeed;
                updateGameLoop();
                System.out.println("Level: " + newLevelVal + " | Speed: " + (int)gameSpeed + "ms");
            }
        }
    }

    private void updateGameLoop() {
        // We must stop the old timeline before starting a new one
        if (gameLoop != null) {
            gameLoop.stop();
        }
        // Create a new timeline with the faster Duration
        this.gameLoop = new Timeline(new KeyFrame(
                Duration.millis(gameSpeed),
                ae -> {
                    if (!isFrozen) {
                        DownData data = onDownEvent(new MoveEvent(EventType.DOWN, EventSource.THREAD));
                        gameView.updateView(data);
                    }
                }
        ));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    @Override
    public void saveHighScore() {
        int currentScore = board.getScore().scoreProperty().get();
        int currentHigh = board.getScore().highScoreProperty().get();

        // Save if we beat or tied the record
        if (currentScore >= currentHigh) {
            HighScoreManager.saveHighScore(currentScore);
        }
    }

}
