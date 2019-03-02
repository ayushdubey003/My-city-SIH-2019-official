package in.gov.sih.mycity;

public class Attraction {
    private String title, imageURL, description;

    public Attraction(String title, String imageURL, String description){
        this.title = title;
        this.imageURL = imageURL;
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}