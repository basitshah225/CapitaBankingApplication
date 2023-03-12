package com.example.bankingapplication.GUI.Controllers;

import com.example.bankingapplication.Accounts.Account;
import com.example.bankingapplication.Accounts.BusinessAccount;
import com.example.bankingapplication.Accounts.PersonalAccount;
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
 * Controller for changing the over draft for personal and business accounts
 */
public class ChangeOverDraftController {

    /**
     * The account to change over draft on
     */
    private Account account;

    /**
     * The change over draft button
     */
    @FXML
    protected Button changeOverdraftButton;

    /**
     * The new over draft text field
     */
    @FXML
    protected TextField newOverdraftAmountTextField;

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
     * creating a helper button on Change Overdraft View to guide user on available actions
     */
    public void needHelp() {
        helper.needHelp(button_need_help, """
                To set a new overdraft amount enter the amount into the 'New Business Overdraft Amount' field.
                It much be between £0 - £5000.
                To submit this change click the 'Change Overdraft' button.
                
                To go back to the previous screen click the 'Cancel' button.
                """);
    }

    /**
     * Goes back to the accounts options scene
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
     * Changes the over draft if the over draft is verified
     * Over draft verification changes depending on account type
     */
    public void changeOverdraft() throws IOException, SQLException {

        InputVerifier verifier = new InputVerifier();
        boolean update = true;

        BigDecimal newOverDraft = null;
        if(account.getAccountType().equals("PersonalAccount")){
            if(!verifier.verifyPersonalOverdraft(newOverdraftAmountTextField.getText())){
                update = false;
            }else{
                newOverDraft = new BigDecimal(newOverdraftAmountTextField.getText());
            }
        }else if (account.getAccountType().equals("BusinessAccount")) {
            if(!verifier.verifyBusinessOverdraft(newOverdraftAmountTextField.getText())){
                update = false;
            }else{
                newOverDraft = new BigDecimal(newOverdraftAmountTextField.getText());
            }
        }

        if(update){
            if(account.getAccountType().equals("PersonalAccount")){
                PersonalAccount acc = (PersonalAccount) account;
                acc.setOverDraftAmount(newOverDraft);
                back();

            } else if (account.getAccountType().equals("BusinessAccount")) {
                BusinessAccount acc = (BusinessAccount) account;
                acc.setOverDraftAmount(newOverDraft);
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
