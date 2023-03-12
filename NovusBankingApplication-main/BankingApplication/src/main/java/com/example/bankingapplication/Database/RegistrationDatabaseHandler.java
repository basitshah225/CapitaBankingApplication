package com.example.bankingapplication.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class to handle CRUD requests to the registration table
 */
public class RegistrationDatabaseHandler {

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
    public RegistrationDatabaseHandler() throws SQLException {
        DatabaseInformation databaseInformation = new DatabaseInformation();
        url = databaseInformation.getUrl();
        usernameDatabase = databaseInformation.getUsernameDatabase();
        passwordDatabase = databaseInformation.getPasswordDatabase();
    }

    /**
     * Returns the photoIDType based on the registration number
     * @param registrationPhotoIDNumber The photoIDNumber to select the information for
     * @return resultsS which is a string array of the results
     * @throws SQLException e
     */
    public ArrayList<String> selectRegistration(String registrationPhotoIDNumber) throws SQLException {
        String sql = "SELECT registrationPhotoIDNumber, photoIDType FROM Registration WHERE registrationPhotoIDNumber = ?;";
        ArrayList<String> resultsS = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, registrationPhotoIDNumber);
            var result = stmt.executeQuery();
            if (result.next()) {
                resultsS.add(result.getString("registrationPhotoIDNumber"));
                resultsS.add(result.getString("photoIDType"));
            }
        }
        return resultsS;
    }

    /**
     * Adds a new registration to the table
     * @param registrationPhotoIDNumber The registration number to insert
     * @param photoIDType The type of photo ID
     * @throws SQLException e
     */
    public void insertRegistration(String registrationPhotoIDNumber, String photoIDType) throws SQLException {
        String sql_res= "SELECT * FROM Registration where registrationPhotoIDNumber=?";
        boolean alreadyExists = false;
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql_res);
            stmt.setObject(1, registrationPhotoIDNumber);
            var rs = stmt.executeQuery();
            alreadyExists = rs.next();
        }

        if(!alreadyExists){
            String sql = "INSERT INTO Registration (registrationPhotoIDNumber, photoIDType) values (?,?);";
            try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
                var stmt = conn.prepareStatement(sql);
                stmt.setObject(1, registrationPhotoIDNumber);
                stmt.setObject(2, photoIDType);
                int result = stmt.executeUpdate();
                if(result > 0){
                    System.out.printf("New Registration %s, added to table%n", registrationPhotoIDNumber);
                }
            }
        }
        else{
            System.out.println("Registration already exists");
        }

    }

    /**
     * Updates an existing registration number with a new registration number and type
     * @param oldRegistrationPhotoIDNumber The old registration number
     * @param newRegistrationPhotoIDNumber The new registration number
     * @param newPhotoIDType The type of photo ID
     * @throws SQLException e
     */
    public void updateRegistration(String oldRegistrationPhotoIDNumber, String newRegistrationPhotoIDNumber, String newPhotoIDType) throws SQLException {
        String sql = "UPDATE Registration SET registrationPhotoIDNumber = ?, photoIDType = ? WHERE oldRegistrationPhotoIDNumber = ?;";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, newRegistrationPhotoIDNumber);
            stmt.setObject(2, newPhotoIDType);
            stmt.setObject(3, oldRegistrationPhotoIDNumber);
            var result = stmt.executeUpdate();
            if (result > 0) {
                System.out.printf("Registration successfully changed from %s to %s ",oldRegistrationPhotoIDNumber, newRegistrationPhotoIDNumber);
            }
        }
    }

    /**
     * Deletes the specified registration record
     * @param RegistrationPhotoIDNumber The registration to delete
     * @throws SQLException e
     */
    public void deleteRegistration(String RegistrationPhotoIDNumber) throws SQLException {
        String sql = "DELETE FROM Registration WHERE RegistrationPhotoIDNumber = ?;";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, RegistrationPhotoIDNumber);
            var result = stmt.executeUpdate();
            if (result > 0 ) {
                System.out.printf("%s successfully deleted from Teller table", RegistrationPhotoIDNumber);
            }
        }
    }

}
