
import java.util.*;

public class InvertedIndex {
// Maps a term (word) to a set of document IDs where it appears

    private Map<String, Set<Integer>> index;

    public InvertedIndex() {
        this.index = new HashMap<>();
    }
// Tokenizes the input text into words, can be improved with more advanced tokenization

    private List<String> tokenize(String text) {
// Simple tokenization: splitting by non-word characters (space, punctuation)
        return Arrays.asList(text.toLowerCase().split("\\W+"));
    }
// Adds a document (text) with a unique document ID to the inverted index

    public void addDocument(int docId, String content) {
        List<String> words = tokenize(content);
        for (String word : words) {
            index.computeIfAbsent(word, k -> new HashSet<>()).add(docId);
        }
    }
// Search for a single term and return the list of document IDs where the term appears

    public Set<Integer> search(String term) {
        return index.getOrDefault(term.toLowerCase(), Collections.emptySet());
    }
// Search for multiple terms (AND search: returns documents containing all terms)

    public Set<Integer> searchMultipleTerms(List<String> terms) {
        Set<Integer> result = new HashSet<>();
        boolean firstTerm = true;
        for (String term : terms) {
            Set<Integer> termResults = search(term);
            if (firstTerm) {
                result.addAll(termResults); // Initialize with the first term's results
                firstTerm = false;
            } else {
                result.retainAll(termResults); // Intersection with the next term's results
            }
// Early exit if no documents match
            if (result.isEmpty()) {
                break;
            }
        }
        return result;
    }
// Print the index for debugging or visualization purposes

    public void printIndex() {
        for (Map.Entry<String, Set<Integer>> entry : index.entrySet()) {
            System.out.println("Term: " + entry.getKey() + " -> Documents: " + entry.getValue());
        }
    }

    public static void main(String[] args) {
// Create an inverted index
        InvertedIndex invertedIndex = new InvertedIndex();
// Add documents to the index
        invertedIndex.addDocument(1, "The quick brown fox jumps over the lazy dog");
        invertedIndex.addDocument(2, "The quick brown fox was very fast");
        invertedIndex.addDocument(3, "The lazy dog was sleeping all day");
        invertedIndex.addDocument(4, "Fast foxes and lazy dogs live in harmony");
// Print the index
        System.out.println("Inverted Index:");
        invertedIndex.printIndex();
// Search for a single term
        System.out.println("\nSearch results for 'fox': " + invertedIndex.search("fox"));
// Search for multiple terms
        System.out.println("\nSearch results for 'fox' AND 'lazy': " + invertedIndex.searchMultipleTerms(Arrays.asList("fox", "lazy")));
    }
}
