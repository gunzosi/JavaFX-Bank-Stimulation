package com.jmc.mazebank.Controllers;

import com.jmc.mazebank.Models.Model;
import com.jmc.mazebank.Views.AccountType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    public ChoiceBox acc_selector;
    @FXML
    public Label payee_address;
    @FXML
    public TextField payee_address_fld;
    @FXML
    public TextField password_fld;
    @FXML
    public Button login_btn;
    @FXML
    public Label error_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.CLIENT, AccountType.ADMIN));
        acc_selector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        acc_selector.valueProperty().addListener(observable -> setAcc_selector());
        login_btn.setOnAction(e -> onLogin());
    }

    private void onLogin() {
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        // Model.getInstance().getViewFactory().closeStage(stage);
        // Model.getInstance().getViewFactory().showClientWindow();
        if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.ADMIN) {
            // Model.getInstance().getViewFactory().showAdminWindow();
            // Evaluate Admin Login Credentials
            Model.getInstance().evaluateAdminCred(payee_address_fld.getText(), password_fld.getText());
            if (Model.getInstance().getAdminLoginSuccessFlag()) {
                Model.getInstance().getViewFactory().showAdminWindow();
            } else {
                payee_address_fld.setText("");
                password_fld.setText("");
                error_lbl.setText("Invalid Credentials");
            }
        }
        /*else {
            // Model.getInstance().getViewFactory().showClientWindow();
            // Evaluate Client Login Credentials
            Model.getInstance().evaluateClientCred(payee_address_fld.getText(), password_fld.getText());
            if (Model.getInstance().getClientLoginSuccessFlag()) {
                Model.getInstance().getViewFactory().showClientWindow();
            } else {
                payee_address_fld.setText("");
                password_fld.setText("");
                error_lbl.setText("Invalid Credentials");
            };
        }*/
        if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT) {
            // Model.getInstance().getViewFactory().showClientWindow();
            // Evaluate Client Login Credentials
            Model.getInstance().evaluateClientCred(payee_address_fld.getText(), password_fld.getText());
            if (Model.getInstance().getClientLoginSuccessFlag()) {
                Model.getInstance().getViewFactory().showClientWindow();
            } else {
                payee_address_fld.setText("");
                password_fld.setText("");
                error_lbl.setText("Invalid Credentials");
            };
        }
    }

    private void setAcc_selector() {
        Model.getInstance().getViewFactory().setLoginAccountType((AccountType) acc_selector.getValue());
        // Change PayeeAddress Label
        if(acc_selector.getValue() == AccountType.ADMIN) {
            payee_address.setText("Username");
        } else {
            payee_address.setText("Payee Address");
        }
    }


}
