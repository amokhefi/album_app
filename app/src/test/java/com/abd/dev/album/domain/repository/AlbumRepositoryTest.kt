package com.abd.dev.album.domain.repository

import com.abd.dev.album.data.local.db.AlbumDao
import com.abd.dev.album.data.local.utils.DataLoadingStore
import com.abd.dev.album.data.local.utils.RemoteAlbumToLocalAlbumMapper
import com.abd.dev.album.data.remote.api.AlbumApi
import com.abd.dev.album.domain.utils.LocalAlbumToDomainMapper
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.internal.verification.Times
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)

class AlbumRepositoryTest {

    private lateinit var repository: AlbumRepositoryImpl
    private lateinit var localDataSource: AlbumDao
    private lateinit var dataStore: DataLoadingStore
    private lateinit var apiSpy: AlbumApi

    @Before
    fun steUp() {
        apiSpy = Mockito.spy(FakeAlbumApi())
        localDataSource = FakeAlbumDao()
        dataStore = FakeDataLoadingStore()
        repository = AlbumRepositoryImpl(
            apiSpy, localDataSource,
            AlbumMappers(
                RemoteAlbumToLocalAlbumMapper(),
                LocalAlbumToDomainMapper()
            ),
            dataStore
        )
    }

    @After
    fun tearDown() {
        (apiSpy as FakeAlbumApi).shutdownServer()
    }

    @Test
    fun `load data success`() = runTest {
        val result = repository.loadAlbums()
        assertThat(localDataSource.findAllAlbums()).isNotEmpty()
        verify(apiSpy, Times(1)).getAlbums()
        assertThat(dataStore.isDataLoaded().first()).isTrue()
        assertThat(result.isSuccess)
    }

    @Test
    fun `when first call succeed , second time should not calling web service`() = runTest {
        repository.loadAlbums()
        assertThat(localDataSource.findAllAlbums()).isNotEmpty()
        assertThat(dataStore.isDataLoaded().first()).isTrue()
        // second call
        repository.loadAlbums()
        verify(apiSpy, Times(1)).getAlbums()
    }

    @Test
    fun `return http exception when failure`() = runTest {
        (apiSpy as FakeAlbumApi).serverError = true
        val result = repository.loadAlbums()
        assertThat(localDataSource.findAllAlbums()).isEmpty()
        assertThat(dataStore.isDataLoaded().first()).isFalse()
        verify(apiSpy, Times(1)).getAlbums()
        assertThat(result.isFailure)
    }

    @Test
    fun `should recall web service when first call is failure`() = runTest {
        (apiSpy as FakeAlbumApi).serverError = true
        repository.loadAlbums()
        assertThat(localDataSource.findAllAlbums()).isEmpty()
        assertThat(dataStore.isDataLoaded().first()).isFalse()
        repository.loadAlbums()
        verify(apiSpy, Times(2)).getAlbums()
    }
}