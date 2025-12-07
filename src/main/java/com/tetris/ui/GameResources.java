package com.tetris.ui;

import javafx.scene.text.Font;

/**
 * A utility class responsible for loading global static assets.
 * <p>
 * Currently, this handles the registration of custom typography (specifically the
 * retro "Digital" font) so it can be applied to labels and score displays via CSS
 * or code.
 *
 * @author Jacob Villegas
 */
public class GameResources {

    /**
     * Attempts to load the custom "digital.ttf" font from the classpath resources.
     * <p>
     * If the font file is found, it is registered with the JavaFX graphics engine.
     * This allows the UI to use the font family (usually named "Digital-7") in
     * stylesheets.
     * <p>
     * If the file is missing or cannot be loaded, an error is printed to the standard
     * error stream, and the game will fallback to default system fonts.
     */
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