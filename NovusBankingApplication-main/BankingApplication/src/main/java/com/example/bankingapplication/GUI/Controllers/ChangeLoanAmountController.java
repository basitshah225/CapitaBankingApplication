package com.example.bankingapplication.GUI.Controllers;

import com.example.bankingapplication.Accounts.Account;
import com.example.bankingapplication.Accounts.BusinessAccount;
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
 * Controller for changing the loan amount for businesses
 */
public class ChangeLoanAmountController {

    /**
     * The account to change loan on
     */
    private Account account;

    /**
     * The change loan button
     */
    @FXML
    protected Button changeLoanButton;

    /**
     * The new loan amount text field
     */
    @FXML
    protected TextField newLoanAmountTextField;

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
     * creating a helper button on Change Loan Amount View to guide user on available actions
     */
    public void needHelp() {
        helper.needHelp(button_need_help, """
                To set a new business loan amount enter the amount into the 'New Business Loan Amount' field.
                It must be between £0 - £10million.
                To submit this change click the 'Change Loan' button.
                
                To go back to the previous screen click the 'Cancel' button.
                """);
    }

    /**
     * Switches the scene to the account options controller
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
     * Changes the loan amount if the input is verified
     * Has to be a business account, sets the loan amount
     */
    public void changeLoanAmount() throws IOException, SQLException {

        InputVerifier verifier = new InputVerifier();
        boolean update = true;

        BigDecimal newLoanAmount = null;
        if(!verifier.verifyBusinessLoanAmount(newLoanAmountTextField.getText())){
            update = false;
        }else{
            newLoanAmount = new BigDecimal(newLoanAmountTextField.getText());
        }

        if(update){
            if (account.getAccountType().equals("BusinessAccount")) {
                BusinessAccount acc = (BusinessAccount) account;
                acc.setLoanAmount(newLoanAmount);
                back();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Account is not a personal or business account!");
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
