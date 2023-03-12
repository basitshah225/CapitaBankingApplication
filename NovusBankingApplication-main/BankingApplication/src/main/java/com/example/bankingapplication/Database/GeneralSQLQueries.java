package com.example.bankingapplication.Database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class which handles generic sql calls
 */
public class GeneralSQLQueries {

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
    public GeneralSQLQueries() throws SQLException {
        DatabaseInformation dbi = new DatabaseInformation();
        url = dbi.getUrl();
        usernameDatabase = dbi.getUsernameDatabase();
        passwordDatabase = dbi.getPasswordDatabase();
    }


    /**
     * @return The date at the end of the year
     * @throws SQLException e
     */
    public Date getEndOfYearDate() throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, usernameDatabase, passwordDatabase)) {
            String sql4 = "SELECT DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1) AS EndOfYear;";
            var stmt4 = conn.prepareStatement(sql4);
            var result = stmt4.executeQuery();
            if (result.next()) {
                return result.getDate("endOfYear");
            }
        }
        return null;
    }


}
