
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class DblpHandler extends DefaultHandler {

    private List<Article> articles = new ArrayList<>();
    private Article currentArticle;
    private StringBuilder elementValue;

    // 1. Add a flag to track if we are inside a title
    private boolean insideTitle = false;

    public List<Article> getArticles() {
        return articles;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        // 2. ONLY reset the buffer if we are starting a NEW field, NOT a nested tag like <i>
        if (!insideTitle) {
            elementValue = new StringBuilder();
        }

        if (qName.equalsIgnoreCase("article") || qName.equalsIgnoreCase("inproceedings")) {
            currentArticle = new Article();
            // If you want the ID, grab it here: attributes.getValue("key");
        } else if (qName.equalsIgnoreCase("title")) {
            insideTitle = true; // Turn on the flag
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        // Safe to just append everything. We filter what we keep in endElement.
        if (elementValue != null) {
            elementValue.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (currentArticle != null) {
            if (qName.equalsIgnoreCase("title")) {
                currentArticle.setTitle(elementValue.toString().trim());
                insideTitle = false; // 3. Turn off the flag
            } else if (qName.equalsIgnoreCase("article") || qName.equalsIgnoreCase("inproceedings")) {
                articles.add(currentArticle);
            }
        }
    }
}
