package com.rachit.tripathi75.soundspot.classes;

public class ImportedPlaylist {
    private final int id;
    private final String name;
    private final int songCount;

    public ImportedPlaylist(int id, String name, int songCount) {
        this.id = id;
        this.name = name;
        this.songCount = songCount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSongCount() {
        return songCount;
    }
}