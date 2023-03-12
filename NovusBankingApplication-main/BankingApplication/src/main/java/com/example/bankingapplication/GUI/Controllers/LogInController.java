package com.example.bankingapplication.GUI.Controllers;

import com.example.bankingapplication.Helper.Helper;
import com.example.bankingapplication.Database.TellerHandler;
import com.example.bankingapplication.GUI.SceneSwitcher;
import com.example.bankingapplication.Threads.ThreadRunner;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The controller to control the log in script
 */
public class LogInController {


    /**
     * The close program button
     */
    @FXML
    protected Button closeProgramButton;

    /**
     * The login button
     */
    @FXML
    protected Button button_login;

    /**
     * The need help button
     */
    @FXML
    protected Button button_need_help;

    /**
     * The username text field
     */
    @FXML
    protected TextField text_field_username;

    /**
     * The password text field
     */
    @FXML
    protected TextField text_field_password;

    /**
     * The method to close the program
     * Cancels the threads by calling an instance of thread runner
     */
    public void closeProgram() {
        System.out.println("Closing Program");
        Stage stage = (Stage) button_login.getScene().getWindow();
        stage.close();
        ThreadRunner threadRunner = ThreadRunner.getInstance();
        threadRunner.cancelThread();
    }

    /**
     * Logs the user in by validating the teller on the database
     * Alert is popped up if the user does not log in
     */
    public void logInUser() throws SQLException {
        System.out.println("in login");
        // connection to database
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        TellerHandler tellerhandler = new TellerHandler();
        boolean verified = tellerhandler.validateTeller(text_field_username.getText(), text_field_password.getText());
       // try catch and if statement which allows user access if a correct username and password combination is correct
        try {
            if (!verified) {
                System.out.println("Details incorrect");
                // if detail incorrect an alert pops up to the user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.show();
            } else {
                Stage stage = (Stage) button_login.getScene().getWindow();
                SceneSwitcher sceneswitcher = new SceneSwitcher();
                sceneswitcher.switchScene(stage, "FXMLFiles/AccountsView.fxml", " ");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        helper.closePopUp();
    }

    /**
     * The helper class which can be used for the help buttons
     */
    private final Helper helper = new Helper();

    /**
     * HELPER
     * creating a helper button on login page to guide user to enter username and password
     */
    public void needHelp() {
        helper.needHelp(button_need_help, """
                To login in please enter your username into 
                the 'Username' field and Password into the 
                'Password' field. Note: Username and 
                Password are case sensitive.
                
                Click the 'Login' button to login.
                """);
    }

}




