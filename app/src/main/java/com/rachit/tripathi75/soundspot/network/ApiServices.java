package com.rachit.tripathi75.soundspot.network;

import com.rachit.tripathi75.soundspot.responses.GeniusApiSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class ApiServices {
    public static interface GeniusLyricsApiService {

        @GET("search")
        Call<GeniusApiSearchResponse> searchLyrics(
                @Header("Authorization") String authorization,
                @Header("Accept") String accept,
                @Query("q") String query
        );
    }
}
