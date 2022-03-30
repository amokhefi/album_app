package com.abd.dev.album.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abd.dev.album.domain.repository.AlbumRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepositoryImpl
) : ViewModel() {

    private val _selectedAlbum = MutableStateFlow<UiAlbum?>(null)
    val selectedAlbum = _selectedAlbum.asStateFlow()

    private val _albums = MutableStateFlow<List<UiAlbum>>(emptyList())
    val albumList = _albums.asStateFlow()

    init {
        viewModelScope.launch {
            val loadAlbums = repository.loadAlbums()
            _albums.value = loadAlbums.getOrDefault(emptyList()).map { album ->
                UiAlbum(
                    id = album.id,
                    albumId = album.albumId,
                    title = album.title,
                    thumbnailUrl = album.thumbnailUrl,
                    url = album.url,
                )
            }
        }
    }

    fun setSelectedItem(album: UiAlbum) {
        _selectedAlbum.value = album
    }

}