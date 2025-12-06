package com.tetris.ui;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.animation.ParallelTransition;
import javafx.animation.FadeTransition;
import java.util.List;

import java.util.Random;

public class BoardAnimator {

    public void shake(Node node) {
        if (node == null) return;

        TranslateTransition tt = new TranslateTransition(Duration.millis(50), node);
        tt.setFromX(0);
        tt.setByX(5); // Move right 5px
        tt.setCycleCount(4); // Shake 2 times
        tt.setAutoReverse(true);
        tt.setOnFinished(e -> node.setTranslateX(0)); // Reset position ensure it's centered
        tt.play();
    }

    public void startLogoPulse(Node node) {
        if (node == null) return;

        ScaleTransition pulse = new ScaleTransition(Duration.seconds(1.5), node);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.05);
        pulse.setToY(1.05);
        pulse.setCycleCount(ScaleTransition.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();
    }

    public void spawnClearParticles(Pane parent, List<Integer> rowIndices, List<int[]> rowData) {
        if (rowIndices == null || rowData == null || rowIndices.size() != rowData.size()) return;

        Random rand = new Random();
        BoardStyler styler = new BoardStyler();

        for (int i = 0; i < rowIndices.size(); i++) {
            int rowIndex = rowIndices.get(i);
            int[] colors = rowData.get(i);

            // (Row - 2 hidden rows) * (20px size + 1px gap)
            double yBase = (rowIndex - 2) * 21;

            for (int col = 0; col < colors.length; col++) {
                int colorCode = colors[col];
                if (colorCode == 0) continue;

                Rectangle temp = new Rectangle();
                styler.styleRectangle(temp, colorCode, false);
                javafx.scene.paint.Paint blockColor = temp.getFill();

                double xBase = col * 21;

                // Spawn 4 shards per block for "crumbling" look
                for (int j = 0; j < 4; j++) {
                    Rectangle particle = new Rectangle(8, 8, blockColor);
                    particle.setTranslateX(xBase + rand.nextInt(12));
                    particle.setTranslateY(yBase + rand.nextInt(12));

                    parent.getChildren().add(particle);
                    animateDebris(parent, particle);
                }
            }
        }
    }

    private void animateDebris(Pane parent, Rectangle p) {
        Random rand = new Random();

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.8), p);
        tt.setByY(30 + rand.nextInt(40));        // Fall down
        tt.setByX((rand.nextDouble() - 0.5) * 60); // Spread out

        javafx.animation.RotateTransition rt = new javafx.animation.RotateTransition(Duration.seconds(0.8), p);
        rt.setByAngle(rand.nextInt(360)); // Spin

        FadeTransition ft = new FadeTransition(Duration.seconds(0.8), p);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);

        ParallelTransition pt = new ParallelTransition(tt, rt, ft);
        pt.setOnFinished(e -> parent.getChildren().remove(p));
        pt.play();
    }
}