package com.example.bankingapplication.Database;

import com.example.bankingapplication.Accounts.*;
import com.example.bankingapplication.Accounts.Records.AccountIdentifier;
import com.example.bankingapplication.Database.Records.BusinessAccountRecord;
import com.example.bankingapplication.Database.Records.CustomerRecord;
import com.example.bankingapplication.Database.Records.ISAAccountRecord;
import com.example.bankingapplication.Database.Records.PersonalAccountRecord;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

/**
 * Class which handles calls to the Account table in the database
 */
public class AccountHandler {

    /**
     * Url of database
     */
    private static String url;

    /**
     * Username of database
     */
    private static String usernameDatabase;

    /**
     * Password of database
     */
    private static String passwordDatabase;

    /**
     * Constructor for Handler, sets up the database connection info
     */
    public AccountHandler() {
        DatabaseInformation dbi = new DatabaseInformation();
        url = dbi.getUrl();
        usernameDatabase = dbi.getUsernameDatabase();
        passwordDatabase = dbi.getPasswordDatabase();
    }

    /**
     * Adds a business account to the Accounts table
     * Sets up the foreign key references inside Registration, Customer and Business Account table
     * @param sortCode The sort code of the account
     * @param balance The initial balance of the account
     * @param businessID The id of the business
     * @param overdraftAmount The over draft amount
     * @param chequeBookIssuedVal If the cheque book has been issued
     * @param loanAmount The amount in the loan
     * @param firstName The first name of the customer
     * @param lastName The last name of the customer
     * @param address The address of the customer
     * @param photoIDType The photo ID type
     * @param registrationIDNumber The registration number
     * @return the id for the account in account table
     * @throws SQLException e
     */
    public int insertAccountBusiness(int sortCode, BigDecimal balance, int businessID, BigDecimal overdraftAmount, boolean chequeBookIssuedVal, BigDecimal loanAmount, String firstName, String lastName, String address, Enum<PhotoIDType> photoIDType, String registrationIDNumber) throws SQLException {
        int id = -1;
        //create customer
        CustomerHandler customerHandler = new CustomerHandler();
        int customerID = customerHandler.insertCustomer(firstName, lastName, address, registrationIDNumber, String.valueOf(photoIDType));

        boolean alreadyHasBusiness = false;

        BusinessAccountHandler businessAccountHandler = new BusinessAccountHandler();
        int potentialAccountID = businessAccountHandler.selectAccountBusinessID(businessID);

        if(potentialAccountID != -1){
            alreadyHasBusiness = true;
            System.out.println("Customer already has Business account");
        }


        if(!alreadyHasBusiness){
            //create business account
            BusinessAccountHandler businessHandler = new BusinessAccountHandler();
            int businessAccountID = businessHandler.insertBusinessAccount(businessID, overdraftAmount, chequeBookIssuedVal, loanAmount);
            //create main account
            String sql = "INSERT INTO Account (sortCode, balance, accountType, BusinessAccountID, customerID) values (?,?,?,?,?)";
            try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
                var stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setObject(1, sortCode);
                stmt.setObject(2, balance);
                stmt.setObject(3, "BusinessAccount");
                stmt.setObject(4, businessAccountID);
                stmt.setObject(5, customerID);
                int result = stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }
        }

        return id;
    }

    /**
     * Adds a personal account to the Accounts table
     * @param sortCode The sort code of the account
     * @param balance The initial balance of the account
     * @param overdraftAmount The over draft amount
     * @param firstName The first name of the customer
     * @param lastName The last name of the customer
     * @param address The address of the customer
     * @param photoIDType The photo ID type
     * @param registrationIDNumber The registration number
     * @return the id for the account in account table
     * @throws SQLException e
     */
    public int insertAccountPersonal(int sortCode, BigDecimal balance, BigDecimal overdraftAmount, String firstName, String lastName, String address, String registrationIDNumber, String photoIDType) throws SQLException {
        int id = -1;
        //create customer
        CustomerHandler customerHandler = new CustomerHandler();
        int customerID = customerHandler.insertCustomer(firstName, lastName, address, registrationIDNumber, photoIDType);
        //create Personal account
        PersonalAccountsHandler personalHandler = new PersonalAccountsHandler();
        int personalAccountID = personalHandler.insertPersonalAccount(overdraftAmount);
        //create main account
        String sql = "INSERT INTO Account (sortCode, balance, accountType, personalAccountID, customerID) values (?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setObject(1, sortCode);
            stmt.setObject(2, balance);
            stmt.setObject(3, "PersonalAccount");
            stmt.setObject(4, personalAccountID);
            stmt.setObject(5, customerID);
            int result = stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }
        return id;
    }


    /**
     * Adds an ISA account to the Accounts table
     * @param sortCode The sort code of the account
     * @param balance The initial balance of the account
     * @param moneyInThisYear The amount of money
     * @param firstName The first name of the customer
     * @param lastName The last name of the customer
     * @param address The address of the customer
     * @param photoIDType The photo ID type
     * @param registrationIDNumber The registration number
     * @return the id for the account in account table
     * @throws SQLException e
     */
    public int insertAccountISA(int sortCode, BigDecimal balance, BigDecimal moneyInThisYear, String firstName, String lastName, String address, String registrationIDNumber, String photoIDType) throws SQLException {
        int id = -1;
        //create customer
        CustomerHandler customerHandler = new CustomerHandler();
        int customerID = customerHandler.insertCustomer(firstName, lastName, address, registrationIDNumber, photoIDType);

        boolean alreadyHasISA = false;

        // checks to see if the customer already has an ISA account

        ArrayList<AccountIdentifier> customerAccounts = getAllAccountsOfOneCustomer(customerID);
        for (AccountIdentifier ai: customerAccounts){
            Account acc = getAccount(ai.accountNumber(), ai.sortCode());
            if(acc.getAccountType().equals("ISAAccount")){
                alreadyHasISA = true;
                System.out.println("Customer already has ISA account");
            }
        }

        // If they don't then create on

        if(!alreadyHasISA){
            ISAAccountHandler isaHandler = new ISAAccountHandler();
            int isaAccountID = isaHandler.insertISAAccount(moneyInThisYear);
            //create main account
            String sql = "INSERT INTO Account (sortCode, balance, accountType, isaAccountID, customerID) values (?,?,?,?,?)";
            try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
                var stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setObject(1, sortCode);
                stmt.setObject(2, balance);
                stmt.setObject(3, "ISAAccount");
                stmt.setObject(4, isaAccountID);
                stmt.setObject(5, customerID);
                int result = stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }
        }

        return id;
    }

    /**
     * @param customerID The id of the customer
     * @return An array list for all the accounts the customer has
     * @throws SQLException e
     */
    public ArrayList<AccountIdentifier> getAllAccountsOfOneCustomer(int customerID) throws SQLException {
        ArrayList<AccountIdentifier> accounts = new ArrayList<>();
        String sql = "SELECT accountNumber, sortCode FROM Account where customerID = ?;";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, customerID);
            var accountResult = stmt.executeQuery();
            while(accountResult.next()){
                accounts.add(new AccountIdentifier(accountResult.getInt("accountNumber"), accountResult.getInt("sortCode")));
            }
        }
        return accounts;
    }

    /**
     * @return An arrayList of all the accounts in the database
     * @throws SQLException e
     */
    public ArrayList<AccountIdentifier> getAllAccounts() throws SQLException {
        ArrayList<AccountIdentifier> accounts = new ArrayList<>();
        String sql = "SELECT accountNumber, sortCode FROM Account;";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            var accountResult = stmt.executeQuery();
            while(accountResult.next()){
                accounts.add(new AccountIdentifier(accountResult.getInt("accountNumber"), accountResult.getInt("sortCode")));
            }

        }
        return accounts;
    }

    /**
     * @return An array list of all the ISA accounts in the table
     * @throws SQLException e
     */
    public ArrayList<AccountIdentifier> getAllISAAccounts() throws SQLException {
        ArrayList<AccountIdentifier> accounts = new ArrayList<>();
        String sql = "SELECT accountNumber, sortCode FROM Account where isaAccountID IS NOT NULL;";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            var accountResult = stmt.executeQuery();
            while(accountResult.next()){
                accounts.add(new AccountIdentifier(accountResult.getInt("accountNumber"), accountResult.getInt("sortCode")));
            }
        }
        return accounts;
    }

    /**
     * @return An array list of all the business accounts in the table
     * @throws SQLException e
     */
    public ArrayList<AccountIdentifier> getAllBusinessAccounts() throws SQLException {
        ArrayList<AccountIdentifier> accounts = new ArrayList<>();
        String sql = "SELECT accountNumber, sortCode FROM Account where businessAccountID IS NOT NULL;";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            var accountResult = stmt.executeQuery();
            while(accountResult.next()){
                System.out.println(accountResult.getInt("accountNumber"));
                accounts.add(new AccountIdentifier(accountResult.getInt("accountNumber"), accountResult.getInt("sortCode")));
            }
        }
        return accounts;
    }



    /**
     * The method to return an account from the database
     * @param accountNumber The account number for the account
     * @param sortCode The sort code for the account, creates a composite primary key for the account
     * @return An Account with the information put  in
     * @throws SQLException e
     */
    public Account getAccount(int accountNumber, int sortCode) throws SQLException{
        // Sets an account to null;
        Account accountToReturn = null;
        // Selects the account from the account table
        String sql = "SELECT * FROM Account WHERE accountNumber = ? and sortCode = ?;";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, accountNumber);
            stmt.setObject(2, sortCode);
            var accountResult = stmt.executeQuery();

            // If the account exists
            // Get the balance, account type, and customer ID from this table
            if(accountResult.next()) {
                BigDecimal balance = accountResult.getBigDecimal("balance");
                String accountType = accountResult.getString("accountType");
                int customerID = accountResult.getInt("customerID");

                // Calls the customer select to get the information about that customer from the customer ID
                CustomerHandler customerHandler = new CustomerHandler();
                CustomerRecord customerResultSet = customerHandler.viewCustomer(customerID);
                if(customerResultSet!=null){

                    // Gets the first name, last name, address, registration number from customer
                    String firstname = customerResultSet.firstName();
                    String lastName = customerResultSet.lastName();
                    String address = customerResultSet.address();
                    String registrationPhotoIDNumber = customerResultSet.registrationNumber();
                    // Uses the registration number to get the registration type of this number
                    RegistrationDatabaseHandler registrationDatabaseHandler = new RegistrationDatabaseHandler();
                    String registrationPhotoType = registrationDatabaseHandler.selectRegistration(registrationPhotoIDNumber).get(1);
                    PhotoIDType photoIDTypeEnum = null;

                    // Creates the enum of photo id type
                    if(registrationPhotoType.equals("passport")){
                        photoIDTypeEnum = PhotoIDType.passport;
                    } else if (registrationPhotoType.equals("driversLicense")) {
                        photoIDTypeEnum = PhotoIDType.driversLicense;

                    }

                    // Creates the specific account type depending on the type
                    switch (accountType) {
                        case "PersonalAccount" -> {
                            // Gets the id, creates a record of the specific account type with the information stored
                            // returns an account with the correct parameters
                            int personalAccountID = accountResult.getInt("personalAccountID");
                            PersonalAccountsHandler personalAccountsHandler = new PersonalAccountsHandler();
                            PersonalAccountRecord personalAccountRecord = personalAccountsHandler.selectPersonalAccount(personalAccountID);
                            if (personalAccountRecord != null) {
                                accountToReturn = new PersonalAccount(personalAccountRecord.overdraftAmount(), balance, address, accountNumber, sortCode, firstname, lastName, registrationPhotoIDNumber, photoIDTypeEnum);
                            }
                        }
                        case "BusinessAccount" -> {
                            // Gets the id, creates a record of the specific account type with the information stored
                            // returns an account with the correct parameters
                            int businessAccountID = accountResult.getInt("businessAccountID");
                            BusinessAccountHandler businessAccountHandler = new BusinessAccountHandler();
                            BusinessAccountRecord businessAccountRecord = businessAccountHandler.selectBusinessAccount(businessAccountID);
                            if (businessAccountRecord != null) {
                                accountToReturn = new BusinessAccount(businessAccountRecord.businessID(), businessAccountRecord.overdraftAmount(), balance, address, accountNumber, sortCode, firstname, lastName, registrationPhotoIDNumber, photoIDTypeEnum, businessAccountRecord.loanAmount());
                            }
                        }
                        case "ISAAccount" -> {
                            // Gets the id, creates a record of the specific account type with the information stored
                            // returns an account with the correct parameters
                            int ISAAccountID = accountResult.getInt("isaAccountID");
                            ISAAccountHandler isaAccountHandler = new ISAAccountHandler();
                            ISAAccountRecord isaAccountRecord = isaAccountHandler.selectISAAccount(ISAAccountID);
                            if (isaAccountRecord != null) {
                                accountToReturn = new ISAAccount(balance, address, accountNumber, sortCode, firstname, lastName, registrationPhotoIDNumber, photoIDTypeEnum, isaAccountRecord.moneyInThisYear(), isaAccountRecord.dateLastUpdated());
                            }
                        }
                    }
                }
            }
            return accountToReturn;
        }
    }


    /**
     * Transfers money between two accounts in a thread safe way
     * If any limits need adding then this would need to be copied and pasted for any child account
     * @param sendingAccountNumber The account number of the sender
     * @param sendingSortCode The sort code of the sender
     * @param amount The amount to send
     * @param receivingAccountNumber The account number of the receiver
     * @param receivingSortCode The sort code of the receiver
     * @throws SQLException e
     */
    public void transferMoneyBetweenAccount(int sendingAccountNumber, int sendingSortCode, BigDecimal amount, int receivingAccountNumber, int receivingSortCode) throws SQLException {

        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            try {
                // Sets up the locks and autocommit set to false, it can roll back if the transaction is not complete
                conn.setAutoCommit(false);
                String sql = "" +
                        "BEGIN TRANSACTION; " +
                        "DECLARE @deadlock_var NCHAR(6);" +
                        "SET @deadlock_var = N'NORMAL';" +
                        "DECLARE @sendingBalance decimal(19,4); SELECT @sendingBalance = balance FROM Account WITH (XLOCK, ROWLOCK) WHERE accountNumber = ? and sortCode = ?;" +
                        "UPDATE Account WITH (XLOCK, ROWLOCK) SET balance = @sendingBalance-? WHERE accountNumber = ? and sortCode = ?;" +
                        "DECLARE @receivingBalance decimal(19,4); SELECT @receivingBalance = balance FROM Account WITH (XLOCK, ROWLOCK) WHERE accountNumber = ? and sortCode = ?;" +
                        "UPDATE Account WITH (XLOCK, ROWLOCK) SET balance = @receivingBalance+? WHERE accountNumber = ? and sortCode = ?;" +
                        "COMMIT TRANSACTION;";

                // Sets the objects

                var stmt = conn.prepareStatement(sql);
                stmt.setObject(1, sendingAccountNumber);
                stmt.setObject(2, sendingSortCode);
                stmt.setObject(3, amount);
                stmt.setObject(4, sendingAccountNumber);
                stmt.setObject(5, sendingSortCode);
                stmt.setObject(6, receivingAccountNumber);
                stmt.setObject(7, receivingSortCode);
                stmt.setObject(8, amount);
                stmt.setObject(9, receivingAccountNumber);
                stmt.setObject(10, receivingSortCode);

                int result = stmt.executeUpdate();
                if (result > 0) {
                    conn.commit();
                    stmt.close();
                    conn.close();
                }
            }catch (Exception e ){
                // If exception don't commit
                conn.rollback();
                conn.close();
            }
        }

    }

    /**
     * Increases a single accounts balance
     * If any limits need adding then this would need to be copied and pasted for any child account
     * @param accountNumber The account number of the sender
     * @param sortCode The sort code of the sender
     * @param increaseAmount The amount to add
     * @throws SQLException e
     */
    public void increaseAccountBalance(int accountNumber, int sortCode, BigDecimal increaseAmount) throws SQLException {

        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            conn.setAutoCommit(false);
            String sql = " BEGIN TRANSACTION; SELECT balance FROM Account WITH (XLOCK, ROWLOCK) WHERE accountNumber = ? and sortCode = ?;";
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, accountNumber);
            stmt.setObject(2, sortCode);
            var result = stmt.executeQuery();
            if (result.next()) {
                BigDecimal bal = result.getBigDecimal("balance");
                String update = "UPDATE Account WITH (XLOCK, ROWLOCK) SET balance = ?+? WHERE accountNumber = ? and sortCode = ?; COMMIT TRANSACTION;";
                var stmt2 = conn.prepareStatement(update);
                stmt2.setObject(1, bal);
                stmt2.setObject(2, increaseAmount);
                stmt2.setObject(3, accountNumber);
                stmt2.setObject(4, sortCode);
                int result2 = stmt2.executeUpdate();
                if (result2 > 0) {
                    conn.commit();
                }
            }

        }
    }


    /**
     * Decreases a single accounts balance
     * If any limits need adding then this would need to be copied and pasted for any child account
     * @param accountNumber The account number of the sender
     * @param sortCode The sort code of the sender
     * @param decreaseAmount The amount to decrease
     * @throws SQLException e
     */
    public void decreaseAccountBalance(int accountNumber, int sortCode, BigDecimal decreaseAmount) throws SQLException {

        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            conn.setAutoCommit(false);
            String sql = " BEGIN TRANSACTION; SELECT balance FROM Account WITH (XLOCK, ROWLOCK) WHERE accountNumber = ? and sortCode = ?;";
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, accountNumber);
            stmt.setObject(2, sortCode);
            var result = stmt.executeQuery();
            if (result.next()) {
                BigDecimal bal = result.getBigDecimal("balance");
                String update = "UPDATE Account WITH (XLOCK, ROWLOCK) SET balance = ?-? WHERE accountNumber = ? and sortCode = ?; COMMIT TRANSACTION;";
                var stmt2 = conn.prepareStatement(update);
                stmt2.setObject(1, bal);
                stmt2.setObject(2, decreaseAmount);
                stmt2.setObject(3, accountNumber);
                stmt2.setObject(4, sortCode);
                int result2 = stmt2.executeUpdate();
                if (result2 > 0) {
                    conn.commit();
                }
            }

        }
    }

    /**
     * Sets an accounts balance to whatever is passed in
     * If any limits need adding then this would need to be copied and pasted for any child account
     * @param accountNumber The account number of the sender
     * @param sortCode The sort code of the sender
     * @param balance The amount to set the balance to
     * @throws SQLException e
     */
    public void updateAccountBalance(int accountNumber, int sortCode, BigDecimal balance) throws SQLException {
        String sql = "BEGIN TRANSACTION; UPDATE Account WITH (XLOCK, ROWLOCK) SET balance = ? WHERE accountNumber = ? and sortCode = ? COMMIT TRANSACTION ";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, balance);
            stmt.setObject(2, accountNumber);
            stmt.setObject(3, sortCode);
            var result = stmt.executeUpdate();
            if (result > 0) {
                System.out.println("New balance for ("+accountNumber+","+sortCode+") : " + balance);
            }
        }
    }

    /**
     * Updates the address of an account
     * @param accountNumber The account number of the account
     * @param sortCode The sort code of the account
     * @param newAddress The new address
     * @throws SQLException e
     */
    public void updateAccountAddress(int accountNumber, int sortCode, String newAddress) throws SQLException {
        CustomerHandler customerHandler = new CustomerHandler();
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            String sql = "SELECT customerID FROM Account WHERE accountNumber = ? and sortCode = ?;";
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, accountNumber);
            stmt.setObject(2, sortCode);
            var rs =stmt.executeQuery();
            if(rs.next()){
                int customerID = rs.getInt("customerID");
                customerHandler.updateCustomerAddress(customerID, newAddress);
            }
        }
    }

    /**
     * Updates the last name of an account
     * @param accountNumber The account number of the account
     * @param sortCode The sort code of the account
     * @param lastName The new last name
     * @throws SQLException e
     */
    public void updateAccountLastName(int accountNumber, int sortCode, String lastName) throws SQLException {
        CustomerHandler customerHandler = new CustomerHandler();
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            String sql = "SELECT customerID FROM Account WHERE accountNumber = ? and sortCode = ?;";
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, accountNumber);
            stmt.setObject(2, sortCode);
            var rs =stmt.executeQuery();
            if(rs.next()){
                int customerID = rs.getInt("customerID");
                customerHandler.updateCustomerLastName(customerID, lastName);
            }
        }
    }

    /**
     * Gets the balance of the account in a thread safe way
     * @param accountNumber The account number for the account
     * @param sortCode The sort code of the account
     * @return The balance
     * @throws SQLException e
     */
    public BigDecimal getAccountBalance(int accountNumber, int sortCode) throws SQLException {
        String sql = "BEGIN TRANSACTION; SELECT balance FROM Account WITH (XLOCK, ROWLOCK) WHERE accountNumber = ? and sortCode = ?; COMMIT TRANSACTION;";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, accountNumber);
            stmt.setObject(2, sortCode);
            var result = stmt.executeQuery();
            if(result.next()) {
                return result.getBigDecimal("balance");
            }
            return null;
        }

    }

    /**
     * @param accountNumber The account number of the account to delete
     * @throws SQLException e
     */
    public void deleteAccount(int accountNumber) throws SQLException {
        String sql = "DELETE FROM Account WHERE accountNumber = ?";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, accountNumber);
            var result = stmt.executeUpdate();
            if (result > 0 ) {
                System.out.printf("Account Successfully deleted from Account table");
            }
        }
    }

    /**
     * Gets the ISA account id of the account
     * @param accountNumber The account number of the account
     * @param sortCode The sort code of the account
     * @return the ISA account ID
     * @throws SQLException e
     */
    public int getISAAccountID(int accountNumber, int sortCode) throws SQLException {
        String sql = "SELECT isaAccountID FROM ACCOUNT WITH (XLOCK, ROWLOCK) WHERE accountNumber = ? and sortCode = ?;";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt  = conn.prepareStatement(sql);
            stmt.setObject(1, accountNumber);
            stmt.setObject(2, sortCode);
            var result = stmt.executeQuery();
            if(result.next()) {
                return result.getInt("ISAAccountID");
            }
            return -1;
        }
    }

    /**
     * Gets the business account id of the account
     * @param accountNumber The account number of the account
     * @param sortCode The sort code of the account
     * @return the business account ID
     * @throws SQLException e
     */
    public int getBusinessAccountID(int accountNumber, int sortCode) throws SQLException {
        String sql = "SELECT businessAccountID FROM ACCOUNT WITH (XLOCK, ROWLOCK) WHERE accountNumber = ? and sortCode = ?;";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt  = conn.prepareStatement(sql);
            stmt.setObject(1, accountNumber);
            stmt.setObject(2, sortCode);
            var result = stmt.executeQuery();
            if(result.next()) {
                return result.getInt("businessAccountID");
            }
            return -1;
        }
    }


    /**
     * Gets the Personal account id of the account
     * @param accountNumber The account number of the account
     * @param sortCode The sort code of the account
     * @return the Personal account ID
     * @throws SQLException e
     */
    public int getPersonalAccountID(int accountNumber, int sortCode) throws SQLException {
        String sql = "SELECT * FROM Account WHERE accountNumber = ? and sortCode = ?";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, accountNumber);
            stmt.setObject(2, sortCode);
            var result = stmt.executeQuery();
            if(result.next()) {
                return result.getInt("personalAccountID");
            }
            return -1;
        }

    }


}
