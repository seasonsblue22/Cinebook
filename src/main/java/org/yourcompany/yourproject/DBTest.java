
package org.yourcompany.yourproject;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class DBTest
{
    public static void main(String[] args)
    {
        try
        {
            Connection con = DBConnection.getConnection();

            DatabaseMetaData md = con.getMetaData();

            System.out.println("MySQL Connected Successfully!");
            System.out.println("MySQL Version: " + md.getDatabaseProductVersion());

            con.close();
        }
        catch (SQLException e)
        {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}
