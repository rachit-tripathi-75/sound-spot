package com.rachit.tripathi75.soundspot.classes;


public class Playlist {
    private String title;
    private String artist;
    private int imageResourceId;

    public Playlist(String title, String artist, int imageResourceId) {
        this.title = title;
        this.artist = artist;
        this.imageResourceId = imageResourceId;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
