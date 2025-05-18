package com.rachit.tripathi75.soundspot.classes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public class ApiServices {
    public interface GetLyricsApiService {
        @GET("v1/{artist}/{title}")
        Call<GetLyricsResponse> getLyrics(
                @Header("Content-Type") String ContentType,
                @Path("artist") String artist,
                @Path("title") String title
        );
    }
}
