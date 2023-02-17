package com.example.flickrfinder.respository

import android.content.Context
import android.util.Log
import com.example.flickrfinder.model.PhotosResponse
import com.example.flickrfinder.networkclient.ApiCall
import com.example.flickrfinder.networkclient.ApiClient
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(private val appContext: Context) : PhotoRepository {

    private val networkApiClient = ApiClient.getRoomClient().create(ApiCall::class.java)
    private val API_KEY = ""      // TODO keep this safe
    private val PER_PAGE_LIMIT = "25"
    private var currentPage = 1
    private var searchString = "nature"

    @OptIn(DelicateCoroutinesApi::class)
    override fun getPhotos() {
        GlobalScope.launch {
            try {
                networkApiClient
                    .requestPhotos(getOptions())
                    .enqueue(object : Callback<PhotosResponse> {
                        override fun onResponse(call: Call<PhotosResponse>, response: Response<PhotosResponse>) {
                            val responseData = response.body()!!.photos

                            Log.e("ASIM", "response: $responseData")
                        }

                        override fun onFailure(call: Call<PhotosResponse>, t: Throwable) {
                            Log.e("ASIM", "Failed: ${t.message}")
                        }
                    })
            } catch (e: Exception) {
                Log.e("ASIM", "Exception: $e")
            }
        }
    }

    private fun getOptions(): Map<String, String> {
        val dataMap: MutableMap<String, String> = HashMap()
        dataMap["method"] = "flickr.photos.search"
        dataMap["format"] = "json"
        dataMap["nojsoncallback"] = "1"     // returns raw json with no function wrapper
        dataMap["text"] = searchString
        dataMap["extras"] = "url_h"
        dataMap["per_page"] = PER_PAGE_LIMIT
        dataMap["page"] = currentPage.toString()
        dataMap["api_key"] = API_KEY
        return dataMap
    }

    companion object {
        const val BASE_LOGIN_URL = "https://api.flickr.com/services/"
    }

}