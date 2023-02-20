package com.example.flickrfinder.respository

import com.example.flickrfinder.model.PhotosResponse
import com.skydoves.sandwich.ApiResponse

class TestRepository : PhotoRepository {

    override suspend fun getPhotos(searchText: String, nextPage: Boolean): ApiResponse<PhotosResponse> {
        TODO("Not yet implemented")
    }

}