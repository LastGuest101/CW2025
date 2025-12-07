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
/**
 * The central controller for the Tetris game application.
 * <p>
 * This class implements the Model-View-Controller (MVC) pattern by acting as the
 * intermediary between the {@link Board} (Model) and the {@link GameView} (View).
 * It is responsible for:
 * <ul>
 * <li>Managing the main game loop (JavaFX Timeline).</li>
 * <li>Processing user inputs (keyboard events).</li>
 * <li>Coordinating game states (Pause, Freeze, Game Over).</li>
 * <li>Updating the score, levels, and playing sounds.</li>
 * </ul>
 *
 * @author Jacob Villegas
 */
public class GameController implements InputEventListener {

    private final Board board = new SimpleBoard(GameConfig.BOARD_WIDTH, GameConfig.BOARD_HEIGHT, new RandomBrickGenerator());
    private final GameView gameView;
    private final SoundManager soundManager;

    private final LevelManager levelManager;
    private final FreezeManager freezeManager;

    private Timeline gameLoop;

    /**
     * Constructs a new GameController and initializes the game environment.
     * <p>
     * This sets up the UI bindings for scores and levels, loads high scores,
     * initializes the starting board state, and starts the background music.
     *
     * @param view The UI view responsible for rendering the game.
     */
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

    /**
     * Creates the main game loop using a JavaFX Timeline.
     * <p>
     * The loop triggers on every tick (determined by the current level speed).
     * If the game is not currently "Frozen", it forces a Move Down event.
     *
     * @return A configured {@link Timeline} object.
     */
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

    /**
     * Recreates and restarts the game loop to apply new speed settings.
     * <p>
     * This is called whenever the player levels up to make the game faster.
     */
    private void updateGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        this.gameLoop = createGameLoop();
        gameLoop.play();
    }

    /**
     * Handles the "Down" action (either from gravity or user pressing Down).
     *
     * @param event The move event details.
     * @return A {@link DownData} object containing the view updates.
     */
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

    /**
     * Handles the "Hard Drop" action (Spacebar).
     * <p>
     * Instantly drops the piece to the bottom and locks it.
     *
     * @param event The move event details.
     * @return A {@link DownData} object containing the view updates (including cleared lines).
     */
    @Override
    public DownData onSpaceEvent(MoveEvent event) {
        while (board.moveBrickDown()) {
            board.getScore().add(1);
        }
        return handleIntersect();
    }

    /**
     * Handles moving the piece to the left.
     *
     * @param event The move event details.
     * @return The updated {@link ViewData}.
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        if (board.moveBrickLeft()) soundManager.playMove();
        return board.getViewData();
    }

    /**
     * Handles moving the piece to the right.
     *
     * @param event The move event details.
     * @return The updated {@link ViewData}.
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        if (board.moveBrickRight()) soundManager.playMove();
        return board.getViewData();
    }

    /**
     * Handles rotating the piece.
     *
     * @param event The move event details.
     * @return The updated {@link ViewData}.
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        if (board.rotateLeftBrick()) soundManager.playRotate();
        return board.getViewData();
    }

    /**
     * Processes logic when a brick hits the bottom or another piece.
     * <p>
     * This method:
     * <ol>
     * <li>Plays the drop sound.</li>
     * <li>Merges the brick into the board matrix.</li>
     * <li>Checks for and processes cleared lines.</li>
     * <li>Checks for Game Over conditions.</li>
     * <li>Refreshes the view.</li>
     * </ol>
     *
     * @return The data required to update the UI (cleared lines, score, etc).
     */

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

    /**
     * Updates the score and level based on the number of cleared lines.
     *
     * @param clearRow The result of the row clearing check.
     */
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

    /**
     * Checks if the game is over (unable to spawn a new brick).
     *
     * @return {@code true} if the game is over; {@code false} otherwise.
     */
    private boolean processGameOver() {
        if (board.createNewBrick()) {
            gameLoop.stop();
            gameView.gameOver();
            saveHighScore();
            return true;
        }
        return false;
    }

    /**
     * Resets the game to its initial state.
     * <p>
     * Clears the board, resets level/score, resets power-ups, and starts a new game loop.
     */
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

    /**
     * Attempts to activate the "Time Freeze" power-up.
     * <p>
     * If successful, it pauses logic updates (but keeps the loop running) and visualizes the freeze.
     */
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

    /**
     * Toggles the game loop between Playing and Paused states.
     */
    @Override
    public void onPauseEvent() {
        if (gameLoop.getStatus() == javafx.animation.Animation.Status.RUNNING) {
            gameLoop.pause();
        } else {
            gameLoop.play();
        }
    }

    /**
     * Stops the game loop and saves the high score.
     * <p>
     * Typically called when the application window is closing.
     */
    public void stopGame() {
        saveHighScore();
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    /**
     * Persists the current high score to storage if the current score exceeds it.
     */
    @Override
    public void saveHighScore() {
        int currentScore = board.getScore().scoreProperty().get();
        int currentHigh = board.getScore().highScoreProperty().get();
        if (currentScore >= currentHigh) {
            HighScoreManager.saveHighScore(currentScore);
        }
    }

    /**
     * Toggles background music on or off.
     */
    @Override
    public void muteMusic() {
        soundManager.toggleMusic();
    }
}