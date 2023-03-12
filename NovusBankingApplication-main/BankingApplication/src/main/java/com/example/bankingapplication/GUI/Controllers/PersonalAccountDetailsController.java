package com.example.bankingapplication.GUI.Controllers;

import com.example.bankingapplication.Accounts.Account;
import com.example.bankingapplication.Accounts.PersonalAccount;
import com.example.bankingapplication.GUI.SceneSwitcher;
import com.example.bankingapplication.Helper.Helper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for showing a personal accounts details
 */
public class PersonalAccountDetailsController {

    /**
     * The account to show the details of
     */
    private Account account;

    /**
     * The overdraft amount text field
     */
    @FXML
    protected TextField overDraftAmountTextField;

    /**
     * The photo id number text field
     */
    @FXML
    protected TextField photoIDNumberTextField;

    /**
     * The balance text field
     */
    @FXML
    protected TextField balanceTextField;

    /**
     * The address text field
     */
    @FXML
    protected TextField addressTextField;

    /**
     * The last name text field
     */
    @FXML
    protected TextField lastNameTextField;

    /**
     * The first name text field
     */
    @FXML
    protected TextField firstNameTextField;

    /**
     * The checkbox for the photo id type, already set
     */
    @FXML
    protected ChoiceBox<String> photoIDTypeChoiceBox;

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
     * The back button to change the scene back to account received view
     * @throws IOException e
     */
    public void back() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        helper.closePopUp();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        switch (account.getAccountType()) {
            case "PersonalAccount" -> {
                AccountReceivedOptionsPersonalController controller = (AccountReceivedOptionsPersonalController) sceneSwitcher.switchScene(stage, "FXMLFiles/AccountReceivedOptionsPersonalView.fxml", "");
                controller.setAccount(account);
                break;
            }
            case "ISAAccount" -> {
                AccountReceivedOptionsISAController controller = (AccountReceivedOptionsISAController) sceneSwitcher.switchScene(stage, "FXMLFiles/AccountReceivedOptionsISAView.fxml", "");
                controller.setAccount(account);
                break;
            }
            case "BusinessAccount" -> {
                AccountReceivedOptionsBusinessController controller = (AccountReceivedOptionsBusinessController) sceneSwitcher.switchScene(stage, "FXMLFiles/AccountReceivedOptionsBusinessView.fxml", "");
                controller.setAccount(account);
                break;
            }
        }
    }

    /**
     * @param account Sets the account
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * Sets all the details in the text fields using the accounts details
     * @throws SQLException e
     */
    public void setAccountDetails() throws SQLException {
        balanceTextField.setText(String.valueOf(account.getBalance()));
        firstNameTextField.setText(account.getFirstName());
        lastNameTextField.setText(account.getLastName());
        addressTextField.setText(account.getAddress());
        photoIDNumberTextField.setText(account.getPhotoIDNumber());
        photoIDTypeChoiceBox.setValue(String.valueOf(account.getPhotoIDType()));
        if(account.getAccountType().equals("PersonalAccount")) {
            PersonalAccount personalAccount = (PersonalAccount) account;
            overDraftAmountTextField.setText(String.valueOf(personalAccount.getOverDraftAmount()));
        }
    }
}
