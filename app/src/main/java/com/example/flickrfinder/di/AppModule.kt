package com.example.flickrfinder.di

import com.example.flickrfinder.respository.PhotoRepository
import com.example.flickrfinder.respository.PhotoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun providePhotoRepository() = PhotoRepositoryImpl() as PhotoRepository
}