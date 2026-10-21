
package org.yourcompany.yourproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection
{
    static String url = "jdbc:mysql://localhost:3306/cinebook";
    static String user = "root";

    public static Connection getConnection() throws SQLException
    {
        String password = System.getenv("CINEBOOK_DB_PASSWORD");

        if (password == null || password.isEmpty())
        {
            throw new SQLException(
                "Database password not set. Set CINEBOOK_DB_PASSWORD first."
            );
        }

        return DriverManager.getConnection(url, user, password);
    }
}
