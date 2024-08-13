package com.jmc.mazebank.Controllers.Client;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AccountController implements Initializable {
    public Label ch_acc_num;
    public Label transaction_limit;
    public Label ch_acc_date;
    public Label ch_acc_balance;
    public Label sv_acc_num;
    public Label withdraw_limit;
    public Label sv_acc_date;
    public Label sv_acc_balance;
    public TextField amount_to_sv;
    public Button transfer_to_sv_btn;
    public TextField amount_to_c;
    public Button transfer_to_c_btn;

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {

    }
}
