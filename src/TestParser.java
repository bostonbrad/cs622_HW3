
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class TestParser {

    public static void main(String[] args) {
        try {
            // Crucial: Set the entity limit mentioned in the DBLP FAQ
            System.setProperty("entityExpansionLimit", "2000000");

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                int count = 0;

                public void startElement(String uri, String localName, String qName, Attributes attributes) {
                    if (qName.equalsIgnoreCase("title")) {
                        count++;
                    }
                }

                public void endDocument() {
                    System.out.println("Success! Found " + count + " titles.");
                }
            };

            saxParser.parse("data/dblp_small.xml", handler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
