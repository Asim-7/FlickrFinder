package com.example.flickrfinder.respository

import com.example.flickrfinder.model.PhotosResponse
import com.skydoves.sandwich.ApiResponse

interface PhotoRepository {
    suspend fun getPhotos(searchText: String, nextPage: Boolean): ApiResponse<PhotosResponse>
}