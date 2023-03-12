package com.example.bankingapplication.Database;

import com.example.bankingapplication.Database.Records.CustomerRecord;

import java.sql.*;

/**
 * Class which handles calls to the Customer table in the database
 */
public class CustomerHandler {
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
    public CustomerHandler() throws SQLException {
        DatabaseInformation dbi = new DatabaseInformation();
        url = dbi.getUrl();
        usernameDatabase = dbi.getUsernameDatabase();
        passwordDatabase = dbi.getPasswordDatabase();
    }

    /**
     * Inserts a customer into the database
     * Also adds the registration first
     * @param firstName The first name of the customer
     * @param lastName The last name of the customer
     * @param address The address of the customer
     * @param registrationIDNumber The number of the ID
     * @param photoIDType The type of the ID
     * @return The id of the customer record
     * @throws SQLException e
     */
    public int insertCustomer(String firstName, String lastName, String address, String registrationIDNumber, String photoIDType)  throws SQLException{
        int id = -1;

        RegistrationDatabaseHandler rd = new RegistrationDatabaseHandler();
        rd.insertRegistration(registrationIDNumber, photoIDType);

        // checks to see if customer already exists

        String sql_res= "SELECT * FROM Customer where registrationPhotoIDNumber=?";
        boolean alreadyExists = false;
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql_res);
            stmt.setObject(1, registrationIDNumber);
            var rs = stmt.executeQuery();
            alreadyExists = rs.next();
            if(alreadyExists){
                id = rs.getInt("customerID");
                return id;
            }
        }

        String sql = "INSERT INTO Customer (firstName,lastName,address,registrationPhotoIDNumber) values (?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setObject(1, firstName);
            stmt.setObject(2, lastName);
            stmt.setObject(3, address);
            stmt.setObject(4, registrationIDNumber);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }
        return id;
    }


    /**
     * Deletes a customer
     * @param customerID The id of the customer to delete
     * @throws SQLException e
     */
    public void deleteCustomer(int customerID) throws SQLException {
        String sql = "DELETE FROM Customer WHERE customerID = ?";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, customerID);
            var result = stmt.executeUpdate();
            if (result > 0 ) {
                System.out.printf("%s successfully deleted from Customer table", customerID);
            }
        }
    }


    /**
     * this method updates the customer first name with a message is the query is successful
     * @param customerID The if of the customer
     * @param firstName The new first name
     * @throws SQLException e
     */
    public void updateCustomerFirstName(int customerID, String firstName) throws SQLException {
        String sql = "UPDATE Customer SET firstName = ? WHERE customerID = ?";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, firstName);
            stmt.setObject(2, customerID);
            var result = stmt.executeUpdate();
            if (result > 0 ) {
                System.out.printf("%s successfully updated from Customer table", customerID);
            }
        }
    }

    /**
     * this method updates the customer last name with a message is the query is successful
     * @param customerID The if of the customer
     * @param lastName The new last name
     * @throws SQLException e
     */
    public void updateCustomerLastName(int customerID, String lastName) throws SQLException {
        String sql = "UPDATE Customer SET lastName = ? WHERE customerID = ?";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, lastName);
            stmt.setObject(2, customerID);
            var result = stmt.executeUpdate();
            if (result > 0 ) {
                System.out.printf("%s successfully updated from Customer table", customerID);
            }
        }
    }

    /**
     * this method updates the customers address with a message is the query is successful
     * @param customerID The id of the customer
     * @param address The address to update
     * @throws SQLException e
     */
    public void updateCustomerAddress(int customerID, String address) throws SQLException {
        String sql = "UPDATE Customer SET address = ? WHERE customerID = ?";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, address);
            stmt.setObject(2, customerID);
            var result = stmt.executeUpdate();
            if (result > 0 ) {
                System.out.printf("%s successfully updated from Customer table", customerID);
            }
        }
    }


    /**
     * Returns a customer record with all the information from a row in the customer table
     * @param customerID The id of the customer
     * @return The record of information
     * @throws SQLException e
     */
    public CustomerRecord viewCustomer(int customerID)throws SQLException{
        String sql = "SELECT firstName, lastName, address, registrationPhotoIDNumber FROM Customer WHERE customerID = ?";
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            var stmt = conn.prepareStatement(sql);
            stmt.setObject(1, customerID);
            var rs =stmt.executeQuery();
            if(rs.next()){
                return new CustomerRecord(rs.getString("firstName"), rs.getString("lastName"), rs.getString("address"), rs.getString("registrationPhotoIDNumber"));
            }
            return null;
        }
    }


    }











