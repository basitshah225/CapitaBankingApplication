package com.example.bankingapplication.GUI.Controllers;

import com.example.bankingapplication.Accounts.Account;
import com.example.bankingapplication.Accounts.BusinessAccount;
import com.example.bankingapplication.Accounts.ISAAccount;
import com.example.bankingapplication.Accounts.PersonalAccount;
import com.example.bankingapplication.GUI.SceneSwitcher;
import com.example.bankingapplication.Helper.Helper;
import com.example.bankingapplication.Security.InputVerifier;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Controller for send money between accounts
 */
public class sendMoneyToOtherAccountController {

    /**
     * The account to send money from
     */
    private Account account = null;

    /**
     * The send money button
     */
    @FXML
    protected Button sendMoneyButton;

    /**
     * The receiving account ID text field
     */
    @FXML
    protected TextField receivingAccountIDTextField;

    /**
     * The receiving sort code text field
     */
    @FXML
    protected TextField receivingAccountSortCodeTextField;

    /**
     * The amount to send
     */
    @FXML
    protected TextField amountToSendTextField;

    /**
     * The button to go back to the account options
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
     * creating a helper button on Create ISA Account View to guide user on available actions
     */
    public void needHelp() {
        helper.needHelp(button_need_help, """
                To send funds to another account please complete all fields.
                - RECEIVING ACCOUNT ID: Enter the account ID of the account you want to send funds to.
                - RECEIVING ACCOUNT SORT CODE: Enter the 6 digit sort code of the account you want to send funds to.
                - AMOUNT TO SEND: Enter the amount in pounds (£) for the funds you want to send.
                - To submit the inputted details press the 'Send Money' button.
                
                To return to the previous screen click the 'Cancel' button.
                """);
    }

    /**
     * Goes back to accounts options
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
     * Clears the text fields
     */
    public void clearTextFields(){
        receivingAccountIDTextField.setText("");
        receivingAccountSortCodeTextField.setText("");
        amountToSendTextField.setText("");
    }

    /**
     * Sends the money between the accounts
     * Verifies all the information before calling the specific accounts send money method
     * @throws SQLException e
     */
    public void sendMoney() throws SQLException, IOException {
        InputVerifier verifier = new InputVerifier();

        boolean canSend = true;

        BigDecimal amountToSend = null;
        if(!verifier.isANumber(amountToSendTextField.getText())){
            canSend = false;

        }else{
            amountToSend = new BigDecimal(amountToSendTextField.getText());
        }

        int accountNumber = -1;
        if(verifier.verifyAccountNumber(receivingAccountIDTextField.getText())){
            accountNumber = Integer.parseInt(receivingAccountIDTextField.getText());
        }
        else{
            canSend = false;
        }

        int sortCode = -1;
        if(verifier.verifyAccountNumber(receivingAccountSortCodeTextField.getText())){
            sortCode = Integer.parseInt(receivingAccountSortCodeTextField.getText());
        }else{
            canSend = false;
        }

        if(canSend){
            int senderAccountNumber = account.getAccountNumber();
            int senderSortCode = account.getSortCode();
            switch (account.getAccountType()) {
                case "PersonalAccount" -> {
                    PersonalAccount personalAccount = (PersonalAccount) account;
                    personalAccount.sendMoneyToOtherAccount(senderAccountNumber,senderSortCode,amountToSend,accountNumber, sortCode);
                    back();
                    break;
                }
                case "ISAAccount" -> {
                    ISAAccount isaAccount = (ISAAccount) account;
                    isaAccount.sendMoneyToOtherAccount(senderAccountNumber,senderSortCode,amountToSend,accountNumber, sortCode);
                    back();
                    break;
                }
                case "BusinessAccount" -> {
                    BusinessAccount businessAccount = (BusinessAccount) account;
                    businessAccount.sendMoneyToOtherAccount(senderAccountNumber,senderSortCode,amountToSend,accountNumber, sortCode);
                    back();
                    break;
                }
            }
            clearTextFields();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please make sure all the details are correct and in the right format");
            alert.show();
        }

    }

    /**
     * @param account Sets the account
     */
    public void setAccount(Account account) {
        this.account = account;
    }
}
