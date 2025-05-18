package com.rachit.tripathi75.soundspot.model;

// Album.java

import java.util.Date;
import java.util.List;

public class Album {
    private String id;
    private String title;
    private String artistId;
    private String artistName;
    private String coverUrl;
    private Date releaseDate;
    private List<Track> tracks;
    private int totalTracks;
    private String albumType; // album, single, EP

    public Album(String id, String title, String artistId, String artistName,
                 String coverUrl, Date releaseDate, List<Track> tracks,
                 int totalTracks, String albumType) {
        this.id = id;
        this.title = title;
        this.artistId = artistId;
        this.artistName = artistName;
        this.coverUrl = coverUrl;
        this.releaseDate = releaseDate;
        this.tracks = tracks;
        this.totalTracks = totalTracks;
        this.albumType = albumType;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public int getTotalTracks() {
        return totalTracks;
    }

    public void setTotalTracks(int totalTracks) {
        this.totalTracks = totalTracks;
    }

    public String getAlbumType() {
        return albumType;
    }

    public void setAlbumType(String albumType) {
        this.albumType = albumType;
    }

    // Helper methods
    public String getFormattedReleaseDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy");
        return sdf.format(releaseDate);
    }

    public String getFormattedAlbumType() {
        return albumType.substring(0, 1).toUpperCase() + albumType.substring(1);
    }
}