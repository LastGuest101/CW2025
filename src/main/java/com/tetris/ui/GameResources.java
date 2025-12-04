package com.tetris.ui;

import javafx.scene.text.Font;

public class GameResources {

    public static void loadFonts() {
        try {
            var fontUrl = GameResources.class.getClassLoader().getResource("digital.ttf");
            if (fontUrl != null) {
                Font.loadFont(fontUrl.toExternalForm(), 38);
            } else {
                System.err.println("Font 'digital.ttf' not found in resources.");
            }
        } catch (Exception e) {
            System.err.println("Could not load font: " + e.getMessage());
        }
    }
}