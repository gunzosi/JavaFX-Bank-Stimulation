package com.jmc.mazebank.Models;

import com.jmc.mazebank.Views.AccountType;
import com.jmc.mazebank.Views.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class Model {
    private static Model model;
    private ViewFactory viewFactory;
    // Database Driver
    private final DatabaseDriver databaseDriver;
    private AccountType loginAccountType = AccountType.CLIENT;
    // Client Data Section
    private Client client;
    private boolean clientLoginSuccessFlag;
    private final ObservableList<Transaction> lastestTransactions;
    private final ObservableList<Transaction> allTransactions;
    // Admin Data Section
    private boolean adminLoginSuccessFlag;
    private final ObservableList<Client> clients;


    private Model() {
        this.viewFactory = new ViewFactory();
        this.databaseDriver = new DatabaseDriver();
        // Client Data Section
        this.clientLoginSuccessFlag = false;
        // Admin Data Section
        this.client = new Client("", "", "",
                null , null, null);
        this.adminLoginSuccessFlag = false;
        this.clients = FXCollections.observableArrayList();
        this.lastestTransactions = FXCollections.observableArrayList();
        this.allTransactions = FXCollections.observableArrayList();
    }

    // Singleton pattern - only one instance of Model can be created with the same OBJECT
    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public DatabaseDriver getDatabaseDriver() {
        return databaseDriver;
    }

    public AccountType getLoginAccountType() {
        return loginAccountType;
    }

    public void setLoginAccountType(AccountType loginAccountType) {
        this.loginAccountType = loginAccountType;
    }
    /*
    * Client section
    * */
    public boolean getClientLoginSuccessFlag() {
        return clientLoginSuccessFlag;
    }
    public void setClientLoginSuccessFlag(boolean flag) {
        this.clientLoginSuccessFlag = flag;
    }


    public Client getClient() {
        return client;
    }

    public void evaluateClientCred(String pAddress, String password) {
        CheckingAccount checkingAccount;
        SavingAccount savingAccount;
        ResultSet resultSet = databaseDriver.getClientData(pAddress, password);
        try {
            if (resultSet.isBeforeFirst()) {
                this.client.firstNameProperty().set(resultSet.getString("FirstName"));
                this.client.lastNameProperty().set(resultSet.getString("LastName"));
                this.client.payeeAddressProperty().set(resultSet.getString("PayeeAddress"));
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                this.client.dateCreatedProperty().set(date);
                checkingAccount = getCheckingAccount(pAddress);
                this.clientLoginSuccessFlag = true;
                /*checkingAccount = null;
                savingAccount = null;*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareTransactions(ObservableList<Transaction> transactions, int limit) {
        ResultSet resultSet = databaseDriver.getTransactions(client.payeeAddressProperty().get(), limit);
        try {
            String sender = resultSet.getString("Sender");
            String receiver = resultSet.getString("Receiver");
            double amount = resultSet.getDouble("Amount");
            String[] dateParts = resultSet.getString("Date").split("-");
            LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
            String message = resultSet.getString("Message");
            transactions.add(new Transaction(sender, receiver, amount, date, message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    public void setLatestTransactions() {
        prepareTransactions(this.lastestTransactions, 5);
    }

    public ObservableList<Transaction> getLatestTransactions() {
        return this.lastestTransactions;
    }

    public void setAllTransactions() {
        prepareTransactions(this.allTransactions, -1);
    }

    public ObservableList<Transaction> getAllTransactions() {
        return allTransactions;
    }

    /*
    * Admin Method Section
    * */

    public boolean getAdminLoginSuccessFlag() {return this.adminLoginSuccessFlag;}

    public void setAdminLoginSuccessFlag(boolean adminLoginSuccessFlag) {
        this.adminLoginSuccessFlag = adminLoginSuccessFlag;
    }

    public void evaluateAdminCred(String username, String password) {
        ResultSet resultSet = databaseDriver.getAdminData(username, password);
        try {
            if (resultSet.isBeforeFirst()) {
                this.adminLoginSuccessFlag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Client> getClients() {
        return clients;
    }

    public void setClients() {
        CheckingAccount checkingAccount;
        SavingAccount savingAccount;
        ResultSet resultSet = databaseDriver.getAllClientsData();

        try {
            while (resultSet.next()) {
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                String payeeAddress = resultSet.getString("PayeeAddress");
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(
                        Integer.parseInt(dateParts[0]),
                        Integer.parseInt(dateParts[1]),
                        Integer.parseInt(dateParts[2])
                );
                clients.add(new Client(firstName, lastName, payeeAddress, null, null, date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Client> searchClient(String pAddress) {
        ObservableList<Client> searchResults = FXCollections.observableArrayList();
        ResultSet resultSet = databaseDriver.searchClient(pAddress);
        try {
            while (resultSet.next()) {
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                String payeeAddress = resultSet.getString("PayeeAddress");
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(
                        Integer.parseInt(dateParts[0]),
                        Integer.parseInt(dateParts[1]),
                        Integer.parseInt(dateParts[2])
                );
                searchResults.add(new Client(firstName, lastName, payeeAddress, null, null, date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchResults;
    }


    /*
    * Utility Methods Section
    * */

    public CheckingAccount getCheckingAccount(String pAddress) {
        SavingAccount account = null;
        ResultSet resultSet = databaseDriver.getSavingsAccountData(pAddress);
        CheckingAccount checkingAccount = null;
        try {
            if (resultSet.isBeforeFirst()) {
                checkingAccount = new CheckingAccount(
                        resultSet.getString("Owner"),
                        resultSet.getString("AccountNumber"),
                        resultSet.getDouble("Balance"),
                        (int) resultSet.getDouble("TransactionLimit")

                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkingAccount;
    }
}
