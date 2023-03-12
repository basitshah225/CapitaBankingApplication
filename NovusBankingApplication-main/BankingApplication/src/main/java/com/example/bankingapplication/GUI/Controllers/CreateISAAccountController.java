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
 * Controller for creating a ISA account
 */
public class CreateISAAccountController implements Initializable {


    /**
     * The button to create the account
     */
    @FXML
    protected Button createAccountButton;

    /**
     * The photo ID number text field
     */
    @FXML
    protected TextField photoIDNumberTextField;

    /**
     * The money put into the ISA account
     */
    @FXML
    protected TextField moneyInAtStartTextField;

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
     * HELPER
     * creating a helper button on Create ISA Account View to guide user on available actions
     */
    public void needHelp() {
        helper.needHelp(button_need_help, """
                To create a new ISA account please enter information into all fields.
                - FIRST NAME: Enter first name and Middle name (if applicable) to the 'First Name' field.
                - LAST NAME: Enter all last names into the 'Last Name' field.
                - POSTCODE: Enter a valid UK postcode including a space were applicable. All characters must be capital letters.
                - INITIAL BALANCE: Enter an initial balance to deposit into the account between £0 - £20,000.
                - PHOTO ID TYPE: Select one of the Photo ID types from the drop down menu.
                - PHOTO ID NUMBER: Enter the 10 digit passport No or first 10 digits of
                  your drivers licence depending on what you selected for your Photo ID Type. 
                  previously.
                - To submit your details click 'Create Account' when all fields are complete.
                
                To return to the previous screen click the 'Cancel' button.
                """);
    }



    /**
     * Switches the scene back to create options view
     * @throws IOException e
     */
    public void back() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher();
        sceneSwitcher.switchScene(stage, "FXMLFiles/CreateAccountOptionsView.fxml", "");
        helper.closePopUp();
    }

    /**
     * Calls initialise to set up the choice box options
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
        moneyInAtStartTextField.setText("");
        photoIDNumberTextField.setText("");
        moneyInAtStartTextField.setText("");
    }

    /**
     * Creates an account once the button is clicked
     * Verifies all inputs before creating the account
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

        BigDecimal moneyInAtStart = null;

        if(!verifier.isANumberInRange(moneyInAtStartTextField.getText(), 0, 20000)){
            shouldCreateAccount = false;

        }else{
            moneyInAtStart = new BigDecimal(moneyInAtStartTextField.getText());
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
            AccountIdentifier iAccountIdentifier = accountManager.createISAAccount(firstName, lastName, address, moneyInAtStart, id, photoIDNumber);
            if(iAccountIdentifier.accountNumber() == -1 && iAccountIdentifier.sortCode() == -1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Could not create account, check the details! Person may already have an ISA Account");
                alert.show();
            }else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                String text = "| Account Number = " + iAccountIdentifier.accountNumber() + " , Sort Code = " + iAccountIdentifier.sortCode() + " |";
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
