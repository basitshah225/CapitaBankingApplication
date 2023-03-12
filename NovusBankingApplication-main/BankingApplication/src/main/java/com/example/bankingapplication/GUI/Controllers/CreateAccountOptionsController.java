package com.example.bankingapplication.GUI.Controllers;

import com.example.bankingapplication.GUI.SceneSwitcher;
import com.example.bankingapplication.Helper.Helper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for creating a personal account
 */
public class CreateAccountOptionsController {

    /**
     * The create personal account option button
     */
    @FXML
    protected Button createPersonalAccountButton;

    /**
     * The create ISA account option button
     */
    @FXML
    protected Button createISAAccountButton;

    /**
     * The create business account option button
     */
    @FXML
    protected Button createBusinessAccountButton;

    /**
     * The back button
     */
    @FXML
    protected Button backButton;

    @FXML
    protected Button button_need_help;

    /**
     * The helper class which can be used for the help buttons
     */
    private final Helper helper = new Helper();

    /**
     * HELPER
     * creating a helper button on Create Account Options View to guide user on available actions
     */
    public void needHelp() {
        helper.needHelp(button_need_help, """
                To create a new Personal Account click the 'Create Personal Account' button.
                
                To create a new ISAAccount click the 'Create ISA Account' button.
                
                To create a new Business Account click the 'Create Business Account' button.
                
                To go back to the previous screen click the 'Back' button.
                """);
    }
    /**
     * Changes the scene back to getting / creating an account
     */
    public void back() throws IOException {
        Stage stage = (Stage) createPersonalAccountButton.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        sceneSwitcher.switchScene(stage, "FXMLFiles/AccountsView.fxml", "");
        helper.closePopUp();
    }

    /**
     * Switches the scene to the create personal account view
     * @throws IOException e
     */
    @FXML
    protected void onCreatePersonalAccountButtonClicked() throws IOException {
        Stage stage = (Stage) createPersonalAccountButton.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        sceneSwitcher.switchScene(stage, "FXMLFiles/CreatePersonalAccountView.fxml", "");
        helper.closePopUp();
    }

    /**
     * Switches the scene to the create ISA account view
     * @throws IOException e
     */
    @FXML
    protected void onCreateISAAccountButtonClicked() throws IOException {
        Stage stage = (Stage) createPersonalAccountButton.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        sceneSwitcher.switchScene(stage, "FXMLFiles/CreateISAAccountView.fxml", "");
        helper.closePopUp();
    }

    /**
     * Switches the scene to the create Business account view
     * @throws IOException e
     */
    @FXML
    protected void onCreateBusinessAccountButtonClicked() throws IOException {
        Stage stage = (Stage) createPersonalAccountButton.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        sceneSwitcher.switchScene(stage, "FXMLFiles/CreateBusinessAccountView.fxml", "");
        helper.closePopUp();
    }

}
