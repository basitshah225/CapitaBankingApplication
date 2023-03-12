package com.example.bankingapplication.Database;

import com.example.bankingapplication.Database.Records.ISAAccountRecord;
//import com.example.bankingapplication.Accounts.ISAAccount;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;


/**
 * Class which handles calls to the ISA Account table in the database
 */
public class ISAAccountHandler {

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
    public ISAAccountHandler() throws SQLException {
        DatabaseInformation dbi = new DatabaseInformation();
        url = dbi.getUrl();
        usernameDatabase = dbi.getUsernameDatabase();
        passwordDatabase = dbi.getPasswordDatabase();
    }


    /**
     * Inserts an isa account into the database
     * @param moneyInThisYear The amount of money they have put in
     * @return The ID of the ISA account
     * @throws SQLException e
     */
    public int insertISAAccount(BigDecimal moneyInThisYear) throws SQLException {
        int id = -1;
        String sql = "insert into ISAAccounts(moneyInThisYear, TimeOfLastUpdate)values(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            Date date = Date.valueOf(LocalDate.now());
            stmt.setObject(1, moneyInThisYear);
            stmt.setDate(2, date);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }
        return id;
    }

    /**
     * Returns an isa account record with the information of one row in the database
     * @param ISAAccountID The id of the account to get the information for
     * @return The record of information
     * @throws SQLException e
     */
    public ISAAccountRecord selectISAAccount(int ISAAccountID) throws SQLException {
        String sql = "SELECT * FROM ISAAccounts WHERE isaAccountID = ?";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, ISAAccountID);
            var result = stmt.executeQuery();
            if (result.next()) {
                return new ISAAccountRecord(ISAAccountID, result.getBigDecimal("moneyInThisYear"), result.getDate("TimeOfLastUpdate"));
            }
            return null;
        }
    }


    /**
     * Updates the money in this year for the account
     * @param isaAccountID The ID of the account to update
     * @param moneyInThisYear The money in this year
     * @throws SQLException e
     */
    public void UpdateMoneyInThisYear(int isaAccountID, BigDecimal moneyInThisYear) throws SQLException {
        String sql = "UPDATE ISAAccounts SET moneyInThisYear = ? WHERE isaAccountID = ?";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, moneyInThisYear);
            stmt.setObject(2, isaAccountID);
            int result = stmt.executeUpdate();
            if (result > 0) {
                System.out.printf("Isa account with id %s added to the table", isaAccountID);
            }
        }
    }

    /**
     * Deletes an account
     * @param isaAccountID The ID of the account to delete
     * @throws SQLException e
     */
    public void deleteISAAccount(int isaAccountID) throws SQLException {
        String sql = "DELETE FROM ISAAccounts WHERE isaAccountID = ?;";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, isaAccountID);
            var result = stmt.executeUpdate();
            if (result > 0) {
                System.out.printf("Isa account with id %s deleted to the table", isaAccountID);
            }
        }
    }

    /**
     * Updates the time since last update
     * @param isaAccountID The account ID to update
     * @throws SQLException e
     */
    public void UpdateTimeOfLastUpdate(int isaAccountID, Date newDate) throws SQLException {
        String sql = "UPDATE ISAAccounts SET TimeOfLastUpdate = ? WHERE isaAccountID = ?";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, newDate);
            stmt.setObject(2, isaAccountID);
            int result = stmt.executeUpdate();
            if (result > 0) {
                System.out.printf("Isa account with id %s added to the table", isaAccountID);
            }
        }
    }


    /**
     * Calculates the APR of an account and adds it to the balance
     * @param annualRate The annual rate percentage to use
     * @param compoundingOption The compounding time period
     * @param accountNumber The account number to use
     * @param sortCode The sort code to use
     * @throws SQLException e
     */
    // apyCalculation
    public void aprCalculation(double annualRate, int compoundingOption, int accountNumber, int sortCode) throws SQLException {
        // Gets the ISA account id
        AccountHandler accountHandler = new AccountHandler();
        int isaAccountID = accountHandler.getISAAccountID(accountNumber, sortCode);
        // Gets the time of last update and money in this year of that account
        String sql = "SELECT TimeOfLastUpdate, moneyInThisYear FROM ISAAccounts WHERE isaAccountID = ?;";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, isaAccountID);
            var result = stmt.executeQuery();
            // If it isn't null
            if (result.next()) {
                // Gets the end of the month date
                Date date = result.getDate("TimeOfLastUpdate");
                BigDecimal moneyInThisYear = result.getBigDecimal("moneyInThisYear");
                String sql2 = "SELECT TimeOfLastUpdate, EOMONTH(TimeOfLastUpdate) AS LastDayDate FROM ISAAccounts WHERE isaAccountID = ?;";
                var stmt2 = conn.prepareStatement(sql2);
                stmt2.setObject(1, isaAccountID);
                var resultLast = stmt2.executeQuery();
                if (resultLast.next()) {
                    Date lastDateDate = resultLast.getDate("LastDayDate");
                    BigDecimal balance = accountHandler.getAccountBalance(accountNumber, sortCode);
                    // If its the last date of the month, calc the apr and then set the dates so it does it next month
                    if (lastDateDate.compareTo(date) == 0) {
                        //if (moneyInThisYear.compareTo(BigDecimal.valueOf(20000)) > 0) {

                            BigDecimal newBalance = balance.multiply(BigDecimal.valueOf((2.75 / 12) / 100));
                            balance = balance.add(newBalance);
                            System.out.println(newBalance);
                            System.out.println(balance);
                            accountHandler.updateAccountBalance(accountNumber, sortCode, balance);
                       // }
                        String sql3 = "UPDATE ISAAccounts SET TimeOfLastUpdate  = DATEADD(day, 1 , ?) where isaAccountID = ?;";
                        var stmt3 = conn.prepareStatement(sql3);
                        stmt3.setObject(1, lastDateDate);
                        stmt3.setObject(2, isaAccountID);
                        int result3 = stmt3.executeUpdate();
                    }

                }

            }

        }
    }

    /**
     * Resets the account of money put in the ISA account in the year back to 0
     * @param accountNumber The account number of the account
     * @param sortCode The sort code of the account
     * @throws SQLException e
     */
    public void resetMoneyThisYear(int accountNumber, int sortCode) throws SQLException {
        Date currentDate = Date.valueOf(LocalDate.now());
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            String sql4 = "SELECT DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1) AS EndOfYear;";
            var stmt4 = conn.prepareStatement(sql4);
            var result = stmt4.executeQuery();
            if (result.next()) {
                Date endOfYearDate = result.getDate("endOfYear");
                if (currentDate.compareTo(endOfYearDate) == 0) {
                    AccountHandler accountHandler = new AccountHandler();
                    int isaAccountID = accountHandler.getISAAccountID(accountNumber, sortCode);
                    String sql = "UPDATE ISAAccounts SET moneyInThisYear=0 FROM ISAAccounts WHERE isaAccountID = ?;";
                    {
                        var stmt = conn.prepareStatement(sql);
                        stmt.setObject(1, isaAccountID);
                        var result2 = stmt.executeUpdate();
                    }
                }
            }
        }
    }


    /**
     * Gets the money in this year from the isa account
     * @param isaAccountID The ID of the account to use
     * @return The money in this year
     * @throws SQLException e
     */
    public BigDecimal getMoneyInthisYear(int isaAccountID)throws SQLException{
        BigDecimal moneyInThisYear = new BigDecimal(0);
        String sql = "SELECT moneyInThisYear FROM ISAAccounts WHERE isaAccountID = ?";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, isaAccountID);
            var rs =stmt.executeQuery();
            if(rs.next()){
                moneyInThisYear = rs.getBigDecimal("moneyInThisYear");
                return moneyInThisYear;
            }

        }
        return moneyInThisYear;
    }





}