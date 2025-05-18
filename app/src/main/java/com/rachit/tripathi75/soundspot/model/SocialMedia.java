package com.rachit.tripathi75.soundspot.model;

// SocialMedia.java


public class SocialMedia {
    private String platform;
    private String url;
    private String handle;

    public SocialMedia(String platform, String url, String handle) {
        this.platform = platform;
        this.url = url;
        this.handle = handle;
    }

    // Getters and setters
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }
}