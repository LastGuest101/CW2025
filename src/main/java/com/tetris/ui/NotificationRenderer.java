package com.tetris.ui;

import javafx.scene.Group;

/**
 * Manages the lifecycle and placement of floating score notifications.
 * <p>
 * This class acts as a bridge between the game logic (which knows <i>when</i> points are earned)
 * and the scene graph (where the notifications need to appear). It handles instantiating
 * {@link NotificationPanel} objects and adding them to the specific UI layer designated
 * for notifications.
 *
 * @author Jacob Villegas
 */
public class NotificationRenderer {

    private final Group notificationGroup;

    /**
     * Constructs a new renderer targeting a specific UI group.
     *
     * @param notificationGroup The parent container (usually defined in FXML) where
     * floating text should appear.
     */
    public NotificationRenderer(Group notificationGroup) {
        this.notificationGroup = notificationGroup;
    }

    /**
     * Creates and displays a floating score notification if points were earned.
     * <p>
     * This method creates a new {@link NotificationPanel}, adds it to the scene,
     * and triggers its internal animation sequence. If the score bonus is 0 or negative,
     * no notification is spawned.
     *
     * @param scoreBonus The number of points to display (e.g., 100).
     */
    public void showScoreBonus(int scoreBonus) {
        if (scoreBonus > 0) {
            NotificationPanel notificationPanel = new NotificationPanel("+" + scoreBonus);
            notificationGroup.getChildren().add(notificationPanel);
            notificationPanel.showScore(notificationGroup.getChildren());
        }
    }
}