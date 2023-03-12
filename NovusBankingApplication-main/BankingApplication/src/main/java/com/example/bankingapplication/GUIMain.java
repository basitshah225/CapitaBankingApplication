package com.example.bankingapplication;


import com.example.bankingapplication.GUI.SceneSwitcher;
import com.example.bankingapplication.Threads.ThreadRunner;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * The main class GUI
 */
public class GUIMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ThreadRunner threadRunner = ThreadRunner.getInstance();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        sceneSwitcher.switchScene(stage, "FXMLFiles/LogInView.fxml",  "Log In");
    }

    public static void main(String[] args) {
        launch();
    }
}