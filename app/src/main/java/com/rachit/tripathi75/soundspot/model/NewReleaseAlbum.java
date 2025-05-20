package com.rachit.tripathi75.soundspot.model;

public class NewReleaseAlbum {
    String title;
    String artist;
    int imageResId;
    int colorResId;

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getColorResId() {
        return colorResId;
    }

    public NewReleaseAlbum(String title, String artist, int imageResId, int colorResId) {
        this.title = title;
        this.artist = artist;
        this.imageResId = imageResId;
        this.colorResId = colorResId;
    }
}