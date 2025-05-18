package com.rachit.tripathi75.soundspot.model;

// Track.java

public class Track {
    private String id;
    private String title;
    private String artistId;
    private String artistName;
    private String albumId;
    private String albumName;
    private String coverUrl;
    private int durationMs;
    private long playCount;
    private boolean isLiked;
    private String previewUrl;

    public Track(String id, String title, String artistId, String artistName,
                 String albumId, String albumName, String coverUrl,
                 int durationMs, long playCount, boolean isLiked, String previewUrl) {
        this.id = id;
        this.title = title;
        this.artistId = artistId;
        this.artistName = artistName;
        this.albumId = albumId;
        this.albumName = albumName;
        this.coverUrl = coverUrl;
        this.durationMs = durationMs;
        this.playCount = playCount;
        this.isLiked = isLiked;
        this.previewUrl = previewUrl;
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

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }

    public long getPlayCount() {
        return playCount;
    }

    public void setPlayCount(long playCount) {
        this.playCount = playCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    // Helper methods
    public String getFormattedDuration() {
        int seconds = durationMs / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public String getFormattedPlayCount() {
        if (playCount < 1000) {
            return String.valueOf(playCount);
        } else if (playCount < 1000000) {
            return String.format("%.1fK", playCount / 1000.0);
        } else {
            return String.format("%.1fM", playCount / 1000000.0);
        }
    }
}