package aetheriaDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InsertData {

    private static final Logger logger = Logger.getLogger(InsertData.class.getName());

    public static void main(String[] args) {
        readFromDatabase();
    }

    public static void readFromDatabase() {
        ConnectToDB connectToDB = new ConnectToDB("jdbc:mysql://localhost:3306/Aetheriadb", "*****", "*****"); // FILL IN THE USERNAME AND PASSWORD
        Connection connection = connectToDB.getConnection();

        if (connection != null) {
            String sqlQuery = null;
            try {
                Statement statement = connection.createStatement();

                sqlQuery = "SELECT * FROM quest"; // FILL IN THE TABLE NAME
                ResultSet resultSet = statement.executeQuery(sqlQuery);

                while (resultSet.next()) { // FILL IN THE COLUMN NAMES
                    int column1Value = resultSet.getInt("eventID");
                    String column2Value = resultSet.getString("Type");


                    System.out.println("ID: " + column1Value + ", Type: " + column2Value);
                }

                resultSet.close();
                statement.close();
                connectToDB.closeConnection();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error executing SQL query: " + sqlQuery, e);
            }
        } else {
            System.err.println("Failed to connect to the database.");
        }
    }
}

