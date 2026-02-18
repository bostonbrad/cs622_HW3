
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.List;

public class LuceneIndex {

    private Directory memoryIndex;
    private StandardAnalyzer analyzer;

    public LuceneIndex() {
        // We use RAM (ByteBuffersDirectory) because it's faster for homework
        // and doesn't leave junk files on your hard drive.
        this.memoryIndex = new ByteBuffersDirectory();
        this.analyzer = new StandardAnalyzer();
    }

    public void addArticles(List<Article> articles) {
        try {
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            IndexWriter writer = new IndexWriter(memoryIndex, indexWriterConfig);

            for (Article a : articles) {
                Document document = new Document();
                // We index the title. Field.Store.YES means we can retrieve the text later.
                document.add(new TextField("title", a.getTitle(), Field.Store.YES));
                writer.addDocument(document);
            }

            writer.close(); // COMMIT the changes
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void search(String queryStr) {
        try {
            // 1. Prepare the query
            Query q = new QueryParser("title", analyzer).parse(queryStr);

            // 2. Create the searcher
            DirectoryReader reader = DirectoryReader.open(memoryIndex);
            IndexSearcher searcher = new IndexSearcher(reader);

            // 3. Execute search (get top 10 results)
            TopDocs docs = searcher.search(q, 10);

            // 4. (Optional) Print results to prove it works
            // System.out.println("Lucene found " + docs.totalHits + " hits for " + queryStr);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
