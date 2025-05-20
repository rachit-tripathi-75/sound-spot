package com.rachit.tripathi75.soundspot.classes;


public class FollowingArtist {
    private String name;
    private int imageResourceId;

    public FollowingArtist(String name, int imageResourceId) {
        this.name = name;
        this.imageResourceId = imageResourceId;
    }

    public String getName() {
        return name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
