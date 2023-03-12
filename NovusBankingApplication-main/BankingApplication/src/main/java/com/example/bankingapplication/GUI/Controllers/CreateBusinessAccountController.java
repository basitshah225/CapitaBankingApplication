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
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller for creating a business account
 */
public class CreateBusinessAccountController implements Initializable {


    /**
     * Create account button
     */
    @FXML
    protected Button createAccountButton;

    /**
     * The business ID text field
     */
    @FXML
    protected TextField businessIDTextField;

    /**
     * The photo ID number text field
     */
    @FXML
    protected TextField photoIDNumberTextField;

    /**
     * The balance text field
     */
    @FXML
    protected TextField balanceTextField;

    /**
     * The address  text field
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
     * The loan amount text field
     */
    @FXML
    protected TextField loanAmountTextField;

    /**
     * The over draft amount text field
     */
    @FXML
    protected TextField overDraftAmountTextField;

    /**
     * The photo ID type choice check box
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
     * HELPER
     * creating a helper button on Create Business Account View to guide user on available actions
     */
    public void needHelp() {
        helper.needHelp(button_need_help, """
                To create a new Business account please enter information into all fields.
                - FIRST NAME: Enter first name and Middle name (if applicable) to the 'First Name' field.
                - LAST NAME: Enter all last names into the 'Last Name' field.
                - POSTCODE: Enter a valid UK postcode including a space were applicable. All characters must be capitals.
                - INITIAL BALANCE: Enter an initial balance to deposit into the account.
                - PHOTO ID TYPE: Select one of the Photo ID types from the drop down menu.
                - PHOTO ID NUMBER: Enter the 10 digit passport No or first 10 digits of
                  your drivers licence depending on what you selected for your Photo ID Type. 
                  previously.
                - BUSINESS ID: Enter your government registered 9 digit business ID.
                - LOAN AMOUNT: Enter a loan amount between £0 - £10million.
                - OVERDRAFT AMOUNT: Enter an overdraft amount between £0 - £5000. 
                - When all the fields are entered click 'Create Account'.
                
                To return to the previous screen click the 'Cancel' button.
                """);
    }

    /**
     * Sets the scene to the create account options view
     * @throws IOException e
     */
    public void back() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        sceneSwitcher.switchScene(stage, "FXMLFiles/CreateAccountOptionsView.fxml", "");
        helper.closePopUp();
    }

    /**
     * Calls the initialise method to allow for setting up the photo check box
     * @param location l
     * @param resources r
     */
    @Override @FXML
    public void initialize(URL location, ResourceBundle resources) {
        photoIDTypeChoiceBox.getItems().addAll("Drivers License", "Passport");
        photoIDTypeChoiceBox.getSelectionModel().select("Drivers License");
    }

    /**
     * Clears all the fields
     */
    public void clearFields(){
        firstNameTextField.setText("");
        lastNameTextField.setText("");
        addressTextField.setText("");
        balanceTextField.setText("");
        businessIDTextField.setText("");
        photoIDNumberTextField.setText("");
        loanAmountTextField.setText("");
        overDraftAmountTextField.setText("");
    }

    /**
     * Creates an account once the create account button is clicked
     * Verifies all the details before attempting to create the account
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

        if(!verifier.isANumberInRange(balanceTextField.getText(), 0, 999999999999999.99)){
            shouldCreateAccount = false;

        }else{
            balance = new BigDecimal(balanceTextField.getText());
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

        int businessID = -1;

        if(!verifier.verifyBusinessID(businessIDTextField.getText())){
            shouldCreateAccount = false;
        }
        else{
            businessID = Integer.parseInt(businessIDTextField.getText());
        }

        BigDecimal overDraftAmount = null;

        if(!verifier.verifyBusinessOverdraft(overDraftAmountTextField.getText())){
            shouldCreateAccount = false;

        }else{
            overDraftAmount = new BigDecimal(overDraftAmountTextField.getText());
        }

        BigDecimal loanAmount = null;

        if(!verifier.verifyBusinessLoanAmount(loanAmountTextField.getText())){
            shouldCreateAccount = false;

        }else{
            loanAmount = new BigDecimal(balanceTextField.getText());
        }

        if(shouldCreateAccount){
            AccountManager accountManager = new AccountManager();
            AccountIdentifier bAccountIdentifier = accountManager.createBusinessAccount(firstName, lastName, address, balance, id, photoIDNumber, businessID, overDraftAmount, loanAmount);
            if(bAccountIdentifier.accountNumber() == -1 && bAccountIdentifier.sortCode() == -1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Could not create account, check the details! Person may already have an Business Account with this Business ID");
                alert.show();
            }else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                String text = "| Account Number = " + bAccountIdentifier.accountNumber() + " , Sort Code = " + bAccountIdentifier.sortCode() + " |";
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
