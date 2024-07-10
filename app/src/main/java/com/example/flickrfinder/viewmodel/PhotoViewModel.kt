package com.example.flickrfinder.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickrfinder.R
import com.example.flickrfinder.model.FlickrUiState
import com.example.flickrfinder.model.NetworkState
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val application: Application,
    private val repository: PhotoRepository,              // here the HelpRepository is an interface because it helps this view model to be tested with both DEFAULT and TEST repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlickrUiState())
    val uiState: StateFlow<FlickrUiState> = _uiState.asStateFlow()

    private val _darkTheme = MutableLiveData(false)
    val darkTheme: LiveData<Boolean> = _darkTheme

    var titleText = "Nature"
    var inProgress = false
    private var predictionsList = mutableListOf(titleText)
    private lateinit var sharedPreference: SharedPreferences

    init {
        initLocalDatabase()
    }

    fun updateTheme() {
        _darkTheme.value = !_darkTheme.value!!
    }

    private fun initLocalDatabase() {
        sharedPreference = application.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)
        retrieveFromLocal()
    }

    fun fetchData(search: String, nextPage: Boolean = false) {
        if (isNetworkConnected()) {
            performNetworkCall(search, nextPage)
        } else {
            showMessage(application.getString(R.string.no_internet))
            _uiState.update { currentState ->
                currentState.copy(
                    requestState = if (currentState.photosList.isEmpty()) NetworkState.Error else NetworkState.Success
                )
            }
        }
    }

    private fun performNetworkCall(search: String, nextPage: Boolean) {
        if (!inProgress) {
            inProgress = true
            titleText = search

            _uiState.update { currentState ->
                currentState.copy(
                    requestState = NetworkState.Loading
                )
            }

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
                        val startText = application.getString(R.string.request_response)
                        resultMessage = "$startText: ${data.stat}"
                    }
                }.onError {
                    resultMessage = "$statusCode - ${message()}"
                }.onException {
                    resultMessage = message()
                }

                withContext(Dispatchers.Main) {
                    val targetPhotosList = if (nextPage && listOfPhotos.isNotEmpty()) {
                        var newList = uiState.value.photosList.toMutableList()
                        newList.addAll(listOfPhotos)
                        newList = newList.distinct().toMutableList()
                        newList
                    } else {
                        listOfPhotos
                    }

                    if (resultMessage.isNotEmpty()) showMessage(resultMessage)

                    _uiState.update { currentState ->
                        currentState.copy(
                            photosList = targetPhotosList,
                            requestState = if (targetPhotosList.isEmpty()) NetworkState.Error else NetworkState.Success
                        )
                    }

                    // delay added due to toast msg on screen
                    val delay = if (resultMessage.isEmpty()) 0 else 1500
                    Handler(Looper.getMainLooper()).postDelayed({ inProgress = false }, delay.toLong())
                }
            }
        }
    }

    fun showMessage(resultMessage: String) {
        Toast.makeText(application, resultMessage, Toast.LENGTH_SHORT).show()
    }

    fun itemValid(photoItem: Photo): Boolean {
        with(photoItem) {
            return when {
                url_n.isNullOrEmpty() || title.isNullOrEmpty() || width_n == null || height_n == null -> false
                width_n < 10 || height_n < 10 -> false
                else -> true
            }
        }
    }

    fun isNetworkConnected(): Boolean {
        return repository.isNetworkConnected()
    }

    fun updateSearchItem(text: String) {
        val listContains = predictionsList.filter { it.lowercase().startsWith(text.lowercase()) }
        _uiState.update { currentState ->
            currentState.copy(
                queryList = listContains.toMutableList(),
                searchItemValue = text
            )
        }
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

    private fun retrieveFromLocal() {
        val set: MutableSet<String>? = sharedPreference.getStringSet("predictions_list", null)
        if (!set.isNullOrEmpty()) {
            set.forEach {
                addPrediction(it, false)
            }
        }

        val savedTitle: String? = sharedPreference.getString("title", null)
        if (!savedTitle.isNullOrEmpty()) titleText = savedTitle

        fetchData(predictionsList[0])
    }

    /** below functions are only used for view model testing */
    fun getPredictionsList(): MutableList<String> {
        return predictionsList
    }

}