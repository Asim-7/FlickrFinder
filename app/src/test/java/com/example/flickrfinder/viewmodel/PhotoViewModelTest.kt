package com.example.flickrfinder.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.flickrfinder.model.Photo
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
        val titleText = viewModel.currentSearchQuery
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

    @Test
    fun `item is not valid if values empty`() {
        val photoItem = Photo("123456", "98765", "4567", 2389, "", 0, 0, "")
        assertThat(viewModel.itemValid(photoItem)).isFalse()
    }

    @Test
    fun `item is not valid if all or any value null`() {
        val photoItem1 = Photo("123456", "98765", "4567", 2389, null, null, null, null)
        val photoItem2 = Photo("123456", "98765", "4567", 2389, null, null, null, "https://picsum.photos/200")
        val photoItem3 = Photo("123456", "98765", "4567", 2389, null, null, 50, "https://picsum.photos/200")
        val photoItem4 = Photo("123456", "98765", "4567", 2389, null, 50, 50, "https://picsum.photos/200")
        assertThat(viewModel.itemValid(photoItem1)).isFalse()
        assertThat(viewModel.itemValid(photoItem2)).isFalse()
        assertThat(viewModel.itemValid(photoItem3)).isFalse()
        assertThat(viewModel.itemValid(photoItem4)).isFalse()
    }

    @Test
    fun `item not valid if width or height is smaller than 10`() {
        val photoItem1 = Photo("123456", "98765", "4567", 2389, "Picture", 5, 5, "https://picsum.photos/200")
        val photoItem2 = Photo("123456", "98765", "4567", 2389, "Picture", 15, 5, "https://picsum.photos/200")
        val photoItem3 = Photo("123456", "98765", "4567", 2389, "Picture", 5, 15, "https://picsum.photos/200")
        assertThat(viewModel.itemValid(photoItem1)).isFalse()
        assertThat(viewModel.itemValid(photoItem2)).isFalse()
        assertThat(viewModel.itemValid(photoItem3)).isFalse()
    }

}