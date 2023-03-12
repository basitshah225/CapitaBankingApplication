package com.example.bankingapplication.Accounts;

import com.example.bankingapplication.Database.AccountHandler;
import com.example.bankingapplication.Database.ISAAccountHandler;


import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;


/**
 * ISA account that inherits methods from account
 */
public class ISAAccount extends Account {

    /**
     * The amount of money in this year
     */
    private BigDecimal moneyInThisYear;

    /**
     * The time since the apr was last updated
     */
    private Date timeSinceLastUpdate;

    /**
     * @return the money in this year
     */
    public BigDecimal getMoneyInThisYear() {
        return moneyInThisYear;
    }

    /**
     * @param moneyInThisYear Sets the money in this year
     */
    public void setMoneyInThisYear(BigDecimal moneyInThisYear) {
        this.moneyInThisYear = moneyInThisYear;
    }

    /**
     * @return Gets the time since last updated
     */
    public Date getTimeSinceLastUpdate() {
        return timeSinceLastUpdate;
    }


    /**
     * Constructor for ISA Account
     * @param balance The balance of the account
     * @param address The address of the customer
     * @param accountNumber The account number of the account
     * @param sortCode The sort code of the account
     * @param firstName The firstName of the customer
     * @param lastName The lastName of the customer
     * @param photoIDNumber The number of the ID
     * @param photoIDType The photo ID type that account registered with
     * @param moneyInThisYear The amount of money in this year
     * @param timeSinceLastUpdate The time since the account was last updated for apr
     */
    public ISAAccount(BigDecimal balance, String address, int accountNumber, int sortCode, String firstName, String lastName, String photoIDNumber, PhotoIDType photoIDType, BigDecimal moneyInThisYear, Date timeSinceLastUpdate) {
        super("ISAAccount", balance, address, accountNumber, sortCode, firstName, lastName, photoIDNumber, photoIDType);
        this.moneyInThisYear = moneyInThisYear;
        this.timeSinceLastUpdate = timeSinceLastUpdate;
    }



    /**
     * Performs the apr calculation on the account
     * @throws SQLException e
     */
    public void aprCalculation() throws SQLException {
        ISAAccountHandler IsaHandler = new ISAAccountHandler();
        IsaHandler.aprCalculation(2.75, 1, getAccountNumber(), getSortCode());
    }

    /**
     * test method for threads
     * @param amount The amount to add or decrease
     * @param add whether to add or subtract
     * @throws SQLException e
     */
    public void runThreadTest(int amount, boolean add) throws SQLException {
        AccountHandler ac = new AccountHandler();
        //ac.transferMoneyBetweenAccount(getAccountNumber(), getSortCode(), BigDecimal.valueOf(amount), 10012, 101010);
        if(add){
            ac.increaseAccountBalance(getAccountNumber(), getSortCode(), BigDecimal.valueOf(amount));
            System.out.println("Increasing with : " + amount);
        }else{
            ac.decreaseAccountBalance(getAccountNumber(), getSortCode(), BigDecimal.valueOf(amount));
            System.out.println("Decreasing with : " + amount);
        }
        setBalance(ac.getAccountBalance(getAccountNumber(), getSortCode()));
        System.out.println("New Balance : " + getBalance()) ;
    }

    public boolean confirmDeposit(int accountNumber, int sortCode,BigDecimal amount) throws SQLException {
        increaseMoneyInThisYear(accountNumber,sortCode,amount);
        return true;

    }


    /**
     * Increases the money in this year
     * @param accountNumber The number of the account
     * @param sortCode The sort code of the account
     * @param increaseISABy The amount to increase the ISA money in this ear by
     * @throws SQLException e
     */
    public void increaseMoneyInThisYear(int accountNumber, int sortCode,BigDecimal increaseISABy) throws SQLException {
        ISAAccountHandler account =  new ISAAccountHandler();
        AccountHandler isaAccount = new AccountHandler();
        int isaID=isaAccount.getISAAccountID(accountNumber,sortCode);
        boolean canIncrease = confirmTransaction(accountNumber,sortCode,increaseISABy);
        BigDecimal isabalance = account.getMoneyInthisYear(isaID);
        BigDecimal accountBalance = isaAccount.getAccountBalance(accountNumber,sortCode);
        if (canIncrease) {
            isabalance.add(increaseISABy);
            account.UpdateMoneyInThisYear(isaID,isabalance.add(increaseISABy));
            isaAccount.increaseAccountBalance(accountNumber,sortCode,increaseISABy);
            String message="Account has increased value";
        }else{
            String message = "Account cannot accept this deposit";
        }

    }



    }





