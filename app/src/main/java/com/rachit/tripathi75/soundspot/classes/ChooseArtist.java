package com.rachit.tripathi75.soundspot.classes;

// Artist.java
// Artist.java
public class ChooseArtist {
    private String id;
    private String name;
    private String imageUrl;
    private boolean selected;
    private String relatedTo;

    public ChooseArtist(String id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.selected = false;
    }

    public ChooseArtist(String id, String name, String imageUrl, String relatedTo) {
        this(id, name, imageUrl);
        this.relatedTo = relatedTo;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
    public String getRelatedTo() { return relatedTo; }
}