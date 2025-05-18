package com.rachit.tripathi75.soundspot.classes;

import com.google.gson.annotations.SerializedName;

public class GetLyricsResponse {
    @SerializedName("lyrics")
    private String lyrics;

    public String getLyrics() {
        return lyrics;
    }

}
