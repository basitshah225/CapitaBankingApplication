package com.example.bankingapplication.Threads;

import com.example.bankingapplication.Accounts.Account;
import com.example.bankingapplication.Accounts.ISAAccount;
import com.example.bankingapplication.Accounts.Records.AccountIdentifier;
import com.example.bankingapplication.Database.AccountHandler;
import com.example.bankingapplication.Database.BusinessAccountHandler;
import com.example.bankingapplication.Database.DatabaseInformation;
import com.example.bankingapplication.Database.GeneralSQLQueries;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Business thread which charges the business 120 a year
 */
public class BusinessThread extends Thread{

    /**
     * Boolean for whether to check or not
     */
    private boolean check = true;

    /**
     * Cancels the thread
     */
    public void cancel() {
        check = false;
        interrupt();
    }

    /**
     * Checks to see if to charge the business
     * Charges the businesses at the end of the year
     * @throws SQLException e
     */
    public void checkForBusinessCharge() throws SQLException {

        // Gets the current date and sees if it's the last day of the year
        // This should only run once per day

        Date currentDate = Date.valueOf(LocalDate.now());
        GeneralSQLQueries generalSQLQueries = new GeneralSQLQueries();
        Date endOfYearDate = generalSQLQueries.getEndOfYearDate();

        // Compares them
        if (currentDate.compareTo(endOfYearDate) == 0) {
            // Gets all the accounts details
            AccountHandler accountHandler = new AccountHandler();
            ArrayList<AccountIdentifier> accountDetails = accountHandler.getAllBusinessAccounts();
            for (AccountIdentifier a : accountDetails) {
                // For each business account get the date last charged
                BusinessAccountHandler bah = new BusinessAccountHandler();
                Account acc = accountHandler.getAccount(a.accountNumber(), a.sortCode());
                int businessAccountID = accountHandler.getBusinessAccountID(a.accountNumber(), a.sortCode());
                Date dateLastCharged = bah.getDateLastCharged(businessAccountID);

                // If the date last charged is not null and it's before the end of year date then charge the account
                // Sets the date last charged
                if(dateLastCharged != null){
                    if(dateLastCharged.before(endOfYearDate)){
                        accountHandler.decreaseAccountBalance(acc.getAccountNumber(), acc.getSortCode(), BigDecimal.valueOf(120));
                        bah.resetDateLastCharged(businessAccountID, endOfYearDate);
                    }
                }
            }
        }
    }

    /**
     * Run overrides run in thread, checks every 24 hours
     */
    public void run(){
        int howLongToCheckInMinutes = 1440;
        try {
            while (check){
                checkForBusinessCharge();
                Thread.sleep(1000 * 60 * howLongToCheckInMinutes);
            }
        } catch (SQLException | InterruptedException e) {

        }
    }


}
