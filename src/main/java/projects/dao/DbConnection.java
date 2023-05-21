package projects.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import projects.exception.DbException;

import javax.swing.*;

public class DbConnection {
    // Constants for database connection details
    private static String HOST = "localhost";
    private static String PASSWORD = "projects";
    private static int PORT = 3306;
    private static String SCHEMA = "projects";
    private static String USER = "projects";

    // Get a connection to the database
    public static Connection getConnection() {
        String uri = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s", HOST, PORT, SCHEMA, USER, PASSWORD);

        try{
            // Use the DriverManager class to get a connection to the database
            Connection conn = DriverManager.getConnection(uri);
            System.out.println("Connection to schema " + SCHEMA + " is successful.");
            return conn;
        } catch (SQLException e) {
            System.out.println("Unable to get connection at " + uri);
            System.out.println("SQLException: " + e.getMessage());
            throw new DbException("Unable to get connection at " + uri, e);
        }
    }
}
