package com.rachit.tripathi75.soundspot.model;

public class NewReleaseSong {
    public static int id;
    public static String title;
    public static String artist;
    public static int imageResId;

    public NewReleaseSong(int id, String title, String artist, int imageResId) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.imageResId = imageResId;
    }
}