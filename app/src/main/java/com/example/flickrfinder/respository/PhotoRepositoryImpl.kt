package com.example.flickrfinder.respository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.flickrfinder.model.PhotosResponse
import com.example.flickrfinder.networkclient.ApiCall
import com.example.flickrfinder.networkclient.ApiClient
import com.example.flickrfinder.util.Constants.API_KEY
import com.example.flickrfinder.util.Constants.PER_PAGE_LIMIT
import com.skydoves.sandwich.*
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(private val context: Context) : PhotoRepository {

    private val networkApiClient = ApiClient.getRoomClient().create(ApiCall::class.java)
    private var currentPage = 1

    override suspend fun getPhotos(searchText: String, nextPage: Boolean): ApiResponse<PhotosResponse> {
        currentPage = if (nextPage) (currentPage + 1) else 1
        return networkApiClient.requestPhotos(getOptions(searchText))
    }

    override fun isNetworkConnected(): Boolean {
        return context.currentConnectivityState
    }

    /** Network utility to get current state of internet connection */
    private val Context.currentConnectivityState: Boolean
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return getCurrentConnectivityState(connectivityManager)
        }

    private fun getCurrentConnectivityState(
        connectivityManager: ConnectivityManager
    ): Boolean {
        val connected = connectivityManager.allNetworks.any { network ->
            connectivityManager.getNetworkCapabilities(network)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                ?: false
        }

        return connected
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