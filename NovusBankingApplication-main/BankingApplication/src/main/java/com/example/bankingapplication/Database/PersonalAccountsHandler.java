package com.example.bankingapplication.Database;

import com.example.bankingapplication.Database.Records.PersonalAccountRecord;

import java.math.BigDecimal;
import java.sql.*;

/**
 * Class which handles calls to the Personal Account table in the database
 */
public class PersonalAccountsHandler {
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
    public PersonalAccountsHandler() {
        DatabaseInformation dbi = new DatabaseInformation();
        url = dbi.getUrl();
        usernameDatabase = dbi.getUsernameDatabase();
        passwordDatabase = dbi.getPasswordDatabase();
    }

    /**
     * Inserts a new personal account into the database
     * @param overdraftAmount The amount of the over draft
     * @return An ID of the personal account
     * @throws SQLException e
     */
    public int insertPersonalAccount (BigDecimal overdraftAmount) throws SQLException {
        int id = -1;
        String sql = "INSERT INTO PersonalAccounts (overdraftAmount) values (?)";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setObject(1, overdraftAmount);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }
        return id;
    }


    /**
     * Retrieves the information for one row in the personal table
     * @param personalAccountID The ID of the personal account
     * @return A personal account record of the information
     * @throws SQLException e
     */
    public PersonalAccountRecord selectPersonalAccount(int personalAccountID) throws SQLException {
        String sql = "SELECT personalAccountID, overdraftAmount FROM PersonalAccounts WHERE personalAccountID = ?";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, personalAccountID);
            var result = stmt.executeQuery();
            if (result.next()) {
                return new PersonalAccountRecord(result.getInt("personalAccountID"), result.getBigDecimal("overdraftAmount"));
            }
            return null;
        }
    }


    /**
     * Updates an accounts overdraft
     * @param personalAccountID The ID of the account to update
     * @param overdraftAmount The amount to update the overdraft with
     * @throws SQLException e
     */
    public void updatePersonalAccountOverdraft(int personalAccountID, BigDecimal overdraftAmount) throws SQLException {
        String sql = "UPDATE PersonalAccounts SET overdraftAmount = ? WHERE personalAccountID = ?";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, overdraftAmount);
            stmt.setObject(2, personalAccountID);
            var result = stmt.executeUpdate();
            if (result > 0) {
                System.out.printf("Overdraft amount changed to %s", overdraftAmount);
            }
        }
    }

    /**
     * Deletes a personal account
     * @param personalAccountID The ID of the account to delete
     * @throws SQLException e
     */
    public void deletePersonalAccount(int personalAccountID) throws SQLException {
        String sql = "DELETE FROM PersonalAccounts WHERE personalAccountID = ?";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, personalAccountID);
            var result = stmt.executeUpdate();
            if (result > 0 ) {
                System.out.printf("Personal Account Successfully deleted from PersonalAccounts table");
            }
        }
    }

}
