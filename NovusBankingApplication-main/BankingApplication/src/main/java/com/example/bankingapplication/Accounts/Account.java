package com.example.bankingapplication.Accounts;
import com.example.bankingapplication.Database.AccountHandler;
import com.example.bankingapplication.Database.BusinessAccountHandler;
import com.example.bankingapplication.Database.ISAAccountHandler;
import com.example.bankingapplication.Database.PersonalAccountsHandler;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Abstract class to represent account
 * Contains what all accounts should be able to do
 */
public abstract class Account {

    /**
     * The balance of the account
     */
    private BigDecimal balance;

    /**
     * The address of the customer
     */
    private String address;

    /**
     * The account number of the account
     */
    private int accountNumber;

    /**
     * The sort code of the account
     */
    private int sortCode;

    /**
     * The first name of the customer
     */
    private String firstName;

    /**
     * The last name of the customer
     */
    private String lastName;

    /**
     * The photo ID type that account registered with
     */
    private Enum<PhotoIDType> photoIDType;

    /**
     * The number of the ID
     */
    private String photoIDNumber;

    /**
     * The type of account it is, personal | business | ISA
     */
     private String accountType;

    /**
     * @param accountType The type of account
     * @param balance The balance of the account
     * @param address The address of the customer
     * @param accountNumber The account number of the account
     * @param sortCode The sort code of the account
     * @param firstName The firstName of the customer
     * @param lastName The lastName of the customer
     * @param photoIDNumber The number of the ID
     * @param photoIDType The photo ID type that account registered with
     */
    public Account(String accountType, BigDecimal balance, String address, int accountNumber, int sortCode, String firstName, String lastName, String photoIDNumber, PhotoIDType photoIDType) {
        this.balance = balance;
        this.address = address;
        this.accountNumber = accountNumber;
        this.sortCode = sortCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoIDNumber  =photoIDNumber;
        this.photoIDType = photoIDType;
        this.accountType = accountType;
    }

    /**
     * @return The type of account
     */
    public String getAccountType() {
            return accountType;
        }

    /**
     * @return The balance of the account
     */
    public BigDecimal getBalance() throws SQLException {

        AccountHandler accountHandler = new AccountHandler();
        BigDecimal newB = accountHandler.getAccountBalance(getAccountNumber(), getSortCode());
        if(!(newB.compareTo(balance) ==0)){
            balance = newB;
        }
        return balance;
        }

    /**
     * @return The address of the customer
     */
    public String getAddress() {
            return address;
        }

    /**
     * @param address Sets the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return The account number
     */
    public int getAccountNumber() {
            return accountNumber;
        }

    /**
     * @param balance The balance to set the account balance to
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * @return The sort code
     */
    public int getSortCode() {
            return sortCode;
        }

    /**
     * @return The first name of the customer
     */
        public String getFirstName() {
            return firstName;
        }

        // public void setFirstName(String firstName) {
            //this.firstName = firstName;
        //}

    /**
     * @return The last name of the customer
     */
    public String getLastName() {
            return lastName;
        }

    /**
     * @param lastName Set the last name of the customer
     */
    public void setLastName(String lastName) {
            this.lastName = lastName;
        }

    /**
     * @param increaseBy The number to increase the balance by
     */
    public void increaseBalance(int accountNumber, int sortCode , BigDecimal increaseBy) throws SQLException {
        AccountHandler acc = new AccountHandler();
        acc.increaseAccountBalance(accountNumber,sortCode,increaseBy);
    }

    /**
     * @param decreaseBy The number to decrease the balance by
     */
    public void decreaseBalance(int accountNumber, int sortCode ,BigDecimal decreaseBy) throws SQLException {
        AccountHandler acc = new AccountHandler();
        acc.decreaseAccountBalance(accountNumber,sortCode,decreaseBy);
    }

    /**
     * Sends money from this account to another account
     * @param senderAccountNumber account number for account that sends money
     * @param senderSortCode sortCode for account that sends money
     * @param receiverAccountNumber The account number of the receiving account
     * @param receiverSenderSortCode The sort code of the receiving account
     * @param amount The amount to send
     * @return true if it sent, false if it did not
     */
    public boolean sendMoneyToOtherAccount(int senderAccountNumber, int senderSortCode, BigDecimal amount, int receiverAccountNumber, int receiverSenderSortCode) throws SQLException {
        BigDecimal balance;
        BigDecimal overdraft;
        BigDecimal predictedBalance;
        BigDecimal zero = new BigDecimal(0);
        String accountType;
        int businessID;
        int personalID;
        int isaID;
        boolean confirmTransaction = false;
        //get reciver account
        AccountHandler recevierAcc = new AccountHandler();
        AccountHandler senderAcc = new AccountHandler();

        boolean checking;
        AccountHandler sendersAcc = new AccountHandler();

        BusinessAccountHandler sender = new BusinessAccountHandler();


        //check if receiver is an isa account;
        accountType = senderAcc.getAccount(senderAccountNumber, senderSortCode).getAccountType();
        //if they want to add to isa account
        System.out.println(accountType);

        switch(accountType) {
            case "ISAAccount":
                balance = sendersAcc.getAccountBalance(senderAccountNumber, senderSortCode);
                System.out.println("Current account balance" + balance);
                //get overdraft amount
                //subtract amount from senders balance
                predictedBalance = balance.subtract(amount);
                //get isaiD
                isaID = sendersAcc.getBusinessAccountID(senderAccountNumber, senderSortCode);

                System.out.println("predicted balance" + predictedBalance);
                finishTransactionForISA(senderAccountNumber, senderSortCode, amount, receiverAccountNumber, receiverSenderSortCode, predictedBalance);
                break;

            case "BusinessAccount":
                //get account balance
                //get overdraft amount
                //subtract amount from senders balance
                balance = sendersAcc.getAccountBalance(senderAccountNumber, senderSortCode);
                predictedBalance = balance.subtract(amount);
                //getID
                businessID = sendersAcc.getBusinessAccountID(senderAccountNumber, senderSortCode);

                BigDecimal businesslAccountOverdraft = new BusinessAccountHandler().selectBusinessAccount(businessID).overdraftAmount();


                businesslAccountOverdraft = businesslAccountOverdraft.negate();
                finishTransaction(senderAccountNumber, senderSortCode, amount, receiverAccountNumber, receiverSenderSortCode, predictedBalance, businesslAccountOverdraft);
                break;
            case "PersonalAccount":

                //get account balance
                balance = sendersAcc.getAccountBalance(senderAccountNumber, senderSortCode);
                System.out.println("Current account balance" + balance);
                //get overdraft amount
                //subtract amount from senders balance
                predictedBalance = balance.subtract(amount);
                //get personalID
                personalID = sendersAcc.getPersonalAccountID(senderAccountNumber, senderSortCode);
                BigDecimal personalAccountOvedraft = new PersonalAccountsHandler().selectPersonalAccount(personalID).overdraftAmount();
                System.out.println("Business OverDraft Amount" + personalAccountOvedraft);

                personalAccountOvedraft = personalAccountOvedraft.negate();
                System.out.println(personalAccountOvedraft);
                System.out.println("predicted balance" + predictedBalance);

                finishTransaction(senderAccountNumber, senderSortCode, amount, receiverAccountNumber, receiverSenderSortCode, predictedBalance, personalAccountOvedraft);
                break;
            default:
                // code block
        }



        return confirmTransaction;

    }

    /**
     * Finishes the Transaction following the determination of the sender account
     * @param senderAccountNumber account number for account that sends money
     * @param senderSortCode sortCode for account that sends money
     * @param amount the amount of money to be received and sent between accounts
     * @param receiverAccountNumber account number that receives money
     * @param receiverSenderSortCode sortCode that sends money
     * @param predictedBalance account that is to be left in the sender account
     * @param overDraftAmountForAccount how much overdraft the account has
     * @return true if its finished
     * @throws SQLException e
     */
    public boolean finishTransaction(int senderAccountNumber, int senderSortCode, BigDecimal amount, int receiverAccountNumber, int receiverSenderSortCode,BigDecimal predictedBalance,BigDecimal overDraftAmountForAccount) throws SQLException {
        AccountHandler recevierAcc = new AccountHandler();
        AccountHandler sendersAcc = new AccountHandler();
        ISAAccountHandler account = new ISAAccountHandler();
        int isaID=recevierAcc.getISAAccountID(receiverAccountNumber,receiverSenderSortCode);

        boolean confirmTransaction = false;
        BigDecimal zero = new BigDecimal(0);
        boolean checking;
        String accountType;

        accountType = recevierAcc.getAccount(receiverAccountNumber, receiverSenderSortCode).getAccountType();

        if(recevierAcc.getAccount(receiverAccountNumber,receiverSenderSortCode)!=null) {
            Account acc = recevierAcc.getAccount(receiverAccountNumber, receiverSenderSortCode);
            checking = acc.confirmTransaction(receiverAccountNumber, receiverSenderSortCode, amount);
            if (checking == true) {

                System.out.println("predicted balance" + predictedBalance);
                if (predictedBalance.compareTo(zero) == 1) {
                    //Predicated balance" + predictedBalance + " is greater than" + zero
                    sendersAcc.transferMoneyBetweenAccount(senderAccountNumber, senderSortCode, amount, receiverAccountNumber, receiverSenderSortCode);
                    if(accountType=="ISAAccount"){
                        BigDecimal isabalance = account.getMoneyInthisYear(isaID);
                        account.UpdateMoneyInThisYear(isaID,isabalance.add(amount));
                    }
                    return confirmTransaction = true;
                } else if (predictedBalance.compareTo(zero) == -1) {
                    //"Predicated balance" + predictedBalance + " is less than" + zero
                    if (predictedBalance.compareTo(overDraftAmountForAccount) == 1) {
                        //"Predicated balance" + predictedBalance + " is greater than" + overDraftAmountForAccount
                        sendersAcc.transferMoneyBetweenAccount(senderAccountNumber, senderSortCode, amount, receiverAccountNumber, receiverSenderSortCode);
                        if(accountType=="ISAAccount"){
                            BigDecimal isabalance = account.getMoneyInthisYear(isaID);
                            account.UpdateMoneyInThisYear(isaID,isabalance.add(amount));
                        }
                        return confirmTransaction = true;
                    } else if (predictedBalance.compareTo(overDraftAmountForAccount) == -1) {
                        System.out.println("Predicated balance" + predictedBalance + " is less than" + overDraftAmountForAccount);
                        return confirmTransaction = false;
                    }
                } else if (predictedBalance.compareTo(zero) == 0) {
                    //"Predicated balance" + predictedBalance + " is equal to" + zero
                    sendersAcc.transferMoneyBetweenAccount(senderAccountNumber, senderSortCode, amount, receiverAccountNumber, receiverSenderSortCode);
                    if(accountType=="ISAAccount"){
                        BigDecimal isabalance = account.getMoneyInthisYear(isaID);
                        account.UpdateMoneyInThisYear(isaID,isabalance.add(amount));
                    }
                }
                return confirmTransaction;
            }
            else{
                confirmTransaction = false;
            }
        }
        else{
            return confirmTransaction;
        }
        return confirmTransaction;
    }

    /**
     *
     * @param senderAccountNumber account number for account that sends money
     * @param senderSortCode sortCode for account that sends money
     * @param amount the amount of money to be received and sent between accounts
     * @param receiverAccountNumber account number that receives money
     * @param receiverSenderSortCode sortCode that sends money
     * @param predictedBalance account that is to be left in the sender account
     * @return
     * @throws SQLException
     */


    public boolean finishTransactionForISA(int senderAccountNumber, int senderSortCode, BigDecimal amount, int receiverAccountNumber, int receiverSenderSortCode,BigDecimal predictedBalance) throws SQLException {
        BigDecimal balance;
        BigDecimal zero = new BigDecimal(0);

        boolean confirmTransaction = false;
        //get reciver account
        AccountHandler recevierAcc = new AccountHandler();
        AccountHandler senderAcc = new AccountHandler();
        ISAAccountHandler  account = new ISAAccountHandler();
        int isaID=recevierAcc.getISAAccountID(receiverAccountNumber,receiverSenderSortCode);

        boolean checking;
        AccountHandler sendersAcc = new AccountHandler();

        BusinessAccountHandler sender = new BusinessAccountHandler();


        if(recevierAcc.getAccount(receiverAccountNumber,receiverSenderSortCode)!=null) {
            confirmTransaction = confirmTransaction(receiverAccountNumber, receiverSenderSortCode, amount);
            if (confirmTransaction == true) {
                //get account balance
                balance = sendersAcc.getAccountBalance(senderAccountNumber, senderSortCode);
                //subtract amount from senders balance
                predictedBalance = balance.subtract(amount);

                //predicted balance" + predictedBalance
                if (predictedBalance.compareTo(zero) == 1) {
                    //Predicated balance" + predictedBalance + " is greater than" + zero
                    sendersAcc.transferMoneyBetweenAccount(senderAccountNumber, senderSortCode, amount, receiverAccountNumber, receiverSenderSortCode);
                    if(accountType=="ISAAccount"){
                        BigDecimal isabalance = account.getMoneyInthisYear(isaID);
                        account.UpdateMoneyInThisYear(isaID,isabalance.add(amount));
                    }
                    return confirmTransaction = true;
                } else if (predictedBalance.compareTo(zero) == 0) {
                    //Predicated balance" + predictedBalance + " is equal to" + zero
                    sendersAcc.transferMoneyBetweenAccount(senderAccountNumber, senderSortCode, amount, receiverAccountNumber, receiverSenderSortCode);
                    if(accountType=="ISAAccount"){
                        BigDecimal isabalance = account.getMoneyInthisYear(isaID);
                        account.UpdateMoneyInThisYear(isaID,isabalance.add(amount));
                    }
                    return confirmTransaction = true;
                } else {
                    return confirmTransaction = false;
                }

            }
            else{
                confirmTransaction = false;
            }
        }

        else{
            confirmTransaction = false;
        }
        return confirmTransaction;
    }

    /**
     * Handles confirming sending money between accounts
     * @param receiverAccountNumber The account number of the receiving account
     * @param receiverSenderSortCode The sort code of the receiving account
     * @param amount The amount to send
     * @return True if the transaction is confirmed
     */
    public boolean confirmTransaction( int receiverAccountNumber, int receiverSenderSortCode,BigDecimal amount) throws SQLException {
        boolean deposit = false;
        BigDecimal balance;
        BigDecimal predictedAmount;
        AccountHandler ac = new AccountHandler();
        BigDecimal maxAmount = new BigDecimal(20000);
        String accountType;
        //check if receiver is an isa account;
        accountType = ac.getAccount(receiverAccountNumber, receiverSenderSortCode).getAccountType();
        //if they want to add to isa account
        System.out.println(accountType);
        if (accountType.equals("ISAAccount")) {
            //get isa account balance
            balance = ac.getAccountBalance(receiverAccountNumber, receiverSenderSortCode);
            predictedAmount = balance.add(amount);
            //if predictedAmount is greater than 20000
            if (predictedAmount.compareTo(maxAmount) >0) {
                String message = "Cannot finish Transaction";
                deposit = false;
                //do you want to send money to another account
            }
            //if predictedAmount is less than 20000
            else if (predictedAmount.compareTo(maxAmount) < 0 ) {
                String message = "Transaction accepted";
                deposit = true;
            }
            //if predictedAmount is equal to 20000
            else if (predictedAmount.compareTo(maxAmount) == 0) {
                String message = "Transaction accepted";
                deposit = true;
                //then transaction cannot happen
                //if balance plus amount is less than 20000
                //then transaction can happen
            }

        }
        else{
            deposit=true;
        }
        return deposit;


    }

    /**
     * Overridden by 2 subclasses but default behavior is to just increase the balance
     * @param accountNumber The number of the account
     * @param sortCode The sort code of the account
     * @param increaseBy The value to increase the account by
     * @throws SQLException e
     */
    public boolean confirmDeposit(int accountNumber, int sortCode , BigDecimal increaseBy) throws SQLException {
        boolean confirmTransaction = false;
        AccountHandler account = new AccountHandler();
        Account thisAccount = account.getAccount(accountNumber,sortCode);
        thisAccount.increaseBalance(accountNumber,sortCode,increaseBy);
        return confirmTransaction = true;

    }

    /**
     * Always overridden in current subclasses however default behavior set to decrease balance
     * @param accountNumber the number of the account
     * @param sortCode the sort code of the account
     * @param amount the amount to decrease by
     * @throws SQLException e
     */
    public boolean confirmWithdraw(int accountNumber, int sortCode , BigDecimal amount) throws SQLException {
        BigDecimal balance;
        BigDecimal predictedBalance;
        int businessID;
        AccountHandler account = new AccountHandler();
        boolean confirmTransaction = false;
        BigDecimal zero = new BigDecimal(0);



        String accountType;
        int personalID;
        int isaID;

        balance = account.getAccountBalance(accountNumber, sortCode);
        accountType = account.getAccount(accountNumber, sortCode).getAccountType();
        predictedBalance = balance.subtract(amount);

        switch (accountType) {
            case "ISAAccount":

                confirmTransaction=confirmWithdrawforISA(accountNumber, sortCode, amount,predictedBalance,zero);
                break;
            case "BusinessAccount":

                businessID = account.getBusinessAccountID(accountNumber, sortCode);
                predictedBalance = balance.subtract(amount);
                BigDecimal businesslAccountOverdraft = new BusinessAccountHandler().selectBusinessAccount(businessID).overdraftAmount();
                businesslAccountOverdraft = businesslAccountOverdraft.negate();
                confirmTransaction=finishWithdrawalForBusinessAndPersonal(accountNumber, sortCode,amount,predictedBalance, businesslAccountOverdraft,zero);
                break;

            case "PersonalAccount":
                personalID = account.getPersonalAccountID(accountNumber, sortCode);

                BigDecimal personalAccountOverdraft = new PersonalAccountsHandler().selectPersonalAccount(personalID).overdraftAmount();
                personalAccountOverdraft = personalAccountOverdraft.negate();
                confirmTransaction=finishWithdrawalForBusinessAndPersonal(accountNumber, sortCode,amount,predictedBalance, personalAccountOverdraft,zero);


                break;
            default:
                //something

        }
        return confirmTransaction;
    }

    /**
     *
     * @param accountNumber account number for account being withdrawn from
     * @param sortCode sortCode for account being withdrawn from
     * @param amount amount being withdrawn
     * @param predictedBalance balance that will be left after money is withdrawn
     * @param overDraftAmount the overdraft amount of the account
     * @param zero BigDecimal object where value = 0 to for verification
     * @return Returns true if it finished the withdrawal
     * @throws SQLException e
     */

    public boolean finishWithdrawalForBusinessAndPersonal(int accountNumber, int sortCode,BigDecimal amount,BigDecimal predictedBalance,BigDecimal overDraftAmount,BigDecimal zero) throws SQLException {
        AccountHandler account = new AccountHandler();
        Account thisAccount = account.getAccount(accountNumber, sortCode);
        boolean confirmTransaction = false;

        if (predictedBalance.compareTo(zero) == 1) {
            //Predicated balance" + predictedBalance + " is greater than" + zero
            thisAccount.decreaseBalance(accountNumber,sortCode,amount);
            confirmTransaction = true;
        } else if (predictedBalance.compareTo(zero) == -1) {
            //"Predicated balance" + predictedBalance + " is less than" + zero
            if (predictedBalance.compareTo(overDraftAmount) == 1) {
                //"Predicated balance" + predictedBalance + " is greater than" + overDraftAmount
                thisAccount.decreaseBalance(accountNumber,sortCode,amount);
                confirmTransaction = true;
            } else if (predictedBalance.compareTo(overDraftAmount) == -1) {
                //Predicated balance" + predictedBalance + " is less than" + overDraftAmount
                confirmTransaction = false;
            }else if(predictedBalance.compareTo(overDraftAmount) == 0){
                //Predicated balance" + predictedBalance + " is equal to" + zero
                thisAccount.decreaseBalance(accountNumber,sortCode,amount);
                confirmTransaction = true;
            }
        } else if (predictedBalance.compareTo(zero) == 0) {
            //Predicated balance" + predictedBalance + " is equal to" + zero
            thisAccount.decreaseBalance(accountNumber,sortCode,amount);
        }

        return confirmTransaction;
    }

    /**
     * With draws money for ISA
     * @param accountNumber account number for account being withdrawn from
     * @param sortCode sortCode for account being withdrawn from
     * @param amount amount to be withdrawn
     * @param predictedBalance amount that will be lift after amount has been withdrawn
     * @param zero Big Decimal object with value zero
     * @return True if the isa is withdrawn
     * @throws SQLException e
     */
    public boolean confirmWithdrawforISA(int accountNumber, int sortCode, BigDecimal amount,BigDecimal predictedBalance, BigDecimal zero) throws SQLException {
        BigDecimal balance;

        AccountHandler account = new AccountHandler();

        boolean confirmTransaction = false;

        Account thisAccount = account.getAccount(accountNumber,sortCode);
        if (predictedBalance.compareTo(zero) == 1) {
            //Predicated balance" + predictedBalance + " is greater than" + zero
            thisAccount.decreaseBalance(accountNumber,sortCode,amount);
            return confirmTransaction = true;
        } else if (predictedBalance.compareTo(zero) == -1) {

            //"Predicated balance" + predictedBalance + " is less than" + zero
            return confirmTransaction = false;

        } else if (predictedBalance.compareTo(zero) == 0) {
            //Predicated balance" + predictedBalance + " is equal to" + zero);
            thisAccount.decreaseBalance(accountNumber,sortCode,amount);
        }

        return confirmTransaction;


    }


    /**
     * @return The photo id type
     */
    public Enum<PhotoIDType> getPhotoIDType(){

        return photoIDType;
    }

    /**
     * @return The number of the id
     */
    public String getPhotoIDNumber(){
            return photoIDNumber;
        }

    /**
     * Sets the overdraft
     * @param accountNumber account number for account that  has a overdraft being set
     * @param sortCode sortCode for account that  has a overdraft being set
     * @param overdraftAmount the new overdraft value
     * @throws SQLException e
     */
    public void setOverdraft(int accountNumber,int sortCode,BigDecimal overdraftAmount) throws SQLException {
        AccountHandler account = new AccountHandler();
        BigDecimal balance;
        String accountType;
        BigDecimal zero = new BigDecimal(0);
        int personalAccountID ;
        int businessAccountID ;
        BusinessAccountHandler businessAccountHandler = new BusinessAccountHandler();
        PersonalAccountsHandler personalAccountsHandler = new PersonalAccountsHandler();



        balance = account.getAccountBalance(accountNumber, sortCode);
        accountType = account.getAccount(accountNumber, sortCode).getAccountType();

        if (balance.compareTo(zero) == -1){
            String message="Cannot update overdraft amount";

        }
        else if(balance.compareTo(zero) == 0){
            String message = "Overdraft amount can be updated";
            if(accountType.equals("BusinessAccount")){
                businessAccountID=account.getBusinessAccountID(accountNumber,sortCode);
                businessAccountHandler.updateOverDraftAmount(businessAccountID,overdraftAmount);


            } else if (accountType.equals("PersonalAccount")) {
                personalAccountID=account.getPersonalAccountID(accountNumber,sortCode);
                personalAccountsHandler.updatePersonalAccountOverdraft(personalAccountID,overdraftAmount);

            }
        }else if(balance.compareTo(zero) == 1){
            String message = "Overdraft amount can be updated";
            if(accountType.equals("BusinessAccount")){
                businessAccountID=account.getBusinessAccountID(accountNumber,sortCode);
                businessAccountHandler.updateOverDraftAmount(businessAccountID,overdraftAmount);


            } else if (accountType.equals("PersonalAccount")) {
                personalAccountID=account.getPersonalAccountID(accountNumber,sortCode);
                personalAccountsHandler.updatePersonalAccountOverdraft(personalAccountID,overdraftAmount);

            }

        }



    }





}


