package com.rachit.tripathi75.soundspot.model;

public class TrendingItem {
    String title;
    String artist;
    int imageResId;
    int colorResId;

    public TrendingItem(String title, String artist, int imageResId, int colorResId) {
        this.title = title;
        this.artist = artist;
        this.imageResId = imageResId;
        this.colorResId = colorResId;
    }
}