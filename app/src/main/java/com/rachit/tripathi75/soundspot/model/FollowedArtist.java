package com.rachit.tripathi75.soundspot.model;

public class FollowedArtist {
    private String id, name, photoUri;

    public FollowedArtist() {
    }

    public FollowedArtist(String id, String name, String photoUri) {
        this.id = id;
        this.name = name;
        this.photoUri = photoUri;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUri() {
        return photoUri;
    }
}
