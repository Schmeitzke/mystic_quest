package aetheriaDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ConnectToDB {
    private static final Logger logger = Logger.getLogger(ConnectToDB.class.getName());
    private Connection connection = null;

    public ConnectToDB(String DATABASE_URL, String USERNAME, String PASSWORD) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);

            if (connection != null) {
                System.out.println("Connected to the database successfully.");
            } else {
                System.err.println("Failed to connect to the database.");
            }
        } catch (ClassNotFoundException e) {
            logger.severe("Error: MySQL JDBC driver not found.");
            logger.throwing(getClass().getName(), "ConnectToDB constructor", e);
        } catch (SQLException e) {
            logger.severe("Error: Unable to connect to the database.");
            logger.throwing(getClass().getName(), "ConnectToDB constructor", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                logger.severe("Error: Unable to close the connection.");
                logger.throwing(getClass().getName(), "closeConnection", e);
            }
        }
    }
}

