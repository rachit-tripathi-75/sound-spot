package com.rachit.tripathi75.soundspot.model;

// Clip.java


public class Clip {
    private String id;
    private String title;
    private String artistId;
    private String artistName;
    private String thumbnailUrl;
    private String videoUrl;
    private int durationMs;
    private long viewCount;

    public Clip(String id, String title, String artistId, String artistName,
                String thumbnailUrl, String videoUrl, int durationMs, long viewCount) {
        this.id = id;
        this.title = title;
        this.artistId = artistId;
        this.artistName = artistName;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.durationMs = durationMs;
        this.viewCount = viewCount;
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

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    // Helper methods
    public String getFormattedDuration() {
        int seconds = durationMs / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public String getFormattedViewCount() {
        if (viewCount < 1000) {
            return String.valueOf(viewCount);
        } else if (viewCount < 1000000) {
            return String.format("%.1fK", viewCount / 1000.0);
        } else {
            return String.format("%.1fM", viewCount / 1000000.0);
        }
    }
}