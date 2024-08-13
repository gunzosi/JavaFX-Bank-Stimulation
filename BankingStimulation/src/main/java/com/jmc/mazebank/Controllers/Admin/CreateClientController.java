package com.jmc.mazebank.Controllers.Admin;

import com.jmc.mazebank.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CreateClientController implements Initializable {
    public TextField fName_fld;
    public TextField lName_fld;
    public TextField password_fld;
    public CheckBox pAddress_box;
    public Label pAddress_lbl;
    public CheckBox ch_acc_box;
    public TextField ch_amount_fld;
    public CheckBox sv_acc_box;
    public TextField sv_amount_fld;
    public Button create_client_btn;
    public Text error_lbl;

    private String payeeAddress;
    private boolean createCheckingAccountFlag = false;
    private boolean createSavingAccountFlag = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        create_client_btn.setOnAction(event -> createClient());
        pAddress_box.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                payeeAddress = createPayeeAddress();
                onCreatePayeeAddress();
            } else {
                payeeAddress = "";
                pAddress_lbl.setText("");
            }
        });
        ch_acc_box.selectedProperty().addListener((observable, oldValue, newValue) -> {
            createCheckingAccountFlag = newValue;
        });
        sv_acc_box.selectedProperty().addListener((observable, oldValue, newValue) -> {
            createSavingAccountFlag = newValue;
        });
    }

    private void createClient() {
        // Create a checking account
        if (createCheckingAccountFlag) {
            createAccount("Checking");
        }

        // Create a saving account
        if (createSavingAccountFlag) {
            createAccount("Saving");
        }

        String fName = fName_fld.getText();
        String lName = lName_fld.getText();
        String password = password_fld.getText();
        Model.getInstance().getDatabaseDriver().createClient(fName, lName, payeeAddress, password, LocalDate.now());
        error_lbl.setStyle("-fx-text-fill: blue; -fx-font-size: 1.3em; -fx-font-weight: bold");
        error_lbl.setText("Client created successfully");
    }

    private void createAccount(String accountType) {
        double balance = Double.parseDouble(ch_amount_fld.getText());
        String firstSection = "3201";
        String lastSection = String.valueOf((int) (Math.random() * 1000000));
        String accountNumber = firstSection + " " + lastSection;
        if (accountType.equals("Checking")) {
            Model.getInstance().getDatabaseDriver().createCheckingAccount(payeeAddress, accountNumber, 18, balance);
        } else {
            Model.getInstance().getDatabaseDriver().createSavingAccount(payeeAddress, accountNumber, 2000, balance);
        }

    }

    private void onCreatePayeeAddress() {
        if (fName_fld.getText() != null & lName_fld.getText() != null) {
            pAddress_lbl.setText(payeeAddress);
        }
    }

    private void emptyFields() {
        fName_fld.setText("");
        lName_fld.setText("");
        password_fld.setText("");
        pAddress_box.setSelected(false);
        pAddress_lbl.setText("");
        ch_acc_box.setSelected(false);
        ch_amount_fld.setText("");
        sv_acc_box.setSelected(false);
        sv_amount_fld.setText("");
    }

    private String createPayeeAddress() {
        int id = Model.getInstance().getDatabaseDriver().getLastClientsId() + 1;
        char fChar = Character.toLowerCase(fName_fld.getText().charAt(0));
        return "@" + fChar + lName_fld.getText() + id;
    }
}
