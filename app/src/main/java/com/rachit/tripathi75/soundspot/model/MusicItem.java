package com.rachit.tripathi75.soundspot.model;

// ye class, profile section me particular music data ki kya requirement hai, for displaying.... ke liye hai.....
public class MusicItem {
    private int id;
    private String title;
    private String type;
    private String artist;
    private int imageResourceId;

    public MusicItem(int id, String title, String type, String artist, int imageResourceId) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.artist = artist;
        this.imageResourceId = imageResourceId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getArtist() {
        return artist;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getSubtitle() {
        if (type.equals("Artist")) {
            return type;
        } else {
            return type + " • " + artist;
        }
    }
}