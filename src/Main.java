
import java.io.File;
import java.util.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Main {

    public static List<Article> parseDblp(String filePath) {
        try {
            // 1. Create the Factory
            SAXParserFactory factory = SAXParserFactory.newInstance();

            // 2. Create the Parser
            SAXParser saxParser = factory.newSAXParser();

            // 3. Create your Handler (which now holds the list)
            DblpHandler handler = new DblpHandler();

            // 4. Run the Parser
            File xmlFile = new File(filePath);
            saxParser.parse(xmlFile, handler);

            // 5. Return the list that the handler built
            return handler.getArticles();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        // A. Set the security limit for DBLP entities
        System.setProperty("entityExpansionLimit", "2000000");

        // B. Parse the XML and store articles in a simple List first
        // This makes benchmarking much easier than re-parsing the file 100 times.
        List<Article> allArticles = parseDblp("data/dblp_small.xml");
        System.out.println("Number of Articles: " + allArticles.size());

        String[] keywords = {"Database", "Chat", "Query"};

        for (String keyword : keywords) {
            System.out.println("\n==============================");
            System.out.println("Search results for keyword: " + keyword);
            System.out.println("Using Brute Force Search:");

            // --- This block has been updated as suggested:
            for (Article a : allArticles) {
                String title = a.getTitle();
                if (title != null && title.toLowerCase().contains(keyword.toLowerCase())) {
                    System.out.println(" - " + a.getId() + ": " + title);
                }
            }
            // ---

            System.out.println();
            System.out.println("Using Inverted Index Search:");
            InvertedIndex invIndex = new InvertedIndex();
            for (Article a : allArticles) {
                invIndex.addDocument(a.getId(), a.getTitle());
            }
            Set<Integer> results = invIndex.search(keyword);
            for (Integer id : results) {
                Article a = allArticles.get(id);
                System.out.println(" - " + id + ": " + a.getTitle());
            }
        }

        // C. The Benchmarking Loop (for Task 3)
        int[] articleCounts = {1, 5, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        String keyword = "database";

        System.out.println("Articles | Brute Force (ns) | Inverted Index (ns)");

        for (int count : articleCounts) {
            // Get a sublist of the data
            List<Article> subset = allArticles.subList(0, Math.min(count, allArticles.size()));

            // 1. Build the Inverted Index for this subset
            InvertedIndex invIndex = new InvertedIndex();
            for (Article a : subset) {
                invIndex.addDocument(a.getId(), a.getTitle());
            }

            // 2. Measure Brute Force Search
            long startBF = System.nanoTime();
            for (Article a : subset) {
                if (a.getTitle().toLowerCase().contains(keyword)) {
                    // match found (do nothing, just measuring time)
                }
            }
            long endBF = System.nanoTime();

            // 3. Measure Inverted Index Search
            long startInv = System.nanoTime();
            invIndex.search(keyword);
            long endInv = System.nanoTime();

            System.out.printf("%8d | %16d | %18d\n", count, (endBF - startBF), (endInv - startInv));
        }
    }
}
