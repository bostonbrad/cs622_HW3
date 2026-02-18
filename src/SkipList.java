import java.util.ArrayList;
import java.util.List; // Import List
import java.util.Random;

public class SkipList {
    private static final int MAX_LEVEL = 16; // Increased from 4 to handle more data efficiently
    private SkipListNode head;
    private int level;
    private Random random;

    public SkipList() {
        head = new SkipListNode(-1, MAX_LEVEL); 
        level = 0;
        random = new Random();
    }

    private int randomLevel() {
        int newLevel = 0;
        while (newLevel < MAX_LEVEL && random.nextBoolean()) {
            newLevel++;
        }
        return newLevel;
    }

    public void insert(int value) {
        SkipListNode[] update = new SkipListNode[MAX_LEVEL + 1];
        SkipListNode current = head;
        
        for (int i = level; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].value < value) {
                current = current.forward[i];
            }
            update[i] = current;
        }
        
        // Check for duplicates (optional, but good for indices)
        current = current.forward[0];
        if (current != null && current.value == value) {
            return; 
        }

        int newLevel = randomLevel();
        if (newLevel > level) {
            for (int i = level + 1; i <= newLevel; i++) {
                update[i] = head;
            }
            level = newLevel;
        }

        SkipListNode newNode = new SkipListNode(value, newLevel);
        for (int i = 0; i <= newLevel; i++) {
            newNode.forward[i] = update[i].forward[i];
            update[i].forward[i] = newNode;
        }
        // REMOVED System.out.println here!
    }

    public boolean search(int value) {
        SkipListNode current = head;
        for (int i = level; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].value < value) {
                current = current.forward[i];
            }
        }
        current = current.forward[0];
        return current != null && current.value == value;
    }

    // NEW METHOD: Return all IDs in the list (for search results)
    public List<Integer> toList() {
        List<Integer> result = new ArrayList<>();
        SkipListNode current = head.forward[0]; // Start at bottom level
        while (current != null) {
            result.add(current.value);
            current = current.forward[0];
        }
        return result;
    }
}