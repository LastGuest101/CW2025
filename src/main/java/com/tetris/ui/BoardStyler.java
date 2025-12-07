package com.tetris.ui;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import javafx.scene.layout.Region;

/**
 * Handles the visual styling and coloring of individual Tetris blocks and the board.
 * <p>
 * This class acts as the "Painter" for the game. It applies fill colors, border strokes,
 * and effects (like Glow or Shadow) to the JavaFX {@link Rectangle} nodes.
 * <p>
 * It supports two distinct visual modes:
 * <ul>
 * <li><b>Normal Mode:</b> Bricks are colored based on their ID (Pastel/Fruit theme).</li>
 * <li><b>Frozen Mode:</b> All bricks turn cyan/blue with a glowing ice effect (Power-up state).</li>
 * </ul>
 *
 * @author Jacob Villegas
 */
public class BoardStyler {

    /** Tracks whether the "Time Freeze" power-up is currently active. */
    private boolean isFrozen = false; // New state

    /**
     * Updates the internal frozen state flag.
     *
     * @param frozen {@code true} if the freeze power-up is active; {@code false} otherwise.
     */
    public void setFrozen(boolean frozen) {
        this.isFrozen = frozen;
    }

    /**
     * Toggles the CSS classes on the main game containers to reflect the Frozen state.
     * <p>
     * This adds a CSS class (e.g., "frozen-board") to the root pane, allowing the
     * stylesheet to change the background image or border colors globally.
     *
     * @param isFrozen  {@code true} to apply the ice theme; {@code false} to revert to normal.
     * @param rootPane  The main window container.
     * @param gamePanel The specific grid container.
     * @param gameBoard The background board region.
     */
    public void updateFrozenTheme(boolean isFrozen, Region rootPane, Region gamePanel, Region gameBoard) {
        setFrozen(isFrozen);
        toggleStyle(gameBoard, "frozen-board", isFrozen);
    }

    /**
     * Helper: Adds or removes a specific CSS class from a JavaFX node.
     *
     * @param node       The UI element to modify.
     * @param styleClass The CSS class name (e.g., "frozen-board").
     * @param active     {@code true} to add the class; {@code false} to remove it.
     */
    private void toggleStyle(Region node, String styleClass, boolean active) {
        if (active) {
            if (!node.getStyleClass().contains(styleClass)) {
                node.getStyleClass().add(styleClass);
            }
        } else {
            node.getStyleClass().removeAll(styleClass);
        }
    }

    /**
     * Applies the correct visual style to a single block (rectangle) in the grid.
     * <p>
     * This method handles:
     * <ol>
     * <li><b>Fill Color:</b> Determines if it should use the brick's specific ID color or the generic "Ice" color.</li>
     * <li><b>Stroke/Border:</b> Adds a darker border for depth.</li>
     * <li><b>Effects:</b> Adds a glowing {@link DropShadow} (Soft gray for normal, bright Cyan for frozen).</li>
     * <li><b>Visibility:</b> Hides empty blocks (ID 0) unless they are part of the grid background lines.</li>
     * </ol>
     *
     * @param rectangle    The JavaFX node to style.
     * @param colorId      The integer ID of the brick type (0=Empty, 1=I, 2=J, etc.).
     * @param isBackground {@code true} if this rectangle represents a locked background block;
     * {@code false} if it is part of the falling active piece.
     */

    public void styleRectangle(Rectangle rectangle, int colorId, boolean isBackground) {
        Paint baseColor;

        // Determine Color
        if (colorId == 0) {
            baseColor = Color.TRANSPARENT;
        } else if (isFrozen) {
            // ICE MODE: All blocks turn Cyan/Blue
            baseColor = Color.web("#4facfe");
        } else {
            // NORMAL MODE
            baseColor = getFillColor(colorId);
        }

        rectangle.setFill(baseColor);
        rectangle.setArcHeight(10);
        rectangle.setArcWidth(10);

        if (colorId != 0) {
            // Active Brick Style
            if (baseColor instanceof Color c) {
                rectangle.setStroke(c.darker().darker());
            } else {
                rectangle.setStroke(Color.BLACK);
            }
            rectangle.setStrokeType(StrokeType.INSIDE);
            rectangle.setStrokeWidth(2);
            rectangle.setVisible(true);

            // Custom Effect for Ice
            DropShadow glow = new DropShadow();
            if (isFrozen) {
                // Icy Glow (Cyan/White)
                glow.setColor(Color.CYAN);
                glow.setRadius(10);
                glow.setSpread(0.3);
            } else {
                // Normal Glow
                glow.setColor(baseColor instanceof Color c ? c : Color.GRAY);
                glow.setRadius(2);
                glow.setSpread(0);
            }
            rectangle.setEffect(glow);

        } else {
            rectangle.setEffect(null);
            if (isBackground) {
                rectangle.setStroke(Color.rgb(160, 82, 45, 0.25));
                rectangle.setStrokeType(StrokeType.INSIDE);
                rectangle.setStrokeWidth(1);
                rectangle.setVisible(true);
            } else {
                rectangle.setStroke(null);
                rectangle.setVisible(false);
            }
        }
    }

    /**
     * Styles the "Ghost" piece (the transparent shadow showing where the block will land).
     * <p>
     * The ghost style also adapts to the frozen state:
     * <ul>
     * <li><b>Normal:</b> Gray, semi-transparent.</li>
     * <li><b>Frozen:</b> Bright Cyan, thicker border, more opaque.</li>
     * </ul>
     *
     * @param rectangle The ghost block to style.
     */
    public void styleGhost(Rectangle rectangle) {
        if (isFrozen) {
            // FROZEN GHOST STYLE
            rectangle.setFill(Color.web("#A5F2F3", 0.4));

            rectangle.setStroke(Color.web("#00CED1", 0.6));

            // Thicker border to make it pop
            rectangle.setStrokeWidth(2);
        } else {
            // NORMAL GHOST STYLE
            // Grey shadow
            rectangle.setFill(Color.web("#666666", 0.3));
            rectangle.setStroke(Color.web("#666666", 0.6));
            rectangle.setStrokeWidth(1);
        }

        // Common settings
        rectangle.setArcHeight(10);
        rectangle.setArcWidth(10);
        rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setVisible(true);
    }

    /**
     * Maps a brick ID integer to a specific pastel color hex code.
     *
     * @param i The brick ID (1-7).
     * @return The corresponding {@link Color} object.
     */
    private Paint getFillColor(int i) {
        return switch (i) {
            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.web("#FFADAD"); // Strawberry
            case 2 -> Color.web("#FFD6A5"); // Peach
            case 3 -> Color.web("#FDFFB6"); // Pineapple
            case 4 -> Color.web("#CAFFBF"); // Melon
            case 5 -> Color.web("#9BF6FF"); // Watermelon
            case 6 -> Color.web("#A0C4FF"); // Blueberry
            case 7 -> Color.web("#BDB2FF"); // Grape
            default -> Color.WHITE;
        };
    }
}