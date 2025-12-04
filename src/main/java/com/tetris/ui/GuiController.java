package com.tetris.ui;

import com.tetris.data.DownData;
import com.tetris.data.ViewData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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

    private final BoardInitiator boardInitiator = new BoardInitiator();
    private final BoardStyler boardStyler = new BoardStyler();
    private BoardRefresher boardRefresher;
    private GameInputHandler inputHandler;
    private NotificationRenderer notificationRenderer;

    private GridPane ghostPanel;
    private InputEventListener eventListener;
    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GameResources.loadFonts();
        this.notificationRenderer = new NotificationRenderer(groupNotification);

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleInput);

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
        if (downData.getClearRow() != null) {
            notificationRenderer.showScoreBonus(downData.getClearRow().getScoreBonus());
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
        if (inputHandler != null) inputHandler.handleKeyPress(event);
    }

    public void bindScore(IntegerProperty score) {
        scoreLabel.textProperty().bind(score.asString());
    }

    public void bindHighScore(IntegerProperty highScore) {
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

    public void pauseGame(ActionEvent e) {
        isPause.setValue(!isPause.getValue());
        eventListener.onPauseEvent();
        gamePanel.requestFocus();
    }

    public void muteGame(ActionEvent e) {
        eventListener.muteMusic();
        gamePanel.requestFocus();
    }
}