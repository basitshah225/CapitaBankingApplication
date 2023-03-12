package com.example.bankingapplication.Accounts;

import com.example.bankingapplication.Accounts.Records.AccountIdentifier;
import com.example.bankingapplication.Database.AccountHandler;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The class that manages accounts
 */
public class AccountManager {

    /**
     * Returns the sort code from the branch
     */
    SortCodeInformation sic = new SortCodeInformation("Capita");

    /**
     * Creates a personal account
     * @param firstname the first name
     * @param lastName the last name
     * @param address the address
     * @param initialBalance the current balance of the account at creation
     * @param photoIDType The type of id used
     * @param photoIDNumber The number of the id
     * @param overDraftAmount The overdraft amount
     * @return an account identifier containing account number and sort code
     * @throws SQLException e
     */
    public AccountIdentifier createPersonalAccount(String firstname, String lastName, String address, BigDecimal initialBalance, Enum<PhotoIDType> photoIDType, String photoIDNumber, BigDecimal overDraftAmount) throws SQLException {
        AccountHandler accountHandler = new AccountHandler();
        int id = accountHandler.insertAccountPersonal(sic.getSortCode(), initialBalance, overDraftAmount, firstname, lastName, address, photoIDNumber, String.valueOf(photoIDType));
        if(id == -1){
            return new AccountIdentifier(-1, -1);
        }
        return new AccountIdentifier(id, sic.getSortCode());
    }

    /**
     * @param firstname the first name
     * @param lastName the last name
     * @param address the address
     * @param moneyInAtStart the amount of money put into the isa this year
     * @param photoIDType The type of id used
     * @param photoIDNumber The number of the id
     * @return an account identifier containing account number and sort code
     * @throws SQLException e
     */
    public AccountIdentifier createISAAccount(String firstname, String lastName, String address, BigDecimal moneyInAtStart, Enum<PhotoIDType> photoIDType, String photoIDNumber) throws SQLException {
        AccountHandler accountHandler = new AccountHandler();
        int id =  accountHandler.insertAccountISA(sic.getSortCode(), moneyInAtStart, moneyInAtStart, firstname, lastName, address, photoIDNumber, String.valueOf(photoIDType));
        if(id == -1){
            return new AccountIdentifier(-1, -1);
        }
        return new AccountIdentifier(id, sic.getSortCode());
    }

    /**
     * @param firstname the first name
     * @param lastName the last name
     * @param address the address
     * @param initialBalance the balance of the account
     * @param photoIDType The type of id used
     * @param photoIDNumber The number of the id
     * @param businessID The id of the water
     * @param overDraftAmount The amount in the overdraft
     * @param loanAmount The amount the business loans
     * @return an account identifier containing account number and sort code
     * @throws SQLException w
     */
    public AccountIdentifier createBusinessAccount(String firstname, String lastName, String address, BigDecimal initialBalance, Enum<PhotoIDType> photoIDType, String photoIDNumber, int businessID, BigDecimal overDraftAmount, BigDecimal loanAmount) throws SQLException {
        AccountHandler accountHandler = new AccountHandler();
        int id =  accountHandler.insertAccountBusiness(sic.getSortCode(), initialBalance, businessID, overDraftAmount, false, loanAmount, firstname, lastName, address,  photoIDType,  photoIDNumber);
        if(id == -1){
            return new AccountIdentifier(-1, -1);
        }
        return new AccountIdentifier(id, sic.getSortCode());
    }


    /**
     * The method to get an account if its verified
     * @param accountNumber The number of the account
     * @param sortCode The sort code of the account
     * @param firstName The first name of the customer
     * @param lastName The last name of the customer
     * @param address The address of the customer
     * @return An account
     * @throws SQLException e
     */
    public Account getAccount(int accountNumber, int sortCode, String firstName, String lastName, String address) throws SQLException {
        AccountHandler accountHandler = new AccountHandler();
        Account acc = accountHandler.getAccount(accountNumber, sortCode);
        if(firstName.equalsIgnoreCase(acc.getFirstName())){
            if(lastName.equalsIgnoreCase(acc.getLastName())) {
                if (address.equalsIgnoreCase(acc.getAddress())) {
                    return acc;
                }
            }
        }
        return null;
    }
}
