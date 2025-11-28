package com.tetris.ui;

import com.tetris.data.DownData;
import com.tetris.data.ViewData;
import com.tetris.gameLogic.MoveEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.effect.DropShadow;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class GuiController implements GameView, Initializable {

    private static final int BRICK_SIZE = 20;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GameOverPanel gameOverPanel;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private HashMap<KeyCode, Runnable> keyActions;

    private GridPane ghostPanel;

    private Rectangle[][] ghostRectangles;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);

        initKeyActions();
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        gamePanel.setOnKeyPressed(event -> handleKeyPress(event));

        gameOverPanel.setVisible(false);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    private void initKeyActions() {
        keyActions = new HashMap<>();

        keyActions.put(KeyCode.LEFT, () ->
                refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER))));
        keyActions.put(KeyCode.A, keyActions.get(KeyCode.LEFT));

        keyActions.put(KeyCode.RIGHT, () ->
                refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER))));
        keyActions.put(KeyCode.D, keyActions.get(KeyCode.RIGHT));

        keyActions.put(KeyCode.UP, () ->
                refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER))));
        keyActions.put(KeyCode.W, keyActions.get(KeyCode.UP));

        keyActions.put(KeyCode.DOWN, () -> {
            DownData data = eventListener.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER));
            updateView(data);
        });
        keyActions.put(KeyCode.S, keyActions.get(KeyCode.DOWN));

        keyActions.put(KeyCode.SPACE, () -> {
            DownData data = eventListener.onSpaceEvent(new MoveEvent(EventType.SPACE, EventSource.USER));
            updateView(data);
        });


    }
    /*
    Maps all keypresses to its respective event
     */

    private void handleKeyPress(KeyEvent event) {

        if (event.getCode() == KeyCode.N) {
            newGame(null);
            return;
        }

        if (event.getCode() == KeyCode.P) {
            boolean currentState = isPause.getValue();
            isPause.setValue(!currentState);

            eventListener.onPauseEvent();
            return;
        }
        /*
         Code for pause function to work
         */

        Runnable action = keyActions.get(event.getCode());

        if (action != null) {
            action.run();
            event.consume();
        }
    }


    @Override
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        if (ghostPanel == null) {
            ghostPanel = new GridPane();
            ghostPanel.setVgap(1);
            ghostPanel.setHgap(1);
            ((javafx.scene.layout.Pane) brickPanel.getParent()).getChildren().add(ghostPanel);
            ghostPanel.toBack();
            gamePanel.getParent().toBack();
        }

        ghostPanel.getChildren().clear();
        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];

        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                setGhostStyle(rectangle);
                ghostRectangles[i][j] = rectangle;
                ghostPanel.add(rectangle, j, i);
            }
        }
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);

                setRectangleData(0, rectangle, true);

                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                setRectangleData(brick.getBrickData()[i][j], rectangle, false);

                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getHgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getVgap() + brick.getyPosition() * BRICK_SIZE);
    }

    /*
    Set up the visual game board (displayMatrix)
    Creates rectangles for each visible cell and adds them to the gamePanel.
    Set up the current brick’s visual representation (rectangles)
    Creates rectangles for each part of the brick and adds them to brickPanel.
    Position the brick panel correctly on top of the main board
    Takes into account the brick’s offset and the hidden top rows.
*/
    private Paint getFillColor(int i) {
        return switch (i) {
            case 0 -> Color.TRANSPARENT;

            // 1. Strawberry (Pastel Red/Pink)
            case 1 -> Color.web("#FFADAD");

            // 2. Peach (Pastel Orange)
            case 2 -> Color.web("#FFD6A5");

            // 3. Pineapple (Pastel Yellow) e
            case 3 -> Color.web("#FDFFB6");

            // 4. Melon (Pastel Lime Green)
            case 4 -> Color.web("#CAFFBF");

            // 5. Watermelon (Pastel Mint/Cyan)
            case 5 -> Color.web("#9BF6FF");

            // 6. Blueberry (Pastel Periwinkle)
            case 6 -> Color.web("#A0C4FF");

            // 7. Grape (Pastel Lavender)
            case 7 -> Color.web("#BDB2FF");

            default -> Color.WHITE;
        };
    }
    /*
    Fills in the color of the bricks
     */


    private void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {

            double xOffset = gamePanel.getLayoutX() + brick.getxPosition() * (BRICK_SIZE + gamePanel.getHgap());
            double yOffset = -42 + gamePanel.getLayoutY() + brick.getyPosition() * (BRICK_SIZE + gamePanel.getVgap());

            brickPanel.setLayoutX(xOffset);
            brickPanel.setLayoutY(yOffset);

            ghostPanel.setLayoutX(xOffset);

            double ghostYOffset = -42 + gamePanel.getLayoutY() + brick.getGhostYPosition() * (BRICK_SIZE + gamePanel.getVgap());
            ghostPanel.setLayoutY(ghostYOffset);

            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    // Update Real Brick
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j], false);


                    int colorValue = brick.getBrickData()[i][j];
                    if (colorValue != 0) {
                        ghostRectangles[i][j].setVisible(true);
                        Paint realColor = getFillColor(colorValue);
                        ghostRectangles[i][j].setStroke(realColor);
                    } else {
                        ghostRectangles[i][j].setVisible(false);
                    }
                }
            }
        }
    }
    /*
    This method updates the visual representation (brickPanel) of a brick on the screen based
     on the current state of the brick data model (brick), but only if the game is not paused.
     */

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j],true);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle, boolean isBackground) {
        Paint baseColor = getFillColor(color);
        rectangle.setFill(baseColor);

        // Retro sharp corners
        rectangle.setArcHeight(10);
        rectangle.setArcWidth(10);

        if (color != 0) {
            if (baseColor instanceof Color) {
                rectangle.setStroke(((Color) baseColor).darker().darker());
            } else {
                rectangle.setStroke(Color.BLACK);
            }
            rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
            rectangle.setStrokeWidth(2);

            DropShadow glow = new DropShadow();

            glow.setColor(baseColor instanceof Color ? (Color) baseColor : Color.GRAY);

            glow.setRadius(2);

            glow.setSpread(0);

            rectangle.setEffect(glow);

        } else {
            rectangle.setEffect(null);

            if (isBackground) {
                rectangle.setStroke(Color.rgb(160, 82, 45, 0.25));
                rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
                rectangle.setStrokeWidth(1);
            } else {
                rectangle.setStroke(null);
            }
        }
    }



    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
    }
    /*
    Needs to be implemented.
     */

    public void gameOver() {
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }

    public void newGame(ActionEvent actionEvent) {
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    public void pauseGame(ActionEvent actionEvent) {
        gamePanel.requestFocus();
    }

    /*
    Needs to be implemented
     */

    public void updateView(DownData downData) {
        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());
        }
        refreshBrick(downData.getViewData());
    }

    private void setGhostStyle(Rectangle rectangle) {

        rectangle.setFill(Color.web("#666666", 0.3));

        rectangle.setStroke(Color.web("#666666", 0.6));

        rectangle.setArcHeight(10);
        rectangle.setArcWidth(10);
        rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        rectangle.setStrokeWidth(1);
    }

}
