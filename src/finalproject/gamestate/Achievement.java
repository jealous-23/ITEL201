package finalproject.gamestate;

public class Achievement {
    private final String id;
    private final String title;
    private final String description;

    public Achievement(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
  
    }
 
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}
