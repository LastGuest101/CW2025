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
import javafx.scene.effect.Reflection;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

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
    @FXML private ImageView menuLogo;

    @FXML private StackPane mainContainer;
    @FXML private BorderPane gameRoot; // Renamed from rootPane
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GameResources.loadFonts();
        this.notificationRenderer = new NotificationRenderer(groupNotification);
        mainContainer.setFocusTraversable(true);
        mainContainer.requestFocus();
        mainContainer.setOnKeyPressed(this::handleInput);
        animator.startLogoPulse(menuLogo);

        gameOverPanel.setVisible(false);
    }

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

    @Override
    public void refreshGameBackground(int[][] board) {
        if (boardRefresher != null) boardRefresher.refreshBackground(board);
    }

    private void refreshBrick(ViewData brick) {
        if (!isPause.getValue() && boardRefresher != null) {
            boardRefresher.refreshBrick(brick);
        }
    }

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

    public void bindScore(IntegerProperty score)
    {
        scoreLabel.textProperty().bind(score.asString());
    }

    public void bindHighScore(IntegerProperty highScore)
    {
                highScoreLabel.textProperty().bind(highScore.asString());
    }

    public void gameOver() {
        gameOverPanel.setVisible(true);
        isGameOver.setValue(true);
    }

    public void newGame(ActionEvent e) {
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        isPause.setValue(false);
        isGameOver.setValue(false);
    }

    public void muteGame(ActionEvent e) {
        eventListener.muteMusic();
        gamePanel.requestFocus();
    }

    public void activateFreeze(ActionEvent e) {
        eventListener.onFreezeEvent();
        gamePanel.requestFocus(); // Return focus to board so arrow keys work
    }

    @Override
    public void setFreezeStatus(boolean isFrozen, int[][] currentBoard, ViewData currentBrick) {
        boardStyler.updateFrozenTheme(isFrozen, mainContainer, gamePanel, gameBoard);

        if (boardRefresher != null) {
            boardRefresher.refreshBackground(currentBoard);
            boardRefresher.refreshBrick(currentBrick);
        }
    }

    @Override
    public void bindLevel(IntegerProperty levelProperty) {
        levelLabel.textProperty().bind(levelProperty.asString());
    }
    public void startNewGameFromMenu(ActionEvent e) {
        mainMenu.setVisible(false);
        gameRoot.setVisible(true);
        newGame(null); // Call your existing newGame logic
        gamePanel.requestFocus(); // Critical for keyboard input
    }

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

    // 3. Pause Toggle override
    public void pauseGame(ActionEvent e) {
        // 1. Toggle the Boolean Logic (Original Code)
        boolean newState = !isPause.getValue();
        isPause.setValue(newState);
        eventListener.onPauseEvent(); // Tell GameController to stop/start the loop

        // 2. Toggle the Menu UI (New Code)
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

    public void showHelp(ActionEvent e) {
        mainMenu.setVisible(false);
        helpMenu.setVisible(true);
    }

    public void closeHelp(ActionEvent e) {
        helpMenu.setVisible(false);
        mainMenu.setVisible(true);
    }

    public void exitGame(ActionEvent e) {
        System.exit(0);
    }
}