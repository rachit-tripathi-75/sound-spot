package com.rachit.tripathi75.soundspot.model;

public class NewReleaseSong {
    private final String id;
    private final String title;
    private final String artist;
    private final String imageUrl;

    public NewReleaseSong(String id, String title, String artist, String imageUrl) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getImageUrl() { return imageUrl; }
}
