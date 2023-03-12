package com.example.bankingapplication.GUI.Controllers;

import com.example.bankingapplication.Accounts.Account;
import com.example.bankingapplication.Database.AccountHandler;
import com.example.bankingapplication.GUI.SceneSwitcher;
import com.example.bankingapplication.Helper.Helper;
import com.example.bankingapplication.Security.InputVerifier;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for changing customer details
 */
public class ChangeCustomerDetailsController {

    /**
     * The account to change details of
     */
    private Account account;

    /**
     * @param account Sets the account
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * The new last name text field
     */
    @FXML
    protected TextField newLastNameTextField;

    /**
     * The new address text field
     */
    @FXML
    protected TextField newAddressTextField;

    /**
     * To submit changes button
     */
    @FXML
    protected Button submitChangeButton;

    /**
     * The go back button
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
     * creating a helper button on Change Customer Details View to guide user on available
     */
    public void needHelp() {
        helper.needHelp(button_need_help, """
                - To change the name on the Account enter all new last names into the 'New Last Name' field
                - To change the postcode on the Account enter the new address into the 'New Postcode' field.
                - Then click the 'Submit Changed' button to finalise the changes
                
                Please leave both fields blank to keep the original details
                
                To go back to the previous screen click the 'Cancel' button.
                """);
    }
    /**
     * Goes back to the accounts option scene
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
     * Submits the changes to the customers details
     * Verifies the inputs before trying to change
     * Will change if the details are verified
     * @throws SQLException e
     */
    public void submitChanges() throws SQLException, IOException {

        InputVerifier verifier = new InputVerifier();

        boolean shouldUpdateLastName = true;
        boolean shouldUpdateAddress = true;
        String lastName = newLastNameTextField.getText();

        if(!verifier.verifyName(lastName) || lastName.isBlank()){
            shouldUpdateLastName = false;
        }

        String address = newAddressTextField.getText();

        if(!verifier.verifyAddress(newAddressTextField.getText()) || address.isBlank()){
            shouldUpdateAddress = false;
        }

        if(shouldUpdateLastName){
            AccountHandler accountHandler = new AccountHandler();
            accountHandler.updateAccountLastName(account.getAccountNumber(), account.getSortCode(),lastName);
            account.setLastName(lastName);
        }

        if(shouldUpdateAddress){
            AccountHandler accountHandler = new AccountHandler();
            accountHandler.updateAccountAddress(account.getAccountNumber(), account.getSortCode(),address);
            account.setAddress(address);

        }


        if(shouldUpdateAddress || shouldUpdateLastName){
            back();
        }


    }
}
