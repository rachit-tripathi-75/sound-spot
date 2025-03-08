package com.rachit.tripathi75.soundspot.model;

public record SearchListItem(
        String id,
        String title,
        String subtitle,
        String coverImage,
        Type type
) {
    public static enum Type {
        SONG,
        ALBUM,
        PLAYLIST,
        ARTIST
    }
}
