package com.jmc.mazebank.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class DatabaseDriver {
    private Connection conn;

    // DB - CONNECTION
    // jdbc:sqlite:D:\Project\JavaFX-Swing\mazebank.db

    /* Database Connection */
    public DatabaseDriver() {
        try {
            this.conn = DriverManager.getConnection("jdbc:sqlite:D:\\Project\\JavaFX-Swing\\mazebank.db");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /*
     * Client section
     * */

    public ResultSet getClientData(String pAddress, String password) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM client WHERE address = '" + pAddress + "' AND password = '" + password + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getTransactions(String pAddress, int limit) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.createStatement();
            resultSet = statement
                    .executeQuery("" +
                            "SELECT * FROM Transactions " +
                            "WHERE Sender = '" + pAddress + "' OR Receiver = '" + pAddress + "' " +
                            "ORDER BY Date " +
                            "DESC LIMIT " + limit);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public double getSavingsAccountBalance(String pAddress) {
        Statement statement;
        ResultSet resultSet = null;
        double balance = 0;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM SavingAccounts WHERE Owner = '" + pAddress + "'");
            balance = resultSet.getDouble("Balance");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }


/*    public void updateBalance(String pAddress, double amount, String operation) {
        Statement statement;
        ResultSet resultSet;

        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM CheckingAccounts WHERE Owner = '" + pAddress + "'");
            double newBalance = resultSet.getDouble("Balance") - amount;
            statement.executeUpdate("UPDATE CheckingAccounts SET Balance = " + newBalance + " WHERE Owner = '" + pAddress + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    public void updateBalance(String pAddress, double amount, String operation) {
        Statement statement;
        ResultSet resultSet;

        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM CheckingAccounts WHERE Owner = '" + pAddress + "'");
            double newBalance;
            if (operation.equals("ADD")) {
                newBalance = resultSet.getDouble("Balance") + amount;
                statement.executeUpdate("UPDATE CheckingAccounts SET Balance = " + newBalance + " WHERE Owner = '" + pAddress + "'");
            } else {
                if (resultSet.getDouble("Balance") >= amount) {
                    newBalance = resultSet.getDouble("Balance") - amount;
                    statement.executeUpdate("UPDATE CheckingAccounts SET Balance = " + newBalance + " WHERE Owner = '" + pAddress + "'");
                }
                throw new RuntimeException("Insufficient funds");
            }
//            statement.executeUpdate("UPDATE CheckingAccounts SET Balance = " + newBalance + " WHERE Owner = '" + pAddress + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create and records new transaction

    public void newTransaction(String sender, String receiver, double amount, String message) {
        Statement statement;
        try {
            statement = this.conn.createStatement();
            statement.executeUpdate("INSERT INTO " +
                    "Transactions (Sender, Receiver, Amount, Message, Date) " +
                    "VALUES ('" + sender + "', '" + receiver + "', '" + amount + "', '" + message + "', '" + LocalDate.now().toString() + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    Admin section
    * */

    public ResultSet getAdminData(String username, String password) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Admins WHERE username = '" + username + "' AND password = '" + password + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // Create CLIENT
    public void createClient(String fName, String lName, String pAddress, String password, LocalDate date) {
        Statement statement;
        try {
            statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO client (first_name, last_name, address, password, date) " +
                    "VALUES ('" + fName + "', '" + lName + "', '" + pAddress + "', '" + password + "', '" + date.toString() + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createCheckingAccount(String owner, String number, double tLimit, double balance) {
        Statement statement;
        try {
            statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO CheckingAccounts (Owner, AccountNumber, TransactionLimit, Balance) " +
                    "VALUES ('" + owner + "', '" + number + "', '" + tLimit + "', '" + balance + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createSavingAccount(String owner, String number, double wLimit, double balance) {
        Statement statement;
        try {
            statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO SavingAccounts (Owner, AccountNumber, WithdrawLimit, Balance) " +
                    "VALUES ('" + owner + "', '" + number + "', '" + wLimit + "', '" + balance + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    Utility Section
    * */

    public int getLastClientsId() {
        Statement statement;
        ResultSet resultSet = null;
        int id = 0;
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM sqlite_sequence WHERE name = 'Client'");
            while (resultSet.next()) {
                id = resultSet.getInt("seq");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }


    public ResultSet getAllClientsData() {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Client");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }


    public ResultSet searchClient(String pAddress) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Client WHERE PayeeAddress='" + "address" + "'");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void depositSavings(String pAddress, double amount) {
        Statement statement;
        try {
            statement = conn.createStatement();
            statement.executeUpdate("UPDATE SavingAccounts " +
                    "SET Balance = Balance + " + amount + " " +
                    "WHERE Owner = '" + pAddress + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    * Utility
    * */

    public ResultSet getCheckingAccountData(String pAddress) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM SavingAccounts WHERE Owner = '" + pAddress + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getSavingsAccountData(String pAddress) {
        Statement statement;
        ResultSet resultSet = null;

        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM SavingAccounts WHERE Owner = '" + pAddress + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }






}
