
public class Article {

    private String title;
    private int id; // Optional, useful for the SkipList later

    // Generate Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Article{title='" + title + "'}";
    }
}
