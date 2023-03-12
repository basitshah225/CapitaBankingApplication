package com.example.bankingapplication.Accounts;

import com.example.bankingapplication.Database.AccountHandler;
import com.example.bankingapplication.Database.BusinessAccountHandler;
import com.example.bankingapplication.Database.PersonalAccountsHandler;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Class which represents a Personal account and inherits from account
 */
public class PersonalAccount extends Account{
    /**
     * @param overDraftAmount The amount the customer has for their over draft
     * @param balance The balance of the account
     * @param address The address of the customer
     * @param accountNumber The account number of the account
     * @param sortCode The sort code of the account
     * @param firstName The firstName of the customer
     * @param lastName The lastName of the customer
     * @param photoIDNumber The number of the ID
     * @param photoIDType The photo ID type that account registered with
     */
    public PersonalAccount(BigDecimal overDraftAmount, BigDecimal balance, String address, int accountNumber, int sortCode, String firstName, String lastName, String photoIDNumber, PhotoIDType photoIDType) {
        super("PersonalAccount", balance, address, accountNumber, sortCode, firstName, lastName, photoIDNumber, photoIDType);
        this.overDraftAmount = overDraftAmount;
    }

    /**
     * The overdraft the person has
     */
    private BigDecimal overDraftAmount;

    /**
     * @return The over draft amount
     */
    public BigDecimal getOverDraftAmount() {
        return overDraftAmount;
    }

    /**
     * @param newAmount Sets the over draft
     */
    public void setOverDraftAmount(BigDecimal newAmount) throws SQLException {
        setOverdraft(getAccountNumber(), getSortCode(), newAmount);
        this.overDraftAmount = newAmount;
    }





}