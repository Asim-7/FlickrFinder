package com.example.flickrfinder.viewmodel

import android.content.Context
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val repository: PhotoRepository,              // here the HelpRepository is an interface because it helps this view model to be tested with both DEFAULT and FAKE repository
    private val context: Context
) : ViewModel() {

    private var _photosList: MutableList<PhotoData> by mutableStateOf(mutableListOf())
    val photosList: List<PhotoData>
        get() = _photosList

    fun fetchData() {
        viewModelScope.launch {
            val response = repository.getPhotos()
            val listOfPhotos = mutableListOf<PhotoData>()
            var resultMessage = ""

            response.onSuccess {
                if (data.stat == "ok") {
                    data.photos.photo.forEach {
                        if (itemValid(it)) {
                            listOfPhotos.add(PhotoData(it.title!!, it.url_h!!))
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

            if (resultMessage.isNotEmpty()) showMessage(resultMessage)

            _photosList = listOfPhotos
        }
    }

    private fun showMessage(resultMessage: String) {
        Toast.makeText(context, resultMessage, Toast.LENGTH_SHORT).show()
    }

    private fun itemValid(photoItem: Photo): Boolean {
        with(photoItem) {
            return when {
                url_h.isNullOrEmpty() || title.isNullOrEmpty() || width_h == null || height_h == null -> false
                width_h < 10 || height_h < 10 -> false
                else -> true
            }
        }
    }

}