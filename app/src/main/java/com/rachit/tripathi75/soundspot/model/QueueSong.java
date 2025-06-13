package com.rachit.tripathi75.soundspot.model;


public class QueueSong {
    private String title;
    private String artist;
    private Double duration;
    private String albumArtUrl;
    private String views; // Can be null, so a String type is appropriate

    public QueueSong(String title, String artist, Double duration, String albumArtUrl) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.albumArtUrl = albumArtUrl;
        this.views = null;
    }

    public QueueSong(String title, String artist, Double duration, String albumArtUrl, String views) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.albumArtUrl = albumArtUrl;
        this.views = views;
    }

    // Getter methods (optional, but good practice for encapsulation)
    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public Double getDuration() {
        return duration;
    }

    public String getAlbumArtUrl() {
        return albumArtUrl;
    }

    public String getViews() {
        return views;
    }

}