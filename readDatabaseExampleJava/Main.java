import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {

            String query = "USE aetheria;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();

            HashMap<String, LinkedList<String[]>> records = separateEntities("C:\\Users\\benia\\PycharmProjects\\databases\\mystic_quest\\generated_entities.txt");
            HashMap<String, LinkedList<String[]>> relations = separateRelations("C:\\Users\\benia\\PycharmProjects\\databases\\mystic_quest\\generated_events.txt");
            String[] TableNames = new String[] {"Event", "Guild", "Enemy", "NPC", "Dialogue", "Item", "Team", "Player"};
            String[] relationNames = new String[] {
                    "item_with_npc", "npc_with_dialogue",
                    "player_with_enemy", "player_with_guild",
                    "player_with_team", "player_with_npc"
            };

            for (String table : TableNames) {
                LinkedList<String[]> entities = records.get(table);
                for (String[] entity : entities) {
                    try {
                        DataToDatabase.insertData(connection, table, entity);
                    } catch (SQLIntegrityConstraintViolationException e) {

                    }
                }
            }

            for (String table : relationNames) {
                LinkedList<String[]> entities = relations.get(table);
                for (String[] entity : entities) {
                    try {
                        DataToDatabase.insertData(connection, table, entity);
                    } catch (SQLIntegrityConstraintViolationException ignored) {}
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<String, LinkedList<String[]>> separateEntities(String path) {
        HashMap<String, String> DataToTable = new HashMap<>();
        HashMap<String, Integer> TableNOValues = new HashMap<>();
        HashMap<String, LinkedList<String[]>> records = new HashMap<>();
        String[][] DataToTableValues = new String[][] {
                {"questions", "Dialogue"},
                {"GuildName","Guild"},
                {"Character", "Player"},
                {"Item", "Item"},
                {"Enemy", "Enemy"},
                {"Team", "Team"},
                {"Event", "Event"},
                {"NPC", "NPC"},
                {"Vendors", "NPC"}
        };
        String[][] TableNOValuesValues = new String[][] {
                {"Dialogue", "7"},
                {"Guild", "4"},
                {"Player", "11"},
                {"Item", "10"},
                {"Enemy", "6"},
                {"Team", "3"},
                {"Event", "4"},
                {"NPC", "5"}
        };
        String[] TableNames = new String[] {"Dialogue", "Guild", "Player", "Item", "Enemy", "Team", "Event", "NPC"};

        for (String[] values : DataToTableValues) {
            DataToTable.put(values[0],values[1]);
        }
        for (String[] values : TableNOValuesValues) {
            TableNOValues.put(values[0], Integer.valueOf(values[1]));
        }
        for (String table : TableNames) {
            records.put(table, new LinkedList<>());
        }

        File file = new File(path);
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String ObjectName = "";
        String[] values = new String[0];
        boolean hasEnteredValues = false;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.isEmpty() && line.charAt(0) == '-') {
                ObjectName = DataToTable.get(line.split(" ")[1]);
                values = new String[TableNOValues.get(ObjectName)];
                hasEnteredValues = false;
            } else if (!line.isEmpty() && line.charAt(0) == '"') {
                line = line.replaceAll("\"", "");
                line = line.replaceAll("'", "`");
                String[] parts = line.split("=");
                int index = getIndex(ObjectName, parts[0]);
                if (index != -1) {
                    if (values[index] != null) values[index] = values[index] + parts[1];
                    else values[index] = parts[1];
                    hasEnteredValues = true;
                }
            } else if (hasEnteredValues) {
                records.get(ObjectName).add(values);
                hasEnteredValues = false;
            }
        }
        if (hasEnteredValues) {
            records.get(ObjectName).add(values);
        }

        scanner.close();
        return records;
    }

    public static HashMap<String, LinkedList<String[]>> separateRelations(String path) {
        HashMap<String, LinkedList<String[]>> records = new HashMap<>();
        String[] relationNames = new String[] {
                "item_with_npc", "npc_with_dialogue",
                "player_with_enemy", "player_with_guild",
                "player_with_team", "player_with_npc"
        };
        for (String table : relationNames) records.put(table, new LinkedList<>());
        File file = new File(path);
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String ObjectName = scanner.nextLine().split(" ")[2];
        String[] values = new String[4];
        if (ObjectName.equals("player_with_npc")) values = new String[6];
        int index = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.charAt(0) == '[') {
                String value = "";
                if (line.contains("{")) {
                    if (index == 5) value = line.split(" ")[3];
                    else value = line.split(" ")[2];
                    value = value.replace(",","");
                } else {
                    String[] words = line.split(" ");
                    int addition = 0;
                    if (index == 4) addition = 2;
                    StringBuilder builder = new StringBuilder(words[1+addition]);
                    for (int i = 2 + addition; i < words.length; i++) {
                        builder.append(" ");
                        builder.append(words[i]);
                    }
                    value = builder.toString();
                }
                values[index++] = value;
            } else {
                if (scanner.hasNextLine()) {
                    records.get(ObjectName).add(values);
                    ObjectName = scanner.nextLine().split(" ")[2];
                    values = new String[4];
                    if (ObjectName.equals("player_with_npc")) values = new String[6];
                    index = 0;
                }
            }
        }

        scanner.close();
        return records;
    }

    public static int getIndex(String TableName, String ValueName) {
        if (TableName.equals("Dialogue")) {
            if (ValueName.equals("id")) return 0;
            else if (ValueName.equals("content")) return 1;
            else if (ValueName.equals("response1")) return 2;
            else if (ValueName.equals("response2")) return 4;
            else if (ValueName.equals("emotion")) return 6;
        } else if (TableName.equals("Guild")) {
            if (ValueName.equals("id")) return 0;
            else if (ValueName.equals("name")) return 1;
            else if (ValueName.equals("description")) return 2;
            else if (ValueName.equals("founded_year")) return 3;
        } else if (TableName.equals("Player")) {
            if (ValueName.equals("id")) return 0;
            else if (ValueName.equals("first_name")) return 1;
            else if (ValueName.equals("firstname")) return 1;
            else if (ValueName.equals("race")) return 2;
            else if (ValueName.equals("class")) return 3;
            else if (ValueName.equals("last_login")) return 10;
        } else if (TableName.equals("Item")) {
            if (ValueName.equals("id")) return 0;
            else if (ValueName.equals("item_name")) return 1;
            else if (ValueName.equals("item_type")) return 2;
        } else if (TableName.equals("Enemy")) {
            if (ValueName.equals("id")) return 0;
            else if (ValueName.equals("enemy_name")) return 1;
            else if (ValueName.equals("enemy_type")) return 2;
            else if (ValueName.equals("hitpoints")) return 4;
            else if (ValueName.equals("warcry")) return 5;
        } else if (TableName.equals("Team")) {
            if (ValueName.equals("id")) return 0;
            else if (ValueName.equals("team_name")) return 1;
            else if (ValueName.equals("kingdom")) return 2;
        } else if (TableName.equals("Event")) {
            if (ValueName.equals("id")) return 0;
            else if (ValueName.equals("event_name")) return 1;
            else if (ValueName.equals("event_time")) return 3;
        } else if (TableName.equals("NPC")) {
            if (ValueName.equals("id")) return 0;
            else if (ValueName.equals("npc_type")) return 2;
            else if (ValueName.equals("first_name")) return 1;
            else if (ValueName.equals("last_name")) return 1;
            else if (ValueName.equals("location")) return 3;
        }
        System.out.println(TableName + " " + ValueName);
        return -1;
    }
}
