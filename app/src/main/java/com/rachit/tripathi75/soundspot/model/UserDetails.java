package com.rachit.tripathi75.soundspot.model;

import android.net.Uri;

public class UserDetails {

    // here id ka matlab clean email hai.......
    private String id, name, email, photoUri;

    public UserDetails() {
    }

    public UserDetails(String id, String name, String email, String photoUri) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.photoUri = photoUri;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        return photoUri;
    }
}
