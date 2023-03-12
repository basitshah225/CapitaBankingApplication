package com.example.bankingapplication.Threads;

import com.example.bankingapplication.Accounts.Account;
import com.example.bankingapplication.Accounts.ISAAccount;
import com.example.bankingapplication.Accounts.Records.AccountIdentifier;
import com.example.bankingapplication.Database.AccountHandler;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class for updating the ISA APY every N seconds, extends thread
 * Needs to exist outside the GUI / console
 */
public class ISAUpdaterThread extends Thread{

    /**
     * Boolean to check the APYs
     */
    private boolean check = true;

    /**
     * Checks for apy update
     * Gets all the ISA accounts
     * Calls the .apyCalculation method on each account
     * @throws SQLException e
     */
    public void updateISABalanceForAPY() throws SQLException {
        AccountHandler accountHandler = new AccountHandler();
        ArrayList<AccountIdentifier> accountDetails = accountHandler.getAllISAAccounts();
        for (AccountIdentifier a: accountDetails) {
            Account acc = accountHandler.getAccount(a.accountNumber(), a.sortCode());
            ISAAccount accISA = (ISAAccount) acc;
            accISA.aprCalculation();
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
     * The code to run for the thread
     * Sleeps the thread for n minutes every run
     * Loops and calls update balance
     */
    public void run(){
        int howLongToCheckInMinutes = 1440;
        try {
            while (check){
                updateISABalanceForAPY();
                Thread.sleep(1000 * 60 * howLongToCheckInMinutes);
            }
        } catch (SQLException | InterruptedException e) {

        }
    }

}
