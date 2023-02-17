package com.example.flickrfinder.model

data class PhotosResponse(
    val photos: Photos,
    val stat: String
)

data class Photos(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val photo: List<Photo>,
    val total: Int
)

data class Photo(
    val height_h: Int?,
    val title: String?,
    val url_h: String?,
    val width_h: Int?
)