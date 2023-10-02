import java.util.HashMap;
import java.util.Stack;

public class PrimaryKeyGetter {
    public static HashMap<String, Stack<Integer>> map = new HashMap<String, Stack<Integer>>();
    public static int getNotUsedKey(String tableName) {
        Stack<Integer> stack = map.get(tableName);
        int[] values = new int[10000];
        for (int i = 0; i < values.length; i++) {
            values[i] = i;
        }

        if (stack == null || stack.isEmpty()) {
            // get a list of values
            // create a new stack
        }
        stack = map.get(tableName);
        return stack.pop();
    }
}
