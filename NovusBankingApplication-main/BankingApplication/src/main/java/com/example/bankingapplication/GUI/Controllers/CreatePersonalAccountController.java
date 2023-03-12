package com.example.bankingapplication.GUI.Controllers;

import com.example.bankingapplication.Accounts.Account;
import com.example.bankingapplication.Accounts.AccountManager;
import com.example.bankingapplication.Accounts.PhotoIDType;
import com.example.bankingapplication.Accounts.Records.AccountIdentifier;
import com.example.bankingapplication.GUI.SceneSwitcher;
import com.example.bankingapplication.Helper.Helper;
import com.example.bankingapplication.Security.InputVerifier;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller for creating a personal account
 */
public class CreatePersonalAccountController implements Initializable {

    /**
     * The back button
     */
    @FXML
    protected Button backButton;

    /**
     * The create account button
     */
    @FXML
    protected Button createAccountButton;

    /**
     * The over draft amount text field
     */
    @FXML
    protected TextField overDraftAmountTextField;

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
     * The photoID check box
     */
    @FXML
    protected ChoiceBox<String> photoIDTypeChoiceBox;

    @FXML
    protected Button button_need_help;

    /**
     * The helper class which can be used for the help buttons
     */
    private final Helper helper = new Helper();

    /**
     * HELPER
     * creating a helper button on Create Personal Account View to guide user on available actions
     */
    public void needHelp() {
        helper.needHelp(button_need_help, """
                To create a new Personal account please enter information into all fields.
                - FIRST NAME: Enter first name and Middle name (if applicable) to the 'First Name' field.
                - LAST NAME: Enter all last names into the 'Last Name' field.
                - POSTCODE: Enter a valid UK postcode including a space were applicable. All characters must be capital letters.
                - INITIAL BALANCE: Enter an initial balance to deposit into the account between £0 - £20,000.
                - PHOTO ID TYPE: Select one of the Photo ID types from the drop down menu.
                - PHOTO ID NUMBER: Enter the 10 digit passport No or first 10 digits of
                  your drivers licence depending on what you selected for your Photo ID Type. 
                  previously.
                - OVERDRAFT AMOUNT: Enter an amount between £0 - £2.5k.
                - To submit your details click 'Create Account' when all fields are complete.
                
                To return to the previous screen click the 'Cancel' button.
                """);
    }

    /**
     * Initialise is called to set up the checkbox
     * @param location l
     * @param resources r
     */
    @Override @FXML
    public void initialize(URL location, ResourceBundle resources) {
        photoIDTypeChoiceBox.getItems().addAll("Drivers License", "Passport");
        photoIDTypeChoiceBox.getSelectionModel().select("Drivers License");
    }

    /**
     * Switches the scene back to the create account options view
     * @throws IOException e
     */
    public void back() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        sceneSwitcher.switchScene(stage, "FXMLFiles/CreateAccountOptionsView.fxml", "");
        helper.closePopUp();
    }

    /**
     * Clears all the fields
     */
    public void clearFields(){
        firstNameTextField.setText("");
        lastNameTextField.setText("");
        addressTextField.setText("");
        balanceTextField.setText("");
        overDraftAmountTextField.setText("");
        photoIDNumberTextField.setText("");
    }

    /**
     * Creates an account using text field info
     * Verifies the info before being to create an account
     * @throws SQLException e
     */
    public void createAccount() throws SQLException {
        InputVerifier verifier = new InputVerifier();
        boolean shouldCreateAccount = true;

        String firstName = firstNameTextField.getText();

        if(!verifier.verifyName(firstName)){
            shouldCreateAccount = false;
        }

        String lastName = lastNameTextField.getText();

        if(!verifier.verifyName(lastName)){
            shouldCreateAccount = false;
        }

        String address = addressTextField.getText();

        if(!verifier.verifyAddress(address)){
            shouldCreateAccount = false;
        }

        BigDecimal balance = null;

        if(!verifier.isANumberInRange(balanceTextField.getText(), 1, 999999999999999.99)){
            shouldCreateAccount = false;

        }else{
            balance = new BigDecimal(balanceTextField.getText());
        }

        BigDecimal overDraftAmount = BigDecimal.valueOf(0);

        if(!overDraftAmountTextField.getText().isBlank()){
            if(verifier.verifyPersonalOverdraft(overDraftAmountTextField.getText())){
                overDraftAmount = BigDecimal.valueOf(Double.parseDouble(overDraftAmountTextField.getText()));
            }
        }

        String photoIDType = photoIDTypeChoiceBox.getSelectionModel().getSelectedItem();

        PhotoIDType id = PhotoIDType.passport;
        if (photoIDType.equals("driversLicense")) {
            id = PhotoIDType.driversLicense;
        }

        String photoIDNumber = photoIDNumberTextField.getText();

        if(!verifier.verifyPhotoIDNumber(photoIDNumber, id)){
            shouldCreateAccount = false;
        }

        if(shouldCreateAccount){
            AccountManager accountManager = new AccountManager();
            AccountIdentifier pAccountIdentifier = accountManager.createPersonalAccount(firstName, lastName, address, balance, id, photoIDNumber, overDraftAmount);
            if(pAccountIdentifier.accountNumber() == -1 && pAccountIdentifier.sortCode() == -1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Could not create account, check the details!");
                alert.show();
            }else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                String text = "| Account Number = " + pAccountIdentifier.accountNumber() + " , Sort Code = " + pAccountIdentifier.sortCode() + " |";
                alert.setContentText(text);
                alert.show();
                clearFields();
            }

        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please make sure all the details are correct and in the right format");
            alert.show();
        }



    }


}
