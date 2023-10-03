package aetheriaDB;

import java.util.HashMap;
import java.util.Queue;

public class PrimaryKeyGetter {
    public static HashMap<String, Queue<Integer>> map = new HashMap<String, Queue<Integer>>();

    public static void fillMap() {

    }
    
    public static int getNotUsedKey(String entity) {
        Queue<Integer> queue = map.get(entity);
        int[] values = new int[10000];
        for (int i = 0; i < values.length; i++) {
            values[i] = i;
        }

        if (queue == null || queue.isEmpty()) {
            // get a list of values
            // create a new queue
        }
        queue = map.get(entity);
        return queue.poll();
    }
}
