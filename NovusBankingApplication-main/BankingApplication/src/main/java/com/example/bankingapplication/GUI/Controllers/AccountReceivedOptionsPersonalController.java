package com.example.bankingapplication.GUI.Controllers;

import com.example.bankingapplication.Accounts.Account;
import com.example.bankingapplication.GUI.SceneSwitcher;
import com.example.bankingapplication.Helper.Helper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for when the account has been received
 */
public class AccountReceivedOptionsPersonalController {
    /**
     * The account received
     */
    private Account account = null;

    /**
     * The deposit into account button
     */
    @FXML
    protected Button depositIntoAccountButton;

    /**
     * The withdraw from account button
     */
    @FXML
    protected Button withdrawFromAccountButton;

    /**
     * The send money to other account button
     */
    @FXML
    protected Button sendMoneyToAnotherAccountButton;

    /**
     * The view account details button
     */
    @FXML
    protected Button viewAccountDetailsButton;

    /**
     * The change customer details button
     */
    @FXML
    protected Button changeCustomerDetailsButton;

    /**
     * The change over draft button
     */
    @FXML
    protected Button changeOverDraftButton;

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
     * creating a helper button on Account Received Options Personal View to guide user on the 8 options available
     */
    public void needHelp() {
        helper.needHelp(button_need_help, """
                - To send money to an other account click 'Send Money to Another Account'.
                - To change the customer account details on the Personal Account click 'Change Customer Details'.
                - To deposit funds into the Personal Account click 'Deposit Into Account'. 
                - To view the Account details of the Personal Account click 'View Account Details'.
                - To change the overdraft limit on the Personal Account click 'Change OverDraft'.
                - To withdraw funds from the Personal Account click 'Withdraw from Account'.
                """);
    }

    /**
     * Loads the previous accounts view scene
     * @throws IOException e
     */
    public void back() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        sceneSwitcher.switchScene(stage, "FXMLFiles/AccountsView.fxml", "");
        helper.closePopUp();
    }

    /**
     * @param account Sets the account
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * Switches the scene to let the teller send money to another account
     * @throws IOException e
     */
    public void sendMoneyToAnotherAccount() throws IOException {
        Stage stage = (Stage) sendMoneyToAnotherAccountButton.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        sendMoneyToOtherAccountController controller = (sendMoneyToOtherAccountController) sceneSwitcher.switchScene(stage, "FXMLFiles/sendMoneyToOtherAccountView.fxml", "");
        controller.setAccount(account);
    }

    /**
     * Switches the scene to let the teller view the accounts details
     * @throws IOException e
     */
    public void viewAccountDetails() throws IOException, SQLException {
        Stage stage = (Stage) sendMoneyToAnotherAccountButton.getScene().getWindow();
        helper.closePopUp();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        switch (account.getAccountType()) {
            case "PersonalAccount" -> {
                PersonalAccountDetailsController controller = (PersonalAccountDetailsController) sceneSwitcher.switchScene(stage, "FXMLFiles/AccountsDetailsViews/PersonalAccountDetailsView.fxml", "");
                controller.setAccount(account);
                controller.setAccountDetails();
                break;
            }
            case "ISAAccount" -> {
                ISAAccountDetailsController controller = (ISAAccountDetailsController) sceneSwitcher.switchScene(stage, "FXMLFiles/AccountsDetailsViews/ISAAccountDetailsView.fxml", "");
                controller.setAccount(account);
                controller.setAccountDetails();
                break;
            }
            case "BusinessAccount" -> {
                BusinessAccountDetailsController controller = (BusinessAccountDetailsController) sceneSwitcher.switchScene(stage, "FXMLFiles/AccountsDetailsViews/BusinessAccountDetailsView.fxml", "");
                controller.setAccount(account);
                controller.setAccountDetails();
                break;
            }
        }
    }

    /**
     * Switches the scene to let the teller change customer details
     * @throws IOException e
     */
    public void changeCustomerDetails() throws IOException {
        Stage stage = (Stage) sendMoneyToAnotherAccountButton.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        helper.closePopUp();
        ChangeCustomerDetailsController controller = (ChangeCustomerDetailsController) sceneSwitcher.switchScene(stage, "FXMLFiles/ChangeCustomerDetailsView.fxml", "");
        controller.setAccount(account);
    }

    /**
     * Switches the scene to let the teller change an overdraft of personal or business
     * @throws IOException e
     */
    public void changeOverDraft() throws IOException {
        Stage stage = (Stage) sendMoneyToAnotherAccountButton.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        helper.closePopUp();
        if(account.getAccountType().equals("PersonalAccount") || account.getAccountType().equals("BusinessAccount")){
            ChangeOverDraftController controller = (ChangeOverDraftController) sceneSwitcher.switchScene(stage, "FXMLFiles/ChangeOverDraftView.fxml", "");
            controller.setAccount(account);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Account is not a personal or a business account!");
            alert.show();
        }
    }

    /**
     * Switches the scene to let the teller withdraw money from a customers account
     * @throws IOException e
     */
    public void withdrawMoney() throws IOException {
        Stage stage = (Stage) sendMoneyToAnotherAccountButton.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        helper.closePopUp();
        WithdrawMoneyController controller = (WithdrawMoneyController) sceneSwitcher.switchScene(stage, "FXMLFiles/WithdrawMoneyView.fxml", "");
        controller.setAccount(account);
    }

    /**
     * Switches the scene to let the teller deposit money into an account
     * @throws IOException e
     */
    public void depositMoney() throws IOException {
        Stage stage = (Stage) sendMoneyToAnotherAccountButton.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        helper.closePopUp();
        DepositMoneyController controller = (DepositMoneyController) sceneSwitcher.switchScene(stage, "FXMLFiles/DepositMoneyView.fxml", "");
        controller.setAccount(account);
    }
}
