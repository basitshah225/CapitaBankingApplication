package com.example.bankingapplication.Helper;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class Helper {

    /**
     * The popup to be displayed
     */
    private final Popup popup;

    /**
     * The state of the popup
     */
    private int state = 0;

    public Helper(){
        popup = new Popup();
    }

    /**
     * Closes the popup
     */
    public void closePopUp(){
        popup.hide();
    }

    /**
     * Need help method called to display a popup
     * @param button_need_help The button it's attached to
     * @param text The text in the popup
     */
    public void needHelp(Button button_need_help, String text) {

        Stage stage = (Stage) button_need_help.getScene().getWindow();
        Label pLabel = new Label();
        pLabel.setText(text);
        pLabel.setMinWidth(280);
        pLabel.setMinHeight(70);
        pLabel.setStyle("-fx-background-color: #91D9D2; -fx-font-size: 13;-fx-translate-x: 300; -fx-translate-y: -200; -fx-font-family: Calibri; -fx-alignment: center; -fx-border-radius: 5px; " );
        popup.getContent().add(pLabel);

        if(state == 0){
            popup.show(stage);
            button_need_help.setText("Close Help");
            state = 1;
        }else if(state == 1){
            popup.hide();
            button_need_help.setText("Need help?");
            state = 0;
        }

    }
}
