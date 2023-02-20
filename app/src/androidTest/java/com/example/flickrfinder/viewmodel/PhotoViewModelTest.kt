package com.example.flickrfinder.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.example.flickrfinder.respository.TestRepository
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PhotoViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()     // this helps to run tests on the same thread one after another

    private lateinit var viewModel: PhotoViewModel              // variable is not init here to keep the tests independent from each other

    @Before
    fun setup() {                                               // this function runs before the execution of every test case (mostly used as init function)
        viewModel = PhotoViewModel(TestRepository())
    }

    @Test
    fun checkInternetConnection_whenInternetConnected() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val result = viewModel.isNetworkConnected(context)
        assertThat(result).isTrue()
    }

    /** Disconnect internet to run this test */
    /*@Test
    fun checkInternetConnection_whenInternetNotConnected() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val result = viewModel.isNetworkConnected(context)
        assertThat(result).isFalse()
    }*/

}