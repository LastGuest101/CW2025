package com.tetris; // Refactoring: Meaningful package organization

import com.tetris.gameLogic.GameController;
import com.tetris.ui.GuiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    private static final String APP_TITLE = "TetrisJFX";
    private static final String LAYOUT_RESOURCE = "/gameLayout.fxml"; // Use absolute path
    private static final int WINDOW_WIDTH =850;
    private static final int WINDOW_HEIGHT = 600;

    private GameController gameController;

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(LAYOUT_RESOURCE));
            Parent root = fxmlLoader.load();

            GuiController guiController = fxmlLoader.getController();

            this.gameController = new GameController(guiController);

            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

            primaryStage.setTitle(APP_TITLE);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load application layout.");
        }
    }

    @Override
    public void stop() throws Exception {
        if (gameController != null) {
            gameController.stopGame();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}