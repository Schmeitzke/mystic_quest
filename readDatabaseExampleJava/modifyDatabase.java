package aetheriaDB;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class modifyDatabase {

    private static final Logger logger = Logger.getLogger(modifyDatabase.class.getName());

    public static void main(String[] args) {
        readFromDatabase();
        insertQuest(6, "Test", "Test", "Test");
        readFromDatabase();
    }

    public static void readFromDatabase() {
        connectToDB connectToDB = new connectToDB("jdbc:mysql://localhost:3306/Aetheriadb", "root", "Camiel19"); // FILL IN THE USERNAME AND PASSWORD
        Connection connection = connectToDB.getConnection();

        if (connection != null) {
            String sqlQuery = null;
            try {
                Statement statement = connection.createStatement();

                sqlQuery = "SELECT * FROM quest"; // FILL IN THE TABLE NAME
                ResultSet resultSet = statement.executeQuery(sqlQuery);

                while (resultSet.next()) { // FILL IN THE COLUMN NAMES
                    int column1Value = resultSet.getInt("QuestID");
                    String column2Value = resultSet.getString("Name");
                    String column3Value = resultSet.getString("Type");
                    String column4Value = resultSet.getString("Description");

                    System.out.println("ID: " + column1Value + ", Name: " + column2Value + ", Type: " + column3Value + ", Description: " + column4Value);
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

    public static void insertQuest(int questID, String name, String type, String description) {
        connectToDB connectToDB = new connectToDB("jdbc:mysql://localhost:3306/Aetheriadb", "root", "Camiel19");
        Connection connection = connectToDB.getConnection();

        if (connection != null) {
            try {
                // Create an SQL INSERT statement
                String sqlInsert = "INSERT INTO quest (QuestID, Name, Type, Description) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
                preparedStatement.setInt(1, questID);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, type);
                preparedStatement.setString(4, description);

                // Execute the INSERT statement
                int rowsInserted = preparedStatement.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Quest inserted successfully.");
                } else {
                    System.err.println("Failed to insert the quest.");
                }

                // Close the statement and connection
                preparedStatement.close();
                connectToDB.closeConnection();
            } catch (SQLException e) {
                logger.severe("Error: Unable to insert the quest.");
                logger.severe("SQL State: " + e.getSQLState());
                logger.severe("Error Code: " + e.getErrorCode());
                logger.severe("SQL Message: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("Failed to connect to the database.");
        }
    }


}

