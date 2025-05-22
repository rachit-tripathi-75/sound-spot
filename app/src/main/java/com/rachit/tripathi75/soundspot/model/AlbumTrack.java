package com.rachit.tripathi75.soundspot.model;


public class AlbumTrack {
    private int number;
    private String title;
    private String artist;

    public AlbumTrack(int number, String title, String artist) {
        this.number = number;
        this.title = title;
        this.artist = artist;
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }
}