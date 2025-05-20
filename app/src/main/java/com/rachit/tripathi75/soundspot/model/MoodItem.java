package com.rachit.tripathi75.soundspot.model;

// Model classes
public class MoodItem {
    String title;
    String description;
    int imageResId;
    int colorResId;

    public MoodItem(String title, String description, int imageResId, int colorResId) {
        this.title = title;
        this.description = description;
        this.imageResId = imageResId;
        this.colorResId = colorResId;
    }
}