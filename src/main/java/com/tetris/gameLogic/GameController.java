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

    private final Board board = new SimpleBoard(10, 25, new RandomBrickGenerator());
    private final GameView gameView;
    private final SoundManager soundManager;

    private Timeline gameLoop;
    private boolean isFrozen = false;
    private boolean hasUsedFreeze = false;
    private int totalLinesCleared = 0;

    private double gameSpeed = GameConfig.BASE_SPEED;

    private final IntegerProperty currentLevel = new SimpleIntegerProperty(1);

    public GameController(GameView view) {
        this.gameView = view;
        this.soundManager = new SoundManager();

        // Load High Score
        int savedHighScore = HighScoreManager.loadHighScore();
        board.getScore().highScoreProperty().set(savedHighScore);
        gameView.bindLevel(currentLevel);

        board.createNewBrick();
        gameView.setEventListener(this);
        gameView.initGameView(board.getBoardMatrix(), board.getViewData());
        gameView.bindScore(board.getScore().scoreProperty());
        gameView.bindHighScore(board.getScore().highScoreProperty());

        // REFACTORED: Use gameSpeed variable which is now initialized via Config
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
        // gameLoop.play(); // Removed auto-play for Menu support
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

            if (currentScore >= currentHighScore) {
                saveHighScore();
            }
            return new DownData(clearRow, board.getViewData());
        }

        gameView.refreshGameBackground(board.getBoardMatrix());

        if (isFrozen) {
            gameView.setFreezeStatus(true, board.getBoardMatrix(), board.getViewData());
        }

        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        if (board.moveBrickLeft()) {
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

        // REFACTORED: Reset speed using Config constant
        gameSpeed = GameConfig.BASE_SPEED;

        gameLoop.stop();
        board.newGame();
        gameView.refreshGameBackground(board.getBoardMatrix());
        gameView.setFreezeStatus(false, board.getBoardMatrix(), board.getViewData());

        // Use updateGameLoop to ensure the timeline is rebuilt with the correct speed
        updateGameLoop();
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

    @Override
    public void onPauseEvent() {
        if (gameLoop.getStatus() == javafx.animation.Animation.Status.RUNNING) {
            gameLoop.pause();
        } else {
            gameLoop.play();
        }
    }

    @Override
    public void muteMusic(){
        soundManager.toggleMusic();
    }

    @Override
    public void onFreezeEvent() {
        if (gameLoop.getStatus() != javafx.animation.Animation.Status.RUNNING) {
            return;
        }

        if (isFrozen || hasUsedFreeze) {
            return;
        }

        isFrozen = true;
        hasUsedFreeze = true;

        soundManager.playFreeze();
        gameView.setFreezeStatus(true, board.getBoardMatrix(), board.getViewData());

        // REFACTORED: Use Config Constant
        new Timeline(new KeyFrame(
                Duration.millis(GameConfig.FREEZE_DURATION),
                ae -> unfreeze()
        )).play();
    }

    private void unfreeze() {
        isFrozen = false;
        gameView.setFreezeStatus(false, board.getBoardMatrix(), board.getViewData());
    }

    private void updateLevel(int linesRemoved) {
        totalLinesCleared += linesRemoved;
        int newLevelVal = (totalLinesCleared / 10) + 1;

        if (newLevelVal > currentLevel.get()) {
            currentLevel.set(newLevelVal);

            // REFACTORED: Use Config Constants for calculations
            double newSpeed = GameConfig.BASE_SPEED * Math.pow(GameConfig.SPEED_MULTIPLIER, newLevelVal - 1);

            if (newSpeed < GameConfig.MAX_SPEED_CAP) {
                newSpeed = GameConfig.MAX_SPEED_CAP;
            }

            if (Math.abs(newSpeed - gameSpeed) > 1.0) {
                gameSpeed = newSpeed;
                updateGameLoop();
                System.out.println("Level: " + newLevelVal + " | Speed: " + (int)gameSpeed + "ms");
            }
        }
    }

    private void updateGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
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

        if (currentScore >= currentHigh) {
            HighScoreManager.saveHighScore(currentScore);
        }
    }
}