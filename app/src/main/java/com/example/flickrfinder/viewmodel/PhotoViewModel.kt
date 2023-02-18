package com.example.flickrfinder.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickrfinder.model.Photo
import com.example.flickrfinder.model.PhotoData
import com.example.flickrfinder.respository.PhotoRepository
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val repository: PhotoRepository,              // here the HelpRepository is an interface because it helps this view model to be tested with both DEFAULT and FAKE repository
) : ViewModel() {

    private var _photosList: MutableList<PhotoData> by mutableStateOf(mutableListOf())
    val photosList: List<PhotoData>
        get() = _photosList

    var searchTextValue = ""

    fun fetchData(context: Context, search: String) {
        if (isNetworkConnected(context)) performNetworkCall(context, search)
        else showMessage("No internet connection", context)
    }

    private fun performNetworkCall(context: Context, search: String) {
        searchTextValue = search
        viewModelScope.launch {
            val response = repository.getPhotos(search)
            val listOfPhotos = mutableListOf<PhotoData>()
            var resultMessage = ""

            response.onSuccess {
                if (data.stat == "ok") {
                    data.photos.photo.forEach {
                        if (itemValid(it)) {
                            val largePhoto = "https://farm${it.farm}.staticflickr.com/${it.server}/${it.id}_${it.secret}_b.jpg"
                            listOfPhotos.add(PhotoData(it.title!!, it.url_n!!, largePhoto))
                        }
                    }
                } else {
                    resultMessage = "Request response: ${data.stat}"
                }
            }.onError {
                resultMessage = "$statusCode - ${message()}"
            }.onException {
                resultMessage = message()
            }

            withContext(Dispatchers.Main) {
                _photosList = listOfPhotos
                if (resultMessage.isNotEmpty()) showMessage(resultMessage, context)
            }
        }
    }

    private fun showMessage(resultMessage: String, context: Context) {
        Toast.makeText(context, resultMessage, Toast.LENGTH_SHORT).show()
    }

    private fun itemValid(photoItem: Photo): Boolean {
        with(photoItem) {
            return when {
                url_n.isNullOrEmpty() || title.isNullOrEmpty() || width_n == null || height_n == null -> false
                width_n < 10 || height_n < 10 -> false
                else -> true
            }
        }
    }

    private fun isNetworkConnected(context: Context): Boolean {
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

}