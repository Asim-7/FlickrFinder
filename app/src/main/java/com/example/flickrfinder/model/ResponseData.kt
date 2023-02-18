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
    val height_n: Int?,
    val title: String?,
    val url_n: String?,
    val width_n: Int?
)