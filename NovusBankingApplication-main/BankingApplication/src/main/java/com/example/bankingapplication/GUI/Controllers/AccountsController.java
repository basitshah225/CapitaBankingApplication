package com.example.bankingapplication.GUI.Controllers;

import com.example.bankingapplication.Accounts.Account;
import com.example.bankingapplication.GUI.SceneSwitcher;

import com.example.bankingapplication.Helper.Helper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for getting or setting account
 */
public class AccountsController {

    /**
     * Button for create new account
     */
    @FXML
    protected Button createNewAccountButton;

    /**
     * Button for get account
     */
    @FXML
    protected Button getAccountButton;

    /**
     * Button to log out
     */
    @FXML
    protected Button button_logout;

    /**
     * Label for the teller
     */
    @FXML
    protected Label label_hello;
    @FXML
    protected Button button_need_help;

    /**
     * The helper class which can be used for the help buttons
     */
    private final Helper helper = new Helper();

    /**
     * HELPER
     * creating a helper button on Accounts View to guide user to enter create an account or view an existing account
     */
    public void needHelp() {
        helper.needHelp(button_need_help, """
                To create a new Personal, ISA or Business
                account press 'Create a new Account'.
                
                To view you an existing account or change
                your information press 'Alter / Display 
                information for an existing account'.
                """);
    }



    /**
     * Changes the scene to allow the teller to create a new account
     * @throws IOException i
     */
    @FXML
    protected void onNewAccountClicked() throws IOException {
        Stage stage = (Stage) getAccountButton.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        sceneSwitcher.switchScene(stage, "FXMLFiles/CreateAccountOptionsView.fxml", "");
        helper.closePopUp();
    }

    /**
     * Changes the scene to allow the teller to get an account
     * @throws IOException i
     */
    @FXML
    protected void onGetAccountClicked() throws IOException {
        Stage stage = (Stage) getAccountButton.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        sceneSwitcher.switchScene(stage, "FXMLFiles/GetAccountView.fxml", "");
        helper.closePopUp();
    }


    /**
     * Sets the user information in the label
     * @param username The username to set it to
     */
    public void setUserInformation(String username) {
        label_hello.setText(("Hello" + username + "!"));
    }

    /**
     * Logs the user back out to the log in scene
     * @throws IOException e
     */
    public void logOut() throws IOException {
        Stage stage = (Stage) getAccountButton.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        sceneSwitcher.switchScene(stage, "FXMLFiles/LogInView.fxml", "");
        helper.closePopUp();

    }
}



