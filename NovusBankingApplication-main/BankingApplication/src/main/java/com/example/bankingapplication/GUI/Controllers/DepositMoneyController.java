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
 * Controller for depositing money into accounts
 */
public class DepositMoneyController {

    /**
     * The deposit money button
     */
    @FXML
    protected Button depositMoneyButton;

    /**
     * The amount to deposit text field
     */
    @FXML
    protected TextField amountToDepositTextField;

    /**
     * The account to deposit money
     */
    private Account account = null;

    /**
     * THe back button
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
     * creating a helper button on Deposit Money View to guide user on available actions
     */
    public void needHelp() {
        helper.needHelp(button_need_help, """
                To deposit money into an account enter the amount in the 'Amount to deposit' field.
                All deposited money will be in pounds (£)
                To submit this change click the 'Deposit' button.
                               
                To return to the previous screen click the 'Cancel' button.
                """);
    }

    /**
     * The back button which goes back to the account received options controller
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
     * Deposits money into the account
     * Verifies the money before trying to deposit
     * @throws SQLException e
     */
    public void depositMoney() throws SQLException, IOException {

        InputVerifier verifier = new InputVerifier();
        boolean canSend = true;

        BigDecimal value = null;

        if (verifier.isANumber(amountToDepositTextField.getText())) {
            value = new BigDecimal(amountToDepositTextField.getText());
        } else {
            canSend = false;
        }

        if (canSend) {
            int senderAccountNumber = account.getAccountNumber();
            int senderSortCode = account.getSortCode();
            helper.closePopUp();
            switch (account.getAccountType()) {
                case "PersonalAccount" -> {
                    PersonalAccount personalAccount = (PersonalAccount) account;
                    personalAccount.confirmDeposit(senderAccountNumber,senderSortCode,value);
                    back();
                    break;
                }
                case "ISAAccount" -> {
                    ISAAccount isaAccount = (ISAAccount) account;
                    isaAccount.increaseMoneyInThisYear(senderAccountNumber,senderSortCode,value);
                    back();
                    break;
                }
                case "BusinessAccount" -> {
                    BusinessAccount businessAccount = (BusinessAccount) account;
                    businessAccount.confirmDeposit(senderAccountNumber,senderSortCode,value);
                    back();
                    break;
                }
            }

        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please make sure all the details are correct!");
            alert.show();
        }

    }

}
