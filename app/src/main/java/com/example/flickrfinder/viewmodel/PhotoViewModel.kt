package com.example.flickrfinder.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickrfinder.R
import com.example.flickrfinder.model.Photo
import com.example.flickrfinder.model.PhotoData
import com.example.flickrfinder.respository.PhotoRepository
import com.example.flickrfinder.util.Constants.STORAGE_NAME
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

    private var _photosList: List<PhotoData> by mutableStateOf(emptyList())
    val photosList: List<PhotoData>
        get() = _photosList

    private var _queryListState: List<String> by mutableStateOf(emptyList())
    val queryList: List<String>
        get() = _queryListState

    private var _searchItemState by mutableStateOf("")
    val searchItemValue: String
        get() = _searchItemState

    private var _showRedoState by mutableStateOf(false)
    val showRedo: Boolean
        get() = _showRedoState

    var titleText = "Nature"
    private var doRequest = true
    var inProgress = false
    private var predictionsList = mutableListOf(titleText)
    private lateinit var sharedPreference: SharedPreferences

    fun initData(context: Context) {
        if (doRequest) {
            doRequest = false
            initLocalDatabase(context)
        }
    }

    private fun initLocalDatabase(context: Context) {
        sharedPreference = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)
        retrieveFromLocal(context)
    }

    fun fetchData(context: Context, search: String, nextPage: Boolean = false) {
        if (isNetworkConnected(context)) {
            performNetworkCall(context, search, nextPage)
        } else {
            showMessage(context.getString(R.string.no_internet), context)
            _showRedoState = photosList.isEmpty()
        }
    }

    private fun performNetworkCall(context: Context, search: String, nextPage: Boolean) {
        if (!inProgress) {
            inProgress = true
            titleText = search

            viewModelScope.launch {
                val response = repository.getPhotos(search, nextPage)
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
                        val startText = context.getString(R.string.request_response)
                        resultMessage = "$startText: ${data.stat}"
                    }
                }.onError {
                    resultMessage = "$statusCode - ${message()}"
                }.onException {
                    resultMessage = message()
                }

                withContext(Dispatchers.Main) {
                    _photosList = if (nextPage && listOfPhotos.isNotEmpty()) {
                        var newList = photosList.toMutableList()
                        newList.addAll(listOfPhotos)
                        newList = newList.distinct().toMutableList()
                        newList
                    } else {
                        listOfPhotos
                    }

                    if (resultMessage.isNotEmpty()) showMessage(resultMessage, context)
                    _showRedoState = photosList.isEmpty()

                    // delay added due to toast msg on screen
                    val delay = if (resultMessage.isEmpty()) 0 else 1500
                    Handler(Looper.getMainLooper()).postDelayed({ inProgress = false }, delay.toLong())
                }
            }
        }
    }

    fun showMessage(resultMessage: String, context: Context) {
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

    fun isNetworkConnected(context: Context): Boolean {
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

    fun updateSearchItem(text: String) {
        val listContains = predictionsList.filter { it.lowercase().startsWith(text.lowercase()) }
        _queryListState = listContains.toMutableList()
        _searchItemState = text
    }

    fun addPrediction(text: String, save: Boolean) {
        val itemAlreadyPresent = predictionsList.map { it.lowercase() }.contains(text.lowercase())
        if (!itemAlreadyPresent) {
            predictionsList.add(0, text)
            if (save) saveLocally(text)
        }
    }

    private fun saveLocally(text: String) {
        val editor = sharedPreference.edit()
        val set: MutableSet<String> = HashSet()
        set.addAll(predictionsList)
        editor.putStringSet("predictions_list", set)
        editor.putString("title", text)
        editor.apply()
    }

    private fun retrieveFromLocal(context: Context) {
        val set: MutableSet<String>? = sharedPreference.getStringSet("predictions_list", null)
        if (!set.isNullOrEmpty()) {
            set.forEach {
                addPrediction(it, false)
            }
        }

        val savedTitle: String? = sharedPreference.getString("title", null)
        if (!savedTitle.isNullOrEmpty()) titleText = savedTitle

        fetchData(context, predictionsList[0])
    }

    /** below functions are only used for view model testing */
    fun getPredictionsList(): MutableList<String> {
        return predictionsList
    }

}