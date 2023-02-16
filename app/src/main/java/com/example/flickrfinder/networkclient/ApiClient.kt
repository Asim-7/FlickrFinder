package com.example.flickrfinder.networkclient

import com.example.flickrfinder.respository.PhotoRepositoryImpl.Companion.BASE_LOGIN_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    companion object {
        private var retrofit: Retrofit? = null

        private val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        fun getRoomClient(): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_LOGIN_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit!!
        }
    }
}