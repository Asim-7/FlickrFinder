package com.example.flickrfinder.model

data class FlickrUiState(
    val photosList: List<PhotoData> = emptyList(),
    val queryList: List<String> = emptyList(),
    val searchItemValue: String = "",
    val requestState: NetworkState = NetworkState.Loading
)

enum class NetworkState{
    Loading, Success, Error
}