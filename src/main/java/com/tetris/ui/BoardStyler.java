package com.tetris.ui;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import javafx.scene.layout.Region;

public class BoardStyler {

    private boolean isFrozen = false; // New state

    public void setFrozen(boolean frozen) {
        this.isFrozen = frozen;
    }

    public void updateFrozenTheme(boolean isFrozen, Region rootPane, Region gamePanel, Region gameBoard) {
        setFrozen(isFrozen);
        toggleStyle(gameBoard, "frozen-board", isFrozen);
    }

    private void toggleStyle(Region node, String styleClass, boolean active) {
        if (active) {
            if (!node.getStyleClass().contains(styleClass)) {
                node.getStyleClass().add(styleClass);
            }
        } else {
            node.getStyleClass().removeAll(styleClass);
        }
    }



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