package com.example.bankingapplication.Accounts;

import com.example.bankingapplication.Database.AccountHandler;
import com.example.bankingapplication.Database.BusinessAccountHandler;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * A business account that extends account
 */
public class BusinessAccount extends Account{

    /**
     * The integer representing a business ID
     */
    private int businessID;

    /**
     * A boolean for if a cheque book has been issued
     */
    private boolean chequeBookIssued;

    /**
     * The overdraft amount the business has
     */
    private BigDecimal overDraftAmount;

    /**
     * The loan amount the business has
     */
    private BigDecimal loanAmount;

    /**
     * @return Is the cheque book issued
     */
    public boolean isChequeBookIssued() {
        return chequeBookIssued;
    }

    /**
     * @return The over draft of the account
     */
    public BigDecimal getOverDraftAmount() {
        return overDraftAmount;
    }

    /**
     * @param loanAmount Sets the loan amount
     */
    public void setLoanAmount(BigDecimal loanAmount) throws SQLException {
        setNewLoanAmount(getAccountNumber(), getSortCode(), loanAmount);
        this.loanAmount = loanAmount;
    }

    /**
     * @return The loan amount
     */
    public BigDecimal getLoanAmount() {
        return loanAmount;
    }


    /**
     * Constructor for Business Account
     * @param businessID The id of the business
     * @param overDraftAmount The overdraft amount
     * @param balance The balance of the account
     * @param address The address of the customer
     * @param accountNumber The account number of the account
     * @param sortCode The sort code of the account
     * @param firstName The firstName of the customer
     * @param lastName The lastName of the customer
     * @param photoIDNumber The number of the ID
     * @param photoIDType The photo ID type that account registered with
     * @param loanAmount The amount to loan the business
     */
    public BusinessAccount(int businessID, BigDecimal overDraftAmount, BigDecimal balance, String address, int accountNumber, int sortCode, String firstName, String lastName, String photoIDNumber, PhotoIDType photoIDType, BigDecimal loanAmount) {
        super("BusinessAccount", balance, address, accountNumber, sortCode, firstName, lastName, photoIDNumber, photoIDType);
        this.businessID = businessID;
        this.overDraftAmount = overDraftAmount;
        this.loanAmount = loanAmount;
    }

    /**
     * @return The business ID
     */
    public int getBusinessID(){
        return businessID;
    }

    /**
     * @return The over draft amount
     */
    public BigDecimal overDraftAmount(){
        return overDraftAmount;
    }

    /**
     * @param newAmount The new over draft
     */
    public void setOverDraftAmount(BigDecimal newAmount) throws SQLException {
        setOverdraft(getAccountNumber(), getSortCode(), newAmount);
        overDraftAmount = newAmount;
    }

    /**
     * Issue the business a debit card
     */
    public void issueDebitCard(){
        System.out.println("We have issued you a new debit card!");
    }

    /**
     * Issue the business a credit card
     */
    public void issueCreditCard(){
        System.out.println("We have issued you a new credit card!");
    }

    /**
     * International trade
     */
    public void internationalTrade(){
        System.out.println("You can trade internationally!");
    }


    /**
     * Issues the business a loan
     * @param accountNumber The number of the account
     * @param sortCode The sort code of the account
     * @param loanAmount The loan amount
     * @throws SQLException
     */
    public void setNewLoanAmount(int accountNumber,int sortCode,BigDecimal loanAmount) throws SQLException {
        int businessAccountID ;
        AccountHandler getBusinessID = new AccountHandler();
        BusinessAccountHandler businessAccountHandler = new BusinessAccountHandler();
        businessAccountID = getBusinessID.getBusinessAccountID(accountNumber,sortCode);
        businessAccountHandler.updateLoanAmount( businessAccountID,loanAmount);
    }


}
