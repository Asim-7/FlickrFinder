package com.example.flickrfinder.di

import android.content.Context
import android.content.SharedPreferences
import com.example.flickrfinder.R
import com.example.flickrfinder.respository.PhotoRepository
import com.example.flickrfinder.respository.PhotoRepositoryImpl
import com.example.flickrfinder.util.Constants.STORAGE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * In the module, dependencies as added as singleton, which can be reached where needed
 * */
@Module
@InstallIn(SingletonComponent::class)     // helps to keep the dependencies alive as long as app is alive
object AppModule {

    @Singleton
    @Provides
    fun providePhotoRepository(@ApplicationContext context: Context) = PhotoRepositoryImpl(context) as PhotoRepository

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)
    }

}