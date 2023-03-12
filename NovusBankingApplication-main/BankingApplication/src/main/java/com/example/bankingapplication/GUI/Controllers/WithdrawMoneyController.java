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
 * Controller for withdrawing money from accounts
 */
public class WithdrawMoneyController {

    /**
     * The account to withdraw money on
     */
    private Account account = null;

    /**
     * The amount to withdraw text field
     */
    @FXML
    protected TextField amountToWithDrawTextField;

    /**
     * The withdraw money button
     */
    @FXML
    protected Button withdrawMoneyButton;

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
     * creating a helper button on Withdraw Money View to guide user on available actions
     */
    public void needHelp() {
        helper.needHelp(button_need_help, """
                To withdraw funds from your account enter the amount you would like to
                withdraw in pounds (£) in the 'Amount to withdraw' field.
                To submit the amount you would like to withdraw click the 'Withdraw Money'.
                
                To return to the previous screen click the 'Cancel' button.
                """);
    }

    /**
     * Changes the scene back to the account received view
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
     * Withdraws money from each account
     * Verifies all input before trying to withdraw from the account
     * Calls each accounts confirm withdraw
     * @param account Sets the account
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    public void withdrawMoney() throws SQLException, IOException {

        InputVerifier verifier = new InputVerifier();
        boolean canSend = true;
        BigDecimal value = null;

        if (verifier.isANumber(amountToWithDrawTextField.getText())) {
            value = new BigDecimal(amountToWithDrawTextField.getText());
        } else {
            canSend = false;
        }

        if (canSend) {
            int senderAccountNumber = account.getAccountNumber();
            int senderSortCode = account.getSortCode();
            switch (account.getAccountType()) {
                case "PersonalAccount" -> {
                    PersonalAccount personalAccount = (PersonalAccount) account;
                    personalAccount.confirmWithdraw(senderAccountNumber, senderSortCode, value);
                    back();
                    break;
                }
                case "ISAAccount" -> {
                    ISAAccount isaAccount = (ISAAccount) account;
                    isaAccount.confirmWithdraw(senderAccountNumber,senderSortCode,value);
                    back();
                    break;
                }
                case "BusinessAccount" -> {
                    BusinessAccount businessAccount = (BusinessAccount) account;
                    businessAccount.confirmWithdraw(senderAccountNumber, senderSortCode, value);
                    back();
                    break;
                }
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Inputs not valid!");
            alert.show();
        }

    }



}
