package com.example.bankingapplication.GUI.Controllers;

import com.example.bankingapplication.Accounts.Account;
import com.example.bankingapplication.Accounts.BusinessAccount;
import com.example.bankingapplication.Accounts.ISAAccount;
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
 * Controller for showing the business account details
 */
public class BusinessAccountDetailsController {

    /**
     * The account to perform on
     */
    private Account account;

    /**
     * The create account button
     */
    @FXML
    protected Button createAccountButton;

    /**
     * The business ID text field
     */
    @FXML
    protected TextField businessIDTextField;

    /**
     * The photo number text field
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
     * The first last text field
     */
    @FXML
    protected TextField lastNameTextField;

    /**
     * The first name text field
     */
    @FXML
    protected TextField firstNameTextField;

    /**
     * The loan amount text field
     */
    @FXML
    protected TextField loanAmountTextField;

    /**
     * The over draft amount text field
     */
    @FXML
    protected TextField overDraftAmountTextField;

    /**
     * The photo id checkbox
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
    private final Helper helper = new Helper();

    /**
     * Goes back to the account options scene
     * @throws IOException e
     */
    public void back() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        helper.closePopUp();
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
     * Sets all the labels in the information display to the accounts field
     * @throws SQLException e
     */
    public void setAccountDetails() throws SQLException {
        balanceTextField.setText(String.valueOf(account.getBalance()));
        firstNameTextField.setText(account.getFirstName());
        lastNameTextField.setText(account.getLastName());
        addressTextField.setText(account.getAddress());
        photoIDNumberTextField.setText(account.getPhotoIDNumber());
        photoIDTypeChoiceBox.setValue(String.valueOf(account.getPhotoIDType()));
        if(account.getAccountType().equals("BusinessAccount")) {
            BusinessAccount businessAccount = (BusinessAccount) account;
            loanAmountTextField.setText(String.valueOf(businessAccount.getLoanAmount()));
            overDraftAmountTextField.setText(String.valueOf(businessAccount.getOverDraftAmount()));
            businessIDTextField.setText(String.valueOf(businessAccount.getBusinessID()));
        }
    }

}
