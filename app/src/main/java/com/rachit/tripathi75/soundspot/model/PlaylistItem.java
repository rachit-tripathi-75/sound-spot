package com.rachit.tripathi75.soundspot.model;

public class PlaylistItem {
    String title;
    String description;
    int imageResId;
    String gradientColor; // This would be a resource in a real app

    public PlaylistItem(String title, String description, int imageResId, String gradientColor) {
        this.title = title;
        this.description = description;
        this.imageResId = imageResId;
        this.gradientColor = gradientColor;
    }
}
