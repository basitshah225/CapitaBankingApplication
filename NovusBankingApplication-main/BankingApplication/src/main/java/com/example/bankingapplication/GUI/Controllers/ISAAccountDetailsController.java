package com.example.bankingapplication.GUI.Controllers;

import com.example.bankingapplication.Accounts.Account;
import com.example.bankingapplication.Accounts.ISAAccount;
import com.example.bankingapplication.Accounts.PersonalAccount;
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
 * Controller for displaying ISA account details
 */
public class ISAAccountDetailsController {

    /**
     * The account to show the details of
     */
    private Account account;

    /**
     * The money that has been put into the account this year text field
     */
    @FXML
    protected TextField moneyInAtStartTextField;

    /**
     * The photo id number text field
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
     * The last name text field
     */
    @FXML
    protected TextField lastNameTextField;

    /**
     * The first name text field
     */
    @FXML
    protected TextField firstNameTextField;

    /**
     * The photo id choice box
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

    /**
     * The helper class which can be used for the help buttons
     */
    private final Helper helper = new Helper();
    /**
     * Sets the scene to account received options on going back
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
     * Sets the ISA account details using the account
     * @throws SQLException e
     */
    public void setAccountDetails() throws SQLException {
        balanceTextField.setText(String.valueOf(account.getBalance()));
        firstNameTextField.setText(account.getFirstName());
        lastNameTextField.setText(account.getLastName());
        addressTextField.setText(account.getAddress());
        photoIDNumberTextField.setText(account.getPhotoIDNumber());
        photoIDTypeChoiceBox.setValue(String.valueOf(account.getPhotoIDType()));
        if(account.getAccountType().equals("ISAAccount")) {
            ISAAccount isaAccount = (ISAAccount) account;
            moneyInAtStartTextField.setText(String.valueOf(isaAccount.getMoneyInThisYear()));
        }
    }

}
