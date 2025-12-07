package com.tetris.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * A temporary UI component used to display floating score bonuses.
 * <p>
 * This class creates a styled label (e.g., "+100") that performs a specific animation:
 * drifting upward while fading out. Once the animation completes, the panel automatically
 * removes itself from the scene graph to prevent memory leaks and visual clutter.
 *
 * @author Jacob Villegas
 */

public class NotificationPanel extends BorderPane {

    /**
     * Constructs a new notification overlay.
     * <p>
     * Initializes the panel with a minimum size and creates a glowing label with
     * the specified text.
     *
     * @param text The string to display (e.g., "+300").
     */
    public NotificationPanel(String text) {
        setMinHeight(200);
        setMinWidth(220);
        final Label score = new Label(text);
        score.getStyleClass().add("bonusStyle");
        final Effect glow = new Glow(0.6);
        score.setEffect(glow);
        score.setTextFill(Color.WHITE);
        setCenter(score);

    }

    /**
     * Starts the entrance and exit animation.
     * <p>
     * The animation consists of:
     * <ul>
     * <li><b>Fade:</b> Opacity goes from 1.0 to 0.0 over 2 seconds.</li>
     * <li><b>Translate:</b> Moves 40 pixels upward over 2.5 seconds.</li>
     * </ul>
     * When the animation finishes, the {@code onFinished} handler triggers, removing
     * this node from the parent {@code list}.
     *
     * @param list The observable list of children from the parent container (used to remove itself).
     */
    public void showScore(ObservableList<Node> list) {
        FadeTransition ft = new FadeTransition(Duration.millis(2000), this);
        TranslateTransition tt = new TranslateTransition(Duration.millis(2500), this);
        tt.setToY(this.getLayoutY() - 40);
        ft.setFromValue(1);
        ft.setToValue(0);
        ParallelTransition transition = new ParallelTransition(tt, ft);
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                list.remove(NotificationPanel.this);
            }
        });
        transition.play();
    }
}
