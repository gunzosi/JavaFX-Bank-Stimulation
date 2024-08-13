package com.jmc.mazebank.Controllers.Client;

import com.jmc.mazebank.Models.Model;
import com.jmc.mazebank.Models.Transaction;
import com.jmc.mazebank.Views.TransactionCellFactory;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Text user_name;
    public Label login_date;
    public Label checking_bal;
    public Label saving_bal;
    public Label income_lbl;
    public Label expense_lbl;
    public Label checking_acc_num;
    public Label saving_acc_num;
    public ListView<Transaction> transaction_listview;
    public TextField payee_fld;
    public TextField amount_fld;
    public TextField message_fld;
    public Button send_money_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindData();
        initTransactionsLists();
        transaction_listview.setItems(Model.getInstance().getLatestTransactions());
        transaction_listview.setCellFactory(transactionListView -> new TransactionCellFactory());
        send_money_btn.setOnAction(actionEvent -> onSendMoney());
        accountSummary();
    }

    private void bindData() {
        user_name.textProperty().bind(Bindings
                .concat("Hi, ")
                .concat(Model
                        .getInstance()
                        .getClient()
                        .firstNameProperty()));
        login_date.setText("Today, " + LocalDate.now());
        checking_bal.textProperty().bind(Model
                .getInstance()
                .getClient()
                .checkingAccountProperty()
                .get()
                .balanceProperty()
                .asString());
        checking_acc_num.textProperty().bind(Model
                .getInstance()
                .getClient()
                .checkingAccountProperty()
                .get()
                .accountNumberProperty());
        saving_bal.textProperty().bind(Model
                .getInstance()
                .getClient()
                .savingAccountProperty()
                .get()
                .balanceProperty()
                .asString());
        saving_acc_num.textProperty().bind(Model
                .getInstance()
                .getClient()
                .savingAccountProperty()
                .get()
                .accountNumberProperty());
    }


    private void initTransactionsLists() {
         if (Model.getInstance().getLatestTransactions().isEmpty()) {
             Model.getInstance().setLatestTransactions();
         }
    }

    private void onSendMoney() {
        String receiver = payee_fld.getText();
        double amount = Double.parseDouble(amount_fld.getText());
        String message = message_fld.getText();
        String sender = Model.getInstance().getClient().payeeAddressProperty().get();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().searchClient(receiver);
        try {
            if (resultSet.isBeforeFirst()) {
                Model.getInstance().getDatabaseDriver().updateBalance(receiver, amount, "ADD");
            }
            // Update the savings-account balance in the client object
            Model.getInstance().getDatabaseDriver().updateBalance(sender, amount, "SUB");
            Model.getInstance()
                    .getClient()
                    .savingAccountProperty()
                    .get()
                    .setBalance(
                            Model.getInstance()
                                    .getDatabaseDriver()
                                    .getSavingsAccountBalance(sender));
            Model.getInstance().getDatabaseDriver().newTransaction(sender, receiver, amount, message);
            // Clear the fields use .clear() or set the text to "" .setText("")
            payee_fld.clear();
            amount_fld.clear();
            message_fld.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void accountSummary() {
        double income = 0;
        double expenses = 0;
        if (Model.getInstance().getAllTransactions().isEmpty()) {
            Model.getInstance().setAllTransactions();
        }

        for (Transaction transaction : Model.getInstance().getAllTransactions()) {
            if (transaction.senderProperty().get().equals(Model.getInstance().getClient().payeeAddressProperty().get())) {
                expenses += transaction.amountProperty().get();
            } else{
                income += transaction.amountProperty().get();
            }
        }
        income_lbl.setText("+ $" + income);
        expense_lbl.setText("- $" + expenses);
    }



}
