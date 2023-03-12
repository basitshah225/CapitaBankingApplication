package com.example.bankingapplication.Threads;

import com.example.bankingapplication.Accounts.Account;
import com.example.bankingapplication.Accounts.ISAAccount;
import com.example.bankingapplication.Accounts.Records.AccountIdentifier;
import com.example.bankingapplication.Database.AccountHandler;
import com.example.bankingapplication.Database.ISAAccountHandler;

import java.sql.SQLException;
import java.util.ArrayList;

public class ISAResetterThread extends Thread {

    /**
     * Boolean to check the APYs
     */
    private boolean check = true;

    /**
     * Checks for resetting the ISA money
     * Gets all the ISA accounts
     * Creates the ISA account handler and tries to reset
     * @throws SQLException e
     */
    public void checkForMoneyReset() throws SQLException {
        AccountHandler accountHandler = new AccountHandler();
        ArrayList<AccountIdentifier> accountDetails = accountHandler.getAllISAAccounts();
        for (AccountIdentifier a: accountDetails) {
            ISAAccountHandler isaAccountHandler = new ISAAccountHandler();
            isaAccountHandler.resetMoneyThisYear(a.accountNumber(), a.sortCode());
        }
    }

    /**
     * Sets check to false and interrupts thread
     */
    public void cancel() {
        check = false;
        interrupt();
    }

    /**
     * The code to run for the thread, runs once a day
     * Sleeps the thread for n minutes every run
     * Loops and calls update balance
     */
    public void run(){
        int howLongToCheckInMinutes = 1440;
        try {
            while (check){
                checkForMoneyReset();
                Thread.sleep(1000 * 60 * howLongToCheckInMinutes);
            }
        } catch (SQLException | InterruptedException e) {

        }
    }
}
