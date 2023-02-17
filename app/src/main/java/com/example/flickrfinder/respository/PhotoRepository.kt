package com.example.flickrfinder.respository

import com.example.flickrfinder.model.PhotoData

interface PhotoRepository {
    fun getPhotos(): List<PhotoData>
}