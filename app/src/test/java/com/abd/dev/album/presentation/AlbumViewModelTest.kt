package com.abd.dev.album.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.abd.dev.album.domain.repository.FakeAlbumRepository
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumViewModelTest {

    private lateinit var viewModel: AlbumViewModel
    private lateinit var repository: FakeAlbumRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        repository = FakeAlbumRepository()
        viewModel = AlbumViewModel(repository)
    }


    @Test
    fun `initial state should loading`() = runTest{
        assertThat(viewModel.uiState.value).isInstanceOf(AlbumState.Loading::class.java)
    }

    @Test
    fun `load items success`() = runTest{
        viewModel.loadItems()
        val items = viewModel.uiState.first()
        assertThat(items).isInstanceOf(AlbumState.Success::class.java)
        assertThat((items as AlbumState.Success).albums).isNotEmpty()
    }

    @Test
    fun `load items when server Error should return NetworkError`() = runTest{
        repository.simulateNetworkError = true
        viewModel.loadItems()
        val result = viewModel.uiState.first()
        assertThat(result).isInstanceOf(AlbumState.Error::class.java)
        assertThat((result as AlbumState.Error).error).isEqualTo(NetworkError.UnAvailableError)
    }

    @Test
    fun `load items when server Error should return HttpError`() = runBlocking{
        repository.isServerError = true
        viewModel.loadItems()
        val result = viewModel.uiState.first()
        assertThat(result).isInstanceOf(AlbumState.Error::class.java)
        assertThat((result as AlbumState.Error).error).isEqualTo(NetworkError.RemoteError)
    }

}