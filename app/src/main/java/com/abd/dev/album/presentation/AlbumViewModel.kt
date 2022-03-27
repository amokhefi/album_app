package com.abd.dev.album.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abd.dev.album.domain.model.Album
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

    private val _selectedAlbum = MutableStateFlow<Album?>(null)
    val selectedAlbum = _selectedAlbum.asStateFlow()

    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albumList = _albums.asStateFlow()

    init {
        viewModelScope.launch {
            val loadAlbums = repository.loadAlbums()
            _albums.value = loadAlbums.getOrDefault(emptyList())
        }
    }

    fun setSelectedItem(album: Album) {
        _selectedAlbum.value = album
    }

}