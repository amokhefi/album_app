package com.abd.dev.album.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abd.dev.album.domain.repository.AlbumRepository
import com.abd.dev.album.domain.repository.AlbumRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepository
) : ViewModel() {

    private val _selectedAlbum = MutableStateFlow<UiAlbum?>(null)
    val selectedAlbum = _selectedAlbum.asStateFlow()

    private val _uiState = MutableStateFlow<AlbumState>(AlbumState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val loadAlbums = repository.loadAlbums()

            when {
                loadAlbums.isFailure -> {
                    when (loadAlbums.exceptionOrNull()) {
                        is HttpException -> {
                            _uiState.value = AlbumState.Error(NetworkError.RemoteError)
                        }
                        is IOException -> {
                            _uiState.value = AlbumState.Error(NetworkError.UnAvailableError)
                        }
                        is Exception -> {
                            _uiState.value = AlbumState.Error(NetworkError.UnknownError)
                        }
                        null -> Unit
                    }

                }

                loadAlbums.isSuccess -> {
                    _uiState.value = AlbumState.Success(
                        loadAlbums.getOrDefault(emptyList()).map { album ->
                            UiAlbum(
                                id = album.id,
                                albumId = album.albumId,
                                title = album.title,
                                thumbnailUrl = album.thumbnailUrl,
                                url = album.url,
                            )
                        }
                    )
                }

            }
        }
    }

    fun setSelectedItem(album: UiAlbum) {
        _selectedAlbum.value = album
    }

}

sealed class AlbumState {
    object Loading : AlbumState()
    class Success(val albums: List<UiAlbum>) : AlbumState()
    class Error(val error: NetworkError) : AlbumState()
}

sealed class NetworkError {
    object UnAvailableError : NetworkError()
    object RemoteError : NetworkError()
    object UnknownError : NetworkError()
}