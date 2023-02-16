package com.example.flickrfinder.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.flickrfinder.respository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel@Inject constructor(
    private val repository: PhotoRepository              // here the HelpRepository is an interface because it helps this view model to be tested with both DEFAULT and FAKE repository
) : ViewModel() {

    init {
        repository.getPhotos()
    }

    fun hello() {
        Log.e("ASIM", "Hello I am started")
    }

}