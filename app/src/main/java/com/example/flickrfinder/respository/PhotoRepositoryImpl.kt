package com.example.flickrfinder.respository

import com.example.flickrfinder.model.PhotosResponse
import com.example.flickrfinder.networkclient.ApiCall
import com.example.flickrfinder.networkclient.ApiClient
import com.skydoves.sandwich.*

class PhotoRepositoryImpl : PhotoRepository {

    private val networkApiClient = ApiClient.getRoomClient().create(ApiCall::class.java)
    private val API_KEY = ""      // TODO keep this safe
    private val PER_PAGE_LIMIT = "25"
    private var currentPage = 1

    override suspend fun getPhotos(searchText: String, nextPage: Boolean): ApiResponse<PhotosResponse> {
        currentPage = if (nextPage) (currentPage + 1) else 1
        return networkApiClient.requestPhotos(getOptions(searchText))
    }

    private fun getOptions(searchText: String): Map<String, String> {
        val dataMap: MutableMap<String, String> = HashMap()
        dataMap["method"] = "flickr.photos.search"
        dataMap["format"] = "json"
        dataMap["nojsoncallback"] = "1"     // returns raw json with no function wrapper
        dataMap["text"] = searchText
        dataMap["extras"] = "url_n"         // image size: Small (approx. width="320" height="240")
        dataMap["per_page"] = PER_PAGE_LIMIT
        dataMap["page"] = currentPage.toString()
        dataMap["api_key"] = API_KEY
        return dataMap
    }

    companion object {
        const val BASE_LOGIN_URL = "https://api.flickr.com/services/"
    }

}