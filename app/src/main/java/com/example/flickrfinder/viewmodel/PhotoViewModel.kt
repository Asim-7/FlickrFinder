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

    fun fetchData(context: Context) {
        viewModelScope.launch {
            val response = repository.getPhotos()
            val listOfPhotos = mutableListOf<PhotoData>()
            var resultMessage = ""

            response.onSuccess {
                if (data.stat == "ok") {
                    data.photos.photo.forEach {
                        if (itemValid(it)) {
                            listOfPhotos.add(PhotoData(it.title!!, it.url_n!!))
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

            _photosList = listOfPhotos

            withContext(Dispatchers.Main) {
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

}