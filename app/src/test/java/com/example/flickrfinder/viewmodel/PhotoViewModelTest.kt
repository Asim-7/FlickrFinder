package com.example.flickrfinder.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
    fun `starting title text is nature`() {
        val titleText = viewModel.titleText
        assertThat(titleText).isEqualTo("Nature")
    }

    @Test
    fun `starting prediction list is not empty`() {
        val predictionList = viewModel.getPredictionsList()
        assertThat(predictionList).isNotEmpty()
    }

    @Test
    fun `starting prediction list has one element`() {
        val predictionList = viewModel.getPredictionsList()
        assertThat(predictionList).hasSize(1)
    }

}