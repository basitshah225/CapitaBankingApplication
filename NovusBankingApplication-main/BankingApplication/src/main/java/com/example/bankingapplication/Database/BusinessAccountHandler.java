package com.example.bankingapplication.Database;

import com.example.bankingapplication.Database.Records.BusinessAccountRecord;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

/**
 * Class to handle CRUD requests to the registration table
 */
public class BusinessAccountHandler {

    /**
     * Private string which contains the url
     */
    private static String url;

    /**
     * Private string which contains the username for the database
     */
    private static String usernameDatabase;

    /**
     * Private string which contains the password for the database
     */
    private static String passwordDatabase;

    /**
     * Constructor which sets up the database information
     * @throws SQLException e
     */
    public BusinessAccountHandler() throws SQLException {
        DatabaseInformation databaseInformation = new DatabaseInformation();
        url = databaseInformation.getUrl();
        usernameDatabase = databaseInformation.getUsernameDatabase();
        passwordDatabase = databaseInformation.getPasswordDatabase();
    }


    /**
     * Returns if the business has had a cheque book issued
     * @param businessAccountID The business ID to select the information for
     * @return resultsB which is a boolean of the result
     * @throws SQLException e
     */
    public boolean selectChequeBookIssued(int businessAccountID) throws SQLException {
        String sql = "SELECT chequeBookIssued FROM BusinessAccounts WHERE businessAccountID = ?";
        boolean resultsB = true;
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, businessAccountID);
            var result = stmt.executeQuery();
            if (result.next()) {
                resultsB = result.getBoolean("chequeBookIssued");
            }
        }
        return resultsB;
    }



    /**
     * Returns the loan amount of the business from the business id
     * @param businessAccountID The business ID to select the information for
     * @return resultsBD which is a Big Decimal of the result
     * @throws SQLException e
     */
    public BigDecimal selectLoanAmount(int businessAccountID) throws SQLException {
        String sql = "SELECT loanAmount FROM BusinessAccounts WHERE businessAccountID = ?";
        BigDecimal resultsBD = new BigDecimal(-1);
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, businessAccountID);
            var result = stmt.executeQuery();
            if (result.next()) {
                resultsBD = result.getBigDecimal("loanAmount");
            }
        }
        return resultsBD;
    }



    /**
     * Returns the over draft amount of the business from the business id
     * @param businessAccountID The business ID to select the information for
     * @return resultsBD which is a Big Decimal of the result
     * @throws SQLException e
     */
    public BigDecimal selectOverDraftAmount(int businessAccountID) throws SQLException {
        String sql = "SELECT overdraftAmount FROM BusinessAccounts WHERE businessAccountID = ?";
        BigDecimal resultsBD = new BigDecimal(-1);
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, businessAccountID);
            var result = stmt.executeQuery();
            if (result.next()) {
                resultsBD = result.getBigDecimal("overdraftAmount");
            }
        }
        return resultsBD;
    }

    /**
     * Returns the id of the business
     * @param businessAccountID The business ID to select the information for
     * @return resultsI which is the account of the business itself
     * @throws SQLException e
     */
    public int selectBusinessID(int businessAccountID) throws SQLException {
        String sql = "SELECT businessID FROM BusinessAccounts WHERE businessAccountID = ?";
        int resultsI = -1;
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, businessAccountID);
            var result = stmt.executeQuery();
            if (result.next()) {
                resultsI = result.getInt("businessID");
            }
        }
        return resultsI;
    }

    /**
     * Returns the account id of the business from the business ID
     * @param businessID The business ID to select the information for
     * @return resultsI which is the account of the business account
     * @throws SQLException e
     */
    public int selectAccountBusinessID(int businessID) throws SQLException {
        String sql = "SELECT businessAccountID FROM BusinessAccounts WHERE businessID = ?";
        int resultsI = -1;
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, businessID);
            var result = stmt.executeQuery();
            if (result.next()) {
                resultsI = result.getInt("businessAccountID");
            }
        }
        return resultsI;
    }

    /**
     * Returns all the information in a row of the Business Account table
     * @param businessAccountID The business account to look for
     * @return A Business account record containing all the information in the table
     * @throws SQLException e
     */
    public BusinessAccountRecord selectBusinessAccount(int businessAccountID) throws SQLException {
        String sql = "SELECT * FROM BusinessAccounts WHERE businessAccountID = ?";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, businessAccountID);
            var result = stmt.executeQuery();
            if (result.next()) {
                return new BusinessAccountRecord(result.getInt("businessAccountID"), result.getBigDecimal("overdraftAmount"), result.getBoolean("chequeBookIssued"), result.getBigDecimal("loanAmount"), result.getInt("businessID"));
            }
            return null;
        }
    }


    /**
     * Inserts an account into the database
     * @param businessID the id of the business
     * @param overdraftAmount The overdraft amount
     * @param chequeBookIssuedVal Boolean for if a cheque book has been issued
     * @param loanAmount The amount to loan the business
     * @return The id of the account created
     * @throws SQLException e
     */
    public int insertBusinessAccount(int businessID, BigDecimal overdraftAmount, boolean chequeBookIssuedVal, BigDecimal loanAmount) throws SQLException {
        int id = -1;
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            String sql = "INSERT INTO BusinessAccounts (businessID, overdraftAmount, chequeBookIssued, loanAmount, dateLastCharged) VALUES (?, CAST(? AS NUMERIC(19,8)), CAST(? AS BIT) ,CAST(? AS NUMERIC(19,4)), ?);";
            var stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            Date date = Date.valueOf(LocalDate.now());
            stmt.setObject(1, businessID);
            stmt.setBigDecimal(2, overdraftAmount);
            stmt.setObject(3, chequeBookIssuedVal);
            stmt.setBigDecimal(4, loanAmount);
            stmt.setDate(5, date);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }
        return id;
    }




    /**
     * Updates an existing loan with a new loan amount
     * @param businessAccountID The businessAccount to update
     * @param loanAmount The new over loan amount
     * @throws SQLException e
     */
    public void updateLoanAmount(int businessAccountID, BigDecimal loanAmount) throws SQLException {
        String sql = "UPDATE BusinessAccounts SET loanAmount = ? WHERE businessAccountID = ?;";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, loanAmount);
            stmt.setObject(2, businessAccountID);
            var result = stmt.executeUpdate();
            if (result > 0) {
                System.out.printf("Loan successfully changed to %s ",loanAmount);
            }
        }
    }

    /**
     * Updates an existing overdraft with a new overdraft
     * @param businessAccountID The businessAccount to update
     * @param newOverDraftAmount The new over draft number
     * @throws SQLException e
     */
    public void updateOverDraftAmount(int businessAccountID, BigDecimal newOverDraftAmount) throws SQLException {
        String sql = "UPDATE BusinessAccounts SET overdraftAmount = ? WHERE businessAccountID = ?;";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, newOverDraftAmount);
            stmt.setObject(2, businessAccountID);
            var result = stmt.executeUpdate();
            if (result > 0) {
                System.out.printf("Overdraft successfully changed to %s ",newOverDraftAmount);
            }
        }
    }

    /**
     * Updates the value of the cheque book issued
     * @param businessAccountID The businessAccount to update
     * @param bit Sets the cheque book issued to 1 for true, 0 for false
     * @throws SQLException e
     */
    public void updateChequeBook(int businessAccountID, boolean bit) throws SQLException {
        String sql = "UPDATE BusinessAccounts SET chequeBookIssued = ? WHERE businessAccountID = ?;";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            if(bit){
                stmt.setObject(1, 1);
            }else{
                stmt.setObject(1, 0);
            }
            stmt.setObject(2, businessAccountID);
            var result = stmt.executeUpdate();
            if (result > 0) {
                System.out.printf("Cheque book successfully changed to %s ",bit);
            }
        }
    }

    /**
     * Deletes the specified businessAccount record
     * @param businessAccountID The businessAccount to delete
     * @throws SQLException e
     */
    public void deleteBusinessAccount(int businessAccountID) throws SQLException {
        String sql = "DELETE FROM BusinessAccounts WHERE businessAccountID = ?;";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, businessAccountID);
            var result = stmt.executeUpdate();
            if (result > 0 ) {
                System.out.printf("%s successfully deleted from Business table", businessAccountID);
            }
        }
    }

    /**
     * Get the date last charged
     * @param businessAccountID The business account to get the value for
     * @return A Date of last charged
     * @throws SQLException e
     */
    public Date getDateLastCharged(int businessAccountID) throws SQLException {
        String sql = "SELECT dateLastCharged FROM BusinessAccounts WHERE businessAccountID = ?";
        Date resultsI = null;
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, businessAccountID);
            var result = stmt.executeQuery();
            if (result.next()) {
                resultsI = result.getDate("dateLastCharged");
            }
        }
        return resultsI;
    }


    /**
     * Works out if the date of charging needs to be reset
     * @param businessAccountID The account ID of the business
     * @param dateCharged The date last charged
     * @throws SQLException e
     */
    public void resetDateLastCharged(int businessAccountID, Date dateCharged) throws SQLException {
        // Date currentDate = Date.valueOf(LocalDate.now());
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            //String sql4 = "SELECT DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1) AS EndOfYear;";
            //var stmt4 = conn.prepareStatement(sql4);
            //var result = stmt4.executeQuery();
            //if (result.next()) {
               // Date endOfYearDate = result.getDate("endOfYear");
                //if (currentDate.compareTo(endOfYearDate) == 0) {
                    String sql3 = "UPDATE BusinessAccounts SET dateLastCharged =  ? where businessAccountID = ?;";
                    var stmt3 = conn.prepareStatement(sql3);
                    stmt3.setObject(1, dateCharged);
                    stmt3.setObject(2, businessAccountID);
                    int result3 = stmt3.executeUpdate();
               // }
           // }
        }
    }



}
