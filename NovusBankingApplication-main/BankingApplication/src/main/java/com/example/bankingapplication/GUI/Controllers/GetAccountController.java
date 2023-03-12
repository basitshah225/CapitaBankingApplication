package com.example.bankingapplication.GUI.Controllers;

import com.example.bankingapplication.Accounts.Account;
import com.example.bankingapplication.Accounts.AccountManager;
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
 * Controller for getting an account
 */
public class GetAccountController {


    /**
     * The button to get the account
     */
    @FXML
    protected Button getAccountButton;

    /**
     *
     */
    @FXML
    protected TextField firstNameTextField;

    /**
     *
     */
    @FXML
    protected TextField lastNameTextField;

    /**
     *
     */
    @FXML
    protected TextField addressTextField;

    /**
     *
     */
    @FXML
    protected TextField accountNumberTextField;

    /**
     *
     */
    @FXML
    protected TextField sortCodeTextField;

    /**
     *
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
     * creating a helper button on Get Account View to guide user on available actions
     */
    public void needHelp() {
        helper.needHelp(button_need_help, """
                - ACCOUNT NUMBER: Enter the account number into the 'Account Number' field. This will be at least 6 digits.
                - SORTCODE: Enter the 6 digit sort code into the 'SortCode' field.
                - FIRST NAME: Enter first name and Middle name (if applicable) to the 'First Name' field.
                - LAST NAME: Enter all last names into the 'Last Name' field.
                - POSTCODE: Enter a valid UK postcode including a space were applicable. All characters must be capital letters.
                                
                - When all the fields are entered click 'Get Account'.
                                
                To return to the previous screen click the 'Cancel' button.
                                
                """);
    }

    /**
     * Switches the scene back to the accounts view
     */
    public void back() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        helper.closePopUp();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        sceneSwitcher.switchScene(stage, "FXMLFiles/AccountsView.fxml", "");
    }

    /**
     * Clears the text fields
     */
    public void clearFields(){
        firstNameTextField.setText("");
        lastNameTextField.setText("");
        addressTextField.setText("");
        accountNumberTextField.setText("");
        sortCodeTextField.setText("");
    }

    /**
     * Gets the account using the provided information
     * Will not load if the account doesn't exist
     * Verifies all the input information before getting
     * @throws SQLException e
     * @throws IOException i
     */
    public void getAccount() throws SQLException, IOException {
        InputVerifier verifier = new InputVerifier();
        boolean canGetAccount = true;

        String firstName = firstNameTextField.getText();
        if(!verifier.verifyName(firstName)){
            canGetAccount = false;
        }

        String lastName = lastNameTextField.getText();
        if(!verifier.verifyName(lastName)){
            canGetAccount = false;
        }

        String address = addressTextField.getText();
        if(!verifier.verifyAddress(address)){
            canGetAccount = false;
        }

        int accountNumber = -1;

        if(!verifier.verifyAccountNumber(accountNumberTextField.getText())){
            canGetAccount = false;
        }
        else{
            accountNumber = Integer.parseInt(accountNumberTextField.getText());
        }

        int sortCode = -1;

        if(!verifier.verifyAccountNumber(sortCodeTextField.getText())){
            canGetAccount = false;
        }
        else{
            sortCode = Integer.parseInt(sortCodeTextField.getText());
        }

        if(canGetAccount){
            AccountManager accountManager = new AccountManager();
            Account account = accountManager.getAccount(accountNumber, sortCode, firstName, lastName, address);
            if(account !=null){
                Stage stage = (Stage) getAccountButton.getScene().getWindow();
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

                clearFields();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Could not find the account!");
                alert.show();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please make sure all the details are correct and in the right format");
            alert.show();
        }




    }
}
