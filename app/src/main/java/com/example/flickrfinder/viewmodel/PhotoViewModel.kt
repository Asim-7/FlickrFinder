package com.example.flickrfinder.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickrfinder.model.FlickrUiState
import com.example.flickrfinder.model.NetworkState
import com.example.flickrfinder.model.Photo
import com.example.flickrfinder.model.PhotoData
import com.example.flickrfinder.respository.PhotoRepository
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val repository: PhotoRepository,              // here the HelpRepository is an interface because it helps this view model to be tested with both DEFAULT and TEST repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlickrUiState())
    val uiState: StateFlow<FlickrUiState> = _uiState.asStateFlow()

    private val _darkTheme = MutableStateFlow(false)
    val darkTheme: StateFlow<Boolean> = _darkTheme.asStateFlow()

    var currentSearchQuery = "Nature"
    var isLoading = false
    private var searchHistory = mutableListOf(currentSearchQuery)

    init {
        loadSavedData()
    }

    fun toggleTheme() {
        _darkTheme.value = !_darkTheme.value!!
        saveDarkModePreference()
    }

    private fun loadSavedData() {
        loadSearchHistory()
        loadDarkModePreference()
        fetchData(searchHistory.first()) // Fetch initial data
    }

    fun fetchData(searchQuery: String, nextPage: Boolean = false) {
        if (isLoading) return // Prevent multiple concurrent requests

        if (isNetworkConnected()) {
            performNetworkCall(searchQuery, nextPage)
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    requestState = NetworkState.Error,
                    messageState = "No internet"
                )
            }
        }
    }

    private fun performNetworkCall(searchQuery: String, nextPage: Boolean) {
        isLoading = true
        currentSearchQuery = searchQuery

        _uiState.update { currentState ->
            currentState.copy(
                requestState = NetworkState.Loading
            )
        }

        viewModelScope.launch {
            val response = repository.getPhotos(searchQuery, nextPage)
            val newPhotos = mutableListOf<PhotoData>()
            var requestFailed = true

            response.onSuccess {
                if (data.stat == "ok") {
                    data.photos.photo.forEach {
                        if (itemValid(it)) {
                            val largePhoto = "https://farm${it.farm}.staticflickr.com/${it.server}/${it.id}_${it.secret}_b.jpg"
                            newPhotos.add(PhotoData(it.title!!, it.url_n!!, largePhoto))
                        }
                    }
                    requestFailed = false
                }
            }.onError {
                // resultMessage = "$statusCode - ${message()}"
            }.onException {
                // resultMessage = message()
            }

            withContext(Dispatchers.Main) {
                handleNetworkResponse(nextPage, newPhotos, requestFailed)
            }
        }
    }

    private fun handleNetworkResponse(
        nextPage: Boolean,
        newPhotos: MutableList<PhotoData>,
        requestFailed: Boolean
    ) {
        val updatedPhotosList = if (nextPage) {
            (uiState.value.photosList + newPhotos).distinct().toMutableList()
        } else {
            newPhotos
        }

        if (requestFailed) showMessage("Failed to fetch data, please try again")

        _uiState.update { currentState ->
            currentState.copy(
                photosList = updatedPhotosList,
                requestState = if (updatedPhotosList.isEmpty()) NetworkState.Error else NetworkState.Success
            )
        }

        // Delay resetting isLoading if an error occurred to allow the message to be displayed
        viewModelScope.launch {
            delay(if (requestFailed) 1500L else 0L)
            isLoading = false
        }
    }

    fun showMessage(resultMessage: String) {
        _uiState.update { currentState ->
            currentState.copy(
                messageState = resultMessage
            )
        }
    }

    fun clearMessage() {
        _uiState.update { currentState ->
            currentState.copy(
                messageState = null
            )
        }
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

    fun updateSearchQuery(query: String) {
        val listContains = searchHistory.filter { it.lowercase().startsWith(query.lowercase()) }
        _uiState.update { currentState ->
            currentState.copy(
                queryList = listContains.toMutableList(),
                searchItemValue = query
            )
        }
    }

    fun addSearchQueryToHistory(query: String, save: Boolean) {
        val itemAlreadyPresent = searchHistory.map { it.lowercase() }.contains(query.lowercase())
        if (!itemAlreadyPresent) {
            searchHistory.add(0, query)
            if (save) saveSearchHistory(query)
        }
    }

    private fun saveSearchHistory(newQuery: String) {
        val editor = sharedPreferences.edit()
        val set: MutableSet<String> = HashSet()
        set.addAll(searchHistory)
        editor.putStringSet("search_history_list", set)
        editor.putString("query", newQuery)
        editor.apply()
    }

    private fun loadSearchHistory() {
        val savedHistory = sharedPreferences.getStringSet("search_history_list", null)
        if (!savedHistory.isNullOrEmpty()) {
            savedHistory.forEach { addSearchQueryToHistory(it, false) }
        }

        val savedQuery = sharedPreferences.getString("query", null)
        if (!savedQuery.isNullOrEmpty()) currentSearchQuery = savedQuery
    }

    private fun saveDarkModePreference() {
        val editor = sharedPreferences.edit()
        editor.putString("darkMode", darkTheme.value.toString())
        editor.apply()
    }

    private fun loadDarkModePreference() {
        val retrievedString = sharedPreferences.getString("darkMode", null)
        if (retrievedString != null) _darkTheme.value = retrievedString.toBoolean()
    }

    /** below functions are only used for view model testing */
    fun getPredictionsList(): MutableList<String> {
        return searchHistory
    }

}