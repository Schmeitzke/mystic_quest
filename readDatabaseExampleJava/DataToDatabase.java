import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataToDatabase {
    public static void insertData(Connection connection, String table, String[] data) {
        try {
            fillNulls(data);
            StringBuilder values = new StringBuilder("(");
            for (int i = 0; i < data.length - 1; i++) {
                if (isString(data[i])) {
                    values.append("'");
                    values.append(data[i]);
                    values.append("'");
                } else {
                    values.append(data[i]);
                }
                values.append(", ");
            }
            if (isString(data[data.length - 1])) {
                values.append("'");
                values.append(data[data.length - 1]);
                values.append("'");
            } else {
                values.append(data[data.length - 1]);
            }
            values.append(");");

            String query = "INSERT INTO " + table + " VALUES " + values.toString();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isString(String data) {
        if (data == null || data.equals("NULL")) return false;
        try {
            Double.valueOf(data);
            return false;
        } catch (Exception e) {
            return true;
        }
    }
    public static void fillNulls(String[] data) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == null) data[i] = "NULL";
        }
    }
}