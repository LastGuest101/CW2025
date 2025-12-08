package com.tetris.ui;

import com.tetris.data.DownData;
import com.tetris.data.ViewData;
import com.tetris.gameLogic.ClearRow;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The primary JavaFX Controller for the Tetris application.
 * <p>
 * This class implements the {@link GameView} interface and serves as the visual front-end
 * of the application. It maps FXML elements to Java code, handles menu navigation,
 * manages the scene graph layers, and delegates raw input events to the {@link GameInputHandler}.
 * <p>
 *
 *
 * @author Jacob Villegas
 */
public class GuiController implements GameView, Initializable {

    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private Label scoreLabel;
    @FXML private Label highScoreLabel;
    @FXML private Pane nextBrickPane;
    @FXML private BorderPane gameBoard;
    @FXML private Label levelLabel;

    @FXML private StackPane mainContainer;
    @FXML private BorderPane gameRoot;
    @FXML private VBox mainMenu;
    @FXML private VBox pauseMenu;
    @FXML private VBox helpMenu;

    private final BoardInitiator boardInitiator = new BoardInitiator();
    private final BoardStyler boardStyler = new BoardStyler();
    private BoardRefresher boardRefresher;
    private GameInputHandler inputHandler;
    private NotificationRenderer notificationRenderer;

    private GridPane ghostPanel;
    private InputEventListener eventListener;
    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);
    private final BoardAnimator animator = new BoardAnimator();

    /**
     * Called automatically by JavaFX after the FXML file has been loaded.
     * <p>
     * Performs initial setup like loading custom fonts, setting up the notification
     * renderer, and ensuring the main container receives keyboard focus.
     *
     * @param location  The location used to resolve relative paths for the root object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GameResources.loadFonts();
        this.notificationRenderer = new NotificationRenderer(groupNotification);
        mainContainer.setFocusTraversable(true);
        mainContainer.requestFocus();
        mainContainer.setOnKeyPressed(this::handleInput);

        gameOverPanel.setVisible(false);
    }

    /**
     * Connects this View to the Logic Controller.
     * <p>
     * Also initializes the {@link GameInputHandler}, providing it with the necessary
     * callbacks to execute game actions (move, pause, new game) when keys are pressed.
     *
     * @param eventListener The controller interface to receive game events.
     */
    @Override
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
        this.inputHandler = new GameInputHandler(
                eventListener,
                this::refreshBrick,
                this::updateView,
                () -> pauseGame(null),
                () -> newGame(null),
                () -> muteGame(null)
        );
    }

    /**
     * Initializes the visual state of the game board.
     * <p>
     * This calls the {@link BoardInitiator} to stack the necessary panes (Background, Ghost, Active Brick)
     * and fills them with Rectangle nodes.
     *
     * @param boardMatrix The initial state of the static background board.
     * @param brick       The initial state of the active brick.
     */
    @Override
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        this.ghostPanel = boardInitiator.setupLayers(gamePanel, brickPanel);

        BoardVisuals visuals = boardInitiator.initBoard(
                gamePanel, ghostPanel, brickPanel, nextBrickPane, boardMatrix, brick
        );

        this.boardRefresher = new BoardRefresher(brickPanel, ghostPanel, visuals, boardStyler);
        this.boardRefresher.refreshBackground(boardMatrix);
        this.boardRefresher.refreshBrick(brick);
    }

    /**
     * Updates the UI after a "Down" event (Gravity or Soft Drop).
     * <p>
     * This method checks if any rows were cleared in the process. If so, it triggers
     * visual effects (Score notifications, Screen Shake, Particles). Finally, it
     * refreshes the brick position.
     *
     * @param downData The result of the downward movement (contains clear info and brick position).
     */
    @Override
    public void updateView(DownData downData) {
        ClearRow clearRow = downData.getClearRow();

        if (clearRow != null && clearRow.getLinesRemoved() > 0) {
            notificationRenderer.showScoreBonus(clearRow.getScoreBonus());
            animator.shake(gameRoot);

            javafx.scene.layout.Pane staticLayer = (javafx.scene.layout.Pane) brickPanel.getParent();

            if (staticLayer != null) {
                animator.spawnClearParticles(
                        staticLayer,
                        clearRow.getClearedIndices(),
                        clearRow.getClearedRowsData()
                );
            }
        }
        refreshBrick(downData.getViewData());
    }

    /**
     * Forces a full redraw of the static background grid.
     *
     * @param board The 2D array representing the locked blocks.
     */
    @Override
    public void refreshGameBackground(int[][] board) {
        if (boardRefresher != null) boardRefresher.refreshBackground(board);
    }

    /**
     * Refreshes the position and rotation of the active moving brick.
     * <p>
     * This is the most frequently called update method (called every frame).
     *
     * @param brick The current state of the active piece.
     */

    private void refreshBrick(ViewData brick) {
        if (!isPause.getValue() && boardRefresher != null) {
            boardRefresher.refreshBrick(brick);
        }
    }

    /**
     * Central routing method for keyboard input.
     * <p>
     * Filters events:
     * <ul>
     * <li><b>Global Keys:</b> 'M' for mute works anywhere (menus or game).</li>
     * <li><b>Game Keys:</b> Movement keys only work if the game board is visible.</li>
     * </ul>
     *
     * @param event The JavaFX KeyEvent.
     */
    private void handleInput(KeyEvent event) {
        // Allow M (Mute) even on the menu
        if (event.getCode() == javafx.scene.input.KeyCode.M) {
            if (inputHandler != null) inputHandler.handleKeyPress(event);
            return;
        }

        // Only allow movement keys if the GAME is actually visible
        if (gameRoot.isVisible()) {
            if (inputHandler != null) inputHandler.handleKeyPress(event);
        }
    }

    /**
     * Binds the UI Score label to the logic's score property.
     *
     * @param score The observable score property.
     */
    public void bindScore(IntegerProperty score)
    {
        scoreLabel.textProperty().bind(score.asString());
    }

    /**
     * Binds the UI High Score label to the logic's high score property.
     *
     * @param highScore The observable high score property.
     */
    public void bindHighScore(IntegerProperty highScore)
    {
                highScoreLabel.textProperty().bind(highScore.asString());
    }

    /**
     * Transitions the UI to the "Game Over" state.
     * <p>
     * Displays the Game Over overlay and updates internal flags.
     */
    public void gameOver() {
        gameOverPanel.setVisible(true);
        isGameOver.setValue(true);
    }

    /**
     * Starts a new game session.
     * <p>
     * Resets UI flags, hides overlays, and triggers the logic controller to reset the board.
     *
     * @param e Optional ActionEvent (can be null).
     */
    public void newGame(ActionEvent e) {
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        isPause.setValue(false);
        isGameOver.setValue(false);
    }

    /**
     * Toggles the game audio.
     *
     * @param e Optional ActionEvent.
     */
    public void muteGame(ActionEvent e) {
        eventListener.muteMusic();
        gamePanel.requestFocus();
    }

    /**
     * Manually triggers the "Time Freeze" power-up via a UI button.
     *
     * @param e The button click event.
     */
    public void activateFreeze(ActionEvent e) {
        eventListener.onFreezeEvent();
        gamePanel.requestFocus(); // Return focus to board so arrow keys work
    }

    /**
     * Updates the entire board visual theme based on the Freeze status.
     * <p>
     * When frozen, blocks turn cyan and glow. When unfrozen, they return to normal colors.
     *
     * @param isFrozen     {@code true} if frozen mode is active.
     * @param currentBoard The current static board matrix.
     * @param currentBrick The current active brick.
     */
    @Override
    public void setFreezeStatus(boolean isFrozen, int[][] currentBoard, ViewData currentBrick) {
        boardStyler.updateFrozenTheme(isFrozen, mainContainer, gamePanel, gameBoard);

        if (boardRefresher != null) {
            boardRefresher.refreshBackground(currentBoard);
            boardRefresher.refreshBrick(currentBrick);
        }
    }

    /**
     * Binds the UI Level label to the logic's level property.
     *
     * @param levelProperty The observable level property.
     */
    @Override
    public void bindLevel(IntegerProperty levelProperty) {
        levelLabel.textProperty().bind(levelProperty.asString());
    }
    /**
     * Transitions from the Main Menu to the Game Board.
     *
     * @param e The "Play Game" button event.
     */
    public void startNewGameFromMenu(ActionEvent e) {
        mainMenu.setVisible(false);
        gameRoot.setVisible(true);
        newGame(null); // Call your existing newGame logic
        gamePanel.requestFocus(); // Critical for keyboard input
    }

    /**
     * Returns to the Main Menu from the Game.
     * <p>
     * This automatically pauses the game, saves the high score, and switches the visible pane.
     *
     * @param e The "Menu" button event.
     */
    public void showMainMenu(ActionEvent e) {
        // 1. Force the game to pause logic if it's currently running
        if (!isPause.getValue()) {
            // We manually set the boolean and notify the listener to stop the loop
            isPause.setValue(true);
            if (eventListener != null) {
                eventListener.onPauseEvent();
                eventListener.saveHighScore();
            }
        }

        // 2. Switch UI Layers
        gameRoot.setVisible(false);   // Hide the Game Board
        pauseMenu.setVisible(false);  // Hide the Pause Menu (if open)
        helpMenu.setVisible(false);   // Hide Help

        mainMenu.setVisible(true);    // SHOW the Main Menu
    }

    /**
     * Toggles the Pause state.
     * <p>
     * If pausing, it shows the pause menu overlay.
     * If unpausing, it hides the overlay and returns focus to the game board.
     *
     * @param e Optional ActionEvent.
     */
    public void pauseGame(ActionEvent e) {
        boolean newState = !isPause.getValue();
        isPause.setValue(newState);
        eventListener.onPauseEvent(); // Tell GameController to stop/start the loop

        if (newState) {
            // Game is now PAUSED -> Show Menu
            if (pauseMenu != null) {
                pauseMenu.setVisible(true);
                pauseMenu.toFront();
            }
        } else {
            // Game is now RUNNING -> Hide Menu
            if (pauseMenu != null) {
                pauseMenu.setVisible(false);
            }
            gamePanel.requestFocus(); // Give focus back to board so keys work
        }
    }

    /**
     * Shows the Help/Instructions screen.
     *
     * @param e The button click event.
     */
    public void showHelp(ActionEvent e) {
        mainMenu.setVisible(false);
        helpMenu.setVisible(true);
    }

    /**
     * Closes the Help screen and returns to the Main Menu.
     *
     * @param e The button click event.
     */
    public void closeHelp(ActionEvent e) {
        helpMenu.setVisible(false);
        mainMenu.setVisible(true);
    }

    /**
     * Closes the application completely.
     *
     * @param e The button click event.
     */
    public void exitGame(ActionEvent e) {
        System.exit(0);
    }
}