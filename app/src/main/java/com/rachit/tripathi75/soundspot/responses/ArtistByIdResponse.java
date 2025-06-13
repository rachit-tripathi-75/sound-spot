package com.rachit.tripathi75.soundspot.responses;

import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;

public class ArtistByIdResponse {
    private boolean success;
    Data data;

    public boolean getSuccess() {
        return success;
    }
    public Data getData() {
        return data;
    }

    public class Data {
        private String id;
        private String name;
        private String url;
        private String type;
        private float followerCount;
        private String fanCount;
        private String isVerified = null;
        private String dominantLanguage;
        private String dominantType;
        ArrayList< Object > bio = new ArrayList < Object > ();
        private String dob = null;
        private String fb = null;
        private String twitter = null;
        private String wiki = null;
        ArrayList < Object > availableLanguages = new ArrayList < Object > ();
        private boolean isRadioPresent;
        ArrayList <ImageData> image = new ArrayList <ImageData>();
        ArrayList < Object > topSongs = new ArrayList < Object > ();
        ArrayList < Object > topAlbums = new ArrayList < Object > ();
        ArrayList < Object > singles = new ArrayList < Object > ();
        ArrayList < Object > similarArtists = new ArrayList < Object > ();

        // Getter Methods

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public String getType() {
            return type;
        }

        public float getFollowerCount() {
            return followerCount;
        }

        public String getFanCount() {
            return fanCount;
        }

        public String getIsVerified() {
            return isVerified;
        }

        public String getDominantLanguage() {
            return dominantLanguage;
        }

        public String getDominantType() {
            return dominantType;
        }

        public String getDob() {
            return dob;
        }

        public String getFb() {
            return fb;
        }

        public String getTwitter() {
            return twitter;
        }

        public String getWiki() {
            return wiki;
        }

        public boolean getIsRadioPresent() {
            return isRadioPresent;
        }

        public ArrayList<ImageData> getImage() {
            return image;
        }

    }

    public class ImageData {
        private String quality;
        @SerializedName("url")
        private String imageUrl;

        public String getQuality() {
            return quality;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }


}
