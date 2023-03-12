package com.example.bankingapplication.GUI;

import com.example.bankingapplication.GUIMain;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class to switch the scenes
 */
public class SceneSwitcher {

    /**
     * Loads the scene information
     */
    SceneInformation sceneInformation = new SceneInformation();

    /**
     * Switches the scene to the scene named passed in and returns the controller object
     * @param stage The stage to set the scene
     * @param fxmlName The name of the fxml file
     * @param title The title of the screen
     * @return The controller of the scene
     * @throws IOException e
     */
    public Object switchScene(Stage stage, String fxmlName, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIMain.class.getResource(fxmlName));
        Scene scene = new Scene(fxmlLoader.load(), sceneInformation.sceneWidth(), sceneInformation.sceneHeight());
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
        return fxmlLoader.getController();
    }

    /**
     * Switches the scene to the scene named passed in and returns the controller object
     * @param stage The stage to set the scene
     * @param fxmlName The name of the fxml file
     * @param title The title of the screen
     * @param width The width of the scene
     * @param height The height of the scene
     * @throws IOException e
     */
    public void switchScene(Stage stage, String fxmlName, String title, int width, int height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIMain.class.getResource(fxmlName));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

}
