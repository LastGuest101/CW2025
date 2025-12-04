package com.tetris.ui;

import javafx.scene.Group;

public class NotificationRenderer {

    private final Group notificationGroup;

    public NotificationRenderer(Group notificationGroup) {
        this.notificationGroup = notificationGroup;
    }

    public void showScoreBonus(int scoreBonus) {
        if (scoreBonus > 0) {
            NotificationPanel notificationPanel = new NotificationPanel("+" + scoreBonus);
            notificationGroup.getChildren().add(notificationPanel);
            notificationPanel.showScore(notificationGroup.getChildren());
        }
    }
}