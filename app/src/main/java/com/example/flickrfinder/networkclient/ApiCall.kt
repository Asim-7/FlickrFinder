package com.example.flickrfinder.networkclient

import com.example.flickrfinder.model.PhotosResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiCall {

    @GET("rest/")
    suspend fun requestPhotos(
        @QueryMap options: Map<String, String>
    ): ApiResponse<PhotosResponse>

}