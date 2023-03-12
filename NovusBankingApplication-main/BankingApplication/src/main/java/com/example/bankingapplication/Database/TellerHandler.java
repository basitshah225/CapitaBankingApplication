package com.example.bankingapplication.Database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TellerHandler {

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
    public TellerHandler() throws SQLException {
        DatabaseInformation dbi = new DatabaseInformation();
        url = dbi.getUrl();
        usernameDatabase = dbi.getUsernameDatabase();
        passwordDatabase = dbi.getPasswordDatabase();
    }


    /**
     * Inserts a teller into the database
     * @param username The username of the teller
     * @param password The password of the teller
     * @throws SQLException e
     */
    public void insertTeller(String username, String password) throws SQLException {
        String sql = "INSERT INTO Teller (username, password) values (?,?)";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, username);
            stmt.setObject(2, password);
            int result = stmt.executeUpdate();
            if(result > 0){
                System.out.printf("New Teller %s, added to table", username);
            }
        }
    }


    /**
     * Validates a teller, returns true if the teller is valid
     * @param _username The username of the teller
     * @param _password The password of the teller
     * @return Return if the teller is valid or not
     */
    public boolean validateTeller(String _username, String _password){
        String sql = "SELECT username, password FROM Teller WHERE username=? and password=?;";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, _username);
            stmt.setObject(2, _password);
            var rs = stmt.executeQuery();
           if(rs.next()){
               if(_username.equals(rs.getString("username")) && _password.equals(rs.getString("password"))){
                   return true;
               }
               else{
                   return false;
               }
           }
           return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the tellers username
     * @param currentUserName The current username
     * @param newUserName The username to replace it with
     * @throws SQLException e
     */
    public void updateTellerUsername(String currentUserName, String newUserName) throws SQLException {
        String sql = "UPDATE Teller SET username = ? WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, newUserName);
            stmt.setObject(2, currentUserName);
            var result = stmt.executeUpdate();
            if (result > 0) {
                System.out.printf("Username successfully changed from %s to %s ",currentUserName, newUserName);
            }
        }
    }

    /**
     * Deletes a teller from the database
     * @param username The username to delete
     * @throws SQLException e
     */
    public void deleteTeller(String username) throws SQLException {
        String sql = "DELETE FROM Teller WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, username);
            var result = stmt.executeUpdate();
            if (result > 0 ) {
                System.out.printf("%s successfully deleted from Teller table", username);
            }
        }
    }


}
