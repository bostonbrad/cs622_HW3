import java.util.*;

public class SkipListIndex {
    // Maps a word (e.g., "database") to a SkipList of document IDs
    private Map<String, SkipList> index;

    public SkipListIndex() {
        this.index = new HashMap<>();
    }

    public void addDocument(int docId, String content) {
        if (content == null) return;
        
        // Tokenize: split by non-word characters
        String[] tokens = content.toLowerCase().split("\\W+");
        
        for (String token : tokens) {
            if (token.isEmpty()) continue;
            
            // 1. If word doesn't exist, create a new SkipList for it
            index.putIfAbsent(token, new SkipList());
            
            // 2. Insert the docId into that word's SkipList
            index.get(token).insert(docId);
        }
    }

    public List<Integer> search(String keyword) {
        SkipList list = index.get(keyword.toLowerCase());
        if (list == null) {
            return Collections.emptyList();
        }
        // Ensure your SkipList.java has the .toList() method we added earlier!
        return list.toList();
    }
}