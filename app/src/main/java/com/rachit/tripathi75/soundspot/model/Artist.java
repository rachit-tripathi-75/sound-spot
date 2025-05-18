package com.rachit.tripathi75.soundspot.model;

// Artist.java

import java.util.List;

public class Artist {
    private String id;
    private String name;
    private String imageUrl;
    private long monthlyListeners;
    private boolean isFollowing;
    private String biography;
    private List<String> genres;
    private List<Award> awards;
    private List<SocialMedia> socialMedia;

    public Artist(String id, String name, String imageUrl, long monthlyListeners,
                  boolean isFollowing, String biography, List<String> genres,
                  List<Award> awards, List<SocialMedia> socialMedia) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.monthlyListeners = monthlyListeners;
        this.isFollowing = isFollowing;
        this.biography = biography;
        this.genres = genres;
        this.awards = awards;
        this.socialMedia = socialMedia;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getMonthlyListeners() {
        return monthlyListeners;
    }

    public void setMonthlyListeners(long monthlyListeners) {
        this.monthlyListeners = monthlyListeners;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<Award> getAwards() {
        return awards;
    }

    public void setAwards(List<Award> awards) {
        this.awards = awards;
    }

    public List<SocialMedia> getSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(List<SocialMedia> socialMedia) {
        this.socialMedia = socialMedia;
    }

    // Helper method to format monthly listeners
    public String getFormattedMonthlyListeners() {
        if (monthlyListeners < 1000) {
            return String.valueOf(monthlyListeners);
        } else if (monthlyListeners < 1000000) {
            return String.format("%.1fK", monthlyListeners / 1000.0);
        } else {
            return String.format("%.1fM", monthlyListeners / 1000000.0);
        }
    }
}