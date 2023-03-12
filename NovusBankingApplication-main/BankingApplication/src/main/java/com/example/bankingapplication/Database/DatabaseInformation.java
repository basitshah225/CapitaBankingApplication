package com.example.bankingapplication.Database;

/**
 * Class which contains which database to use
 */
public class DatabaseInformation {

    /**
     * @return The url of the database
     */
    public String getUrl() {
        return "jdbc:sqlserver://SQL8002.site4now.net;database=db_a8cc79_Leylor";
    }

    /**
     * @return The username for the database
     */
    public String getUsernameDatabase() {
        return "db_a8cc79_Leylor_admin";
    }


    /**
     * @return The password for the database
     */
    public String getPasswordDatabase() {
        return "Sm13zW16dA62";
    }
}
