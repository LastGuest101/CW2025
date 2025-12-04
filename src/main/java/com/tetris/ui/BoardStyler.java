package com.tetris.ui;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class BoardStyler {

    public void styleRectangle(Rectangle rectangle, int colorId, boolean isBackground) {
        Paint baseColor = getFillColor(colorId);
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

            DropShadow glow = new DropShadow();
            glow.setColor(baseColor instanceof Color c ? c : Color.GRAY);
            glow.setRadius(2);
            glow.setSpread(0);
            rectangle.setEffect(glow);

        } else {
            rectangle.setEffect(null);
            if (isBackground) {
                // Faint grid lines for the board background
                rectangle.setStroke(Color.rgb(160, 82, 45, 0.25));
                rectangle.setStrokeType(StrokeType.INSIDE);
                rectangle.setStrokeWidth(1);
                rectangle.setVisible(true);
            } else {
                // Invisible for empty air in the "Next Brick" or falling piece
                rectangle.setStroke(null);
                rectangle.setVisible(false);
            }
        }
    }

    public void styleGhost(Rectangle rectangle) {
        rectangle.setFill(Color.web("#666666", 0.3));
        rectangle.setStroke(Color.web("#666666", 0.6));
        rectangle.setArcHeight(10);
        rectangle.setArcWidth(10);
        rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setStrokeWidth(1);
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