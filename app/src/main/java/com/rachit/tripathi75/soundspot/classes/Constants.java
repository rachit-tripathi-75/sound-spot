package com.rachit.tripathi75.soundspot.classes;

public class Constants {

    // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   INSTAGRAM API %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    private static String CLIENT_ID = "9836705836405976";
    private static String REDIRECT_URI = "https://www.soundspot.com/";
    private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize" +
            "?client_id=" + CLIENT_ID +
            "&redirect_uri=" + REDIRECT_URI +
            "&response_type=code" +
            "&scope=user_profile,user_media";

}
