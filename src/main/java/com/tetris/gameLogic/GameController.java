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

public class GameController implements InputEventListener {

    private final Board board = new SimpleBoard(GameConfig.BOARD_WIDTH, GameConfig.BOARD_HEIGHT, new RandomBrickGenerator());
    private final GameView gameView;
    private final SoundManager soundManager;

    private final LevelManager levelManager;
    private final FreezeManager freezeManager;

    private Timeline gameLoop;

    public GameController(GameView view) {
        this.gameView = view;
        this.soundManager = new SoundManager();

        this.levelManager = new LevelManager();
        this.freezeManager = new FreezeManager();


        int savedHighScore = HighScoreManager.loadHighScore();
        board.getScore().highScoreProperty().set(savedHighScore);
        gameView.bindLevel(levelManager.levelProperty());
        gameView.bindScore(board.getScore().scoreProperty());
        gameView.bindHighScore(board.getScore().highScoreProperty());

        board.createNewBrick();
        gameView.setEventListener(this);
        gameView.initGameView(board.getBoardMatrix(), board.getViewData());

        this.gameLoop = createGameLoop();
        soundManager.playMusic();
    }

    private Timeline createGameLoop() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(levelManager.getGameSpeed()),
                ae -> {
                    if (!freezeManager.isFrozen()) {
                        DownData data = onDownEvent(new MoveEvent(EventType.DOWN, EventSource.THREAD));
                        gameView.updateView(data);
                    }
                }
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        return timeline;
    }

    private void updateGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        this.gameLoop = createGameLoop();
        gameLoop.play();
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean moved = board.moveBrickDown();
        if (moved) {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
            return new DownData(null, board.getViewData());
        }
        return handleIntersect();
    }

    @Override
    public DownData onSpaceEvent(MoveEvent event) {
        while (board.moveBrickDown()) {
            board.getScore().add(1);
        }
        return handleIntersect();
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        if (board.moveBrickLeft()) soundManager.playMove();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        if (board.moveBrickRight()) soundManager.playMove();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        if (board.rotateLeftBrick()) soundManager.playRotate();
        return board.getViewData();
    }

    private DownData handleIntersect() {
        soundManager.playDrop();
        board.mergeBrickToBackground();

        ClearRow clearRow = board.clearRows(levelManager.getCurrentLevel());
        processClearedLines(clearRow);

        if (processGameOver()) {
            return new DownData(clearRow, board.getViewData());
        }

        gameView.refreshGameBackground(board.getBoardMatrix());

        if (freezeManager.isFrozen()) {
            gameView.setFreezeStatus(true, board.getBoardMatrix(), board.getViewData());
        }

        return new DownData(clearRow, board.getViewData());
    }

    private void processClearedLines(ClearRow clearRow) {
        if (clearRow != null && clearRow.getLinesRemoved() > 0) {
            soundManager.playClearLine();
            board.getScore().add(clearRow.getScoreBonus());

            boolean speedChanged = levelManager.addLines(clearRow.getLinesRemoved());

            if (speedChanged) {
                updateGameLoop();
            }
        }
    }

    private boolean processGameOver() {
        if (board.createNewBrick()) {
            gameLoop.stop();
            gameView.gameOver();
            saveHighScore();
            return true;
        }
        return false;
    }

    @Override
    public void createNewGame() {
        // Reset Managers
        freezeManager.reset();
        levelManager.reset();

        gameLoop.stop();
        board.newGame();

        gameView.refreshGameBackground(board.getBoardMatrix());
        gameView.setFreezeStatus(false, board.getBoardMatrix(), board.getViewData());

        updateGameLoop();
    }

    @Override
    public void onFreezeEvent() {
        if (gameLoop.getStatus() != javafx.animation.Animation.Status.RUNNING) return;

        // Try to activate via Manager (returns true if successful)
        boolean activated = freezeManager.tryActivate(() -> {
            // Callback when freeze ends
            gameView.setFreezeStatus(false, board.getBoardMatrix(), board.getViewData());
        });

        if (activated) {
            soundManager.playFreeze();
            gameView.setFreezeStatus(true, board.getBoardMatrix(), board.getViewData());
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

    public void stopGame() {
        saveHighScore();
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    @Override
    public void saveHighScore() {
        int currentScore = board.getScore().scoreProperty().get();
        int currentHigh = board.getScore().highScoreProperty().get();
        if (currentScore >= currentHigh) {
            HighScoreManager.saveHighScore(currentScore);
        }
    }

    @Override
    public void muteMusic() {
        soundManager.toggleMusic();
    }
}