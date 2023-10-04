package aetheriaDB;

import java.util.*;

public class PrimaryKeyGetter {
    private static final HashMap<String, Queue<Integer>> map = new HashMap<>();

    public static void fillMap(String[] TableNames, HashMap<String, LinkedList<String[]>> records) {
        for (String table : TableNames) {
            Queue<Integer> keys = new LinkedList<>(); // Queue which will hold available primary keys
            Set<Integer> existingKeys = new HashSet<>(); // Set to store existing primary keys

            LinkedList<String[]> tableRecords = records.get(table);
            if (tableRecords != null) {
                for (String[] record : tableRecords) {
                    // Assuming the key is in the first column, convert it to an integer and add it to the set
                    int key = Integer.parseInt(record[0]);
                    existingKeys.add(key);
                }
            }

            // Add the non-existing keys to the queue
            for (int i = 1; i <= 10000; i++) {
                if (!existingKeys.contains(i)) {
                    keys.add(i);
                }
            }

            map.put(table, keys);
        }
    }

    public static int getNotUsedKey(String table) {
        Queue<Integer> queue = map.get(table);
        if (queue != null) {
            Integer value = queue.poll();
            if (value != null) {
                return value;
            } else {
                throw new RuntimeException("No primary keys left in " + table);
            }
        } else {
            throw new RuntimeException("Table " + table + " does not exist or is empty");
        }
    }
}











