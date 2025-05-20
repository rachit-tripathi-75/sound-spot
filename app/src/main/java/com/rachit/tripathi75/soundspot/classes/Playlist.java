package com.rachit.tripathi75.soundspot.classes;


public class Playlist {
    private String title;
    private String artist;
    private String albumArtUrl;

    public Playlist(String title, String artist, String albumArtUrl) {
        this.title = title;
        this.artist = artist;
        this.albumArtUrl = albumArtUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbumArtUrl() {
        return albumArtUrl;
    }
}
