package com.example.soundspot

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("v3/b/65c12103dc74654018a0aec9")
    fun getAlbumData(): Call<MyData>

}