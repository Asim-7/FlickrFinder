package com.example.flickrfinder.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickrfinder.model.PhotoData
import com.example.flickrfinder.respository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel@Inject constructor(
    private val repository: PhotoRepository              // here the HelpRepository is an interface because it helps this view model to be tested with both DEFAULT and FAKE repository
) : ViewModel() {

    private var _photosList = listOf<PhotoData>()
    val photosList: List<PhotoData>
        get() = _photosList

    fun hello() {
        Log.e("ASIM", "Hello I am started")

        viewModelScope.launch {
            _photosList = repository.getPhotos()
        }
    }

}