package com.example.flickrfinder.networkclient

import com.example.flickrfinder.model.PhotosResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiCall {

    @GET("rest/")
    fun requestPhotos(
        @QueryMap options: Map<String, String>
    ): Call<PhotosResponse>

}