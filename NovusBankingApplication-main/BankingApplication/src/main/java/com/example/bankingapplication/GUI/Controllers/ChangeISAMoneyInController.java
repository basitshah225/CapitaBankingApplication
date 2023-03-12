package com.example.bankingapplication.GUI.Controllers;

import com.example.bankingapplication.Accounts.Account;
import com.example.bankingapplication.Accounts.ISAAccount;
import com.example.bankingapplication.GUI.SceneSwitcher;
import com.example.bankingapplication.Helper.Helper;
import com.example.bankingapplication.Security.InputVerifier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Controller increasing the money into the isa account
 */
public class ChangeISAMoneyInController {

    /**
     * The button to change the ISA money in
     */
    public Button changeISAButton;

    /**
     * The isa amount increase text field value
     */
    public TextField isaAmountIncreaseTextField;

    /**
     * The account to change the money on
     */
    private Account account;

    /**
     * @param account Sets the account
     */
    public void setAccount(Account account) {
        this.account = account;
    }

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
     * creating a helper button on Change ISA Money In  View to guide user on available actions
     */
    public void needHelp() {
        helper.needHelp(button_need_help, """
                - To increase the ISA deposit for the year enter the amount you want to
                  increase by in pounds (£) in the 'Increase ISA in this year by' field.
                - To submit this change click the 'Increase ISA In' button
                
                To go back to the previous screen click the 'Cancel' button.
                """);
    }

    /**
     * Goes back to the accounts options
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
     * Tries to increase the isa money in this year if the number is verified
     * All verification handled in increase money this year
     * @throws SQLException e
     */
    public void changeISAMoneyIn() throws SQLException, IOException {
        InputVerifier verifier = new InputVerifier();
        boolean increaseISA = true;
        BigDecimal increaseISABy = null;

        if(!verifier.verifyISAIncrease(isaAmountIncreaseTextField.getText())){
            increaseISA = false;
        }else{
            increaseISABy = new BigDecimal(isaAmountIncreaseTextField.getText());
        }

        if(increaseISA &&  account.getAccountType().equals("ISAAccount")){
            ISAAccount acc = (ISAAccount) account;
            int sortCode =  account.getSortCode();
            int accountNumber =account.getAccountNumber();
            acc.increaseMoneyInThisYear(accountNumber,sortCode,increaseISABy);
            back();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please make sure all the details are correct!");
            alert.show();
        }
    }
}
