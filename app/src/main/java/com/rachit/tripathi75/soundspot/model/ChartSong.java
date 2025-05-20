package com.rachit.tripathi75.soundspot.model;

public class ChartSong {
    int rank;
    String title;
    String artist;
    int imageResId;

    public ChartSong(int rank, String title, String artist, int imageResId) {
        this.rank = rank;
        this.title = title;
        this.artist = artist;
        this.imageResId = imageResId;
    }
}
