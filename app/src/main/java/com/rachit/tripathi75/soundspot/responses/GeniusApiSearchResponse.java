package com.rachit.tripathi75.soundspot.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeniusApiSearchResponse {
    @SerializedName("response")
    private HitsResponse response;

    public HitsResponse getResponse() {
        return response;
    }

    public class HitsResponse {
        @SerializedName("hits")
        private List<Hit> hits;

        public List<Hit> getHits() {
            return hits;
        }
    }

    public class Hit {
        @SerializedName("result")
        private SongResult result;

        public SongResult getResult() {
            return result;
        }
    }


    public class SongResult {
        @SerializedName("full_title")
        private String fullTitle;

        @SerializedName("title")
        private String title;

        @SerializedName("url")
        private String url;

        @SerializedName("header_image_thumbnail_url")
        private String thumbnail;

        public String getFullTitle() {
            return fullTitle;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() { // ye lyrics ka path return karega, jisko we'll have to scrap later on.....
            return url;
        }

        public String getThumbnail() {
            return thumbnail;
        }
    }



}
