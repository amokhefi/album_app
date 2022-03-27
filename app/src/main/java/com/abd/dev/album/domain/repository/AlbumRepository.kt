package com.abd.dev.album.domain.repository

import com.abd.dev.album.domain.model.Album

interface AlbumRepository {
    suspend fun loadAlbums(): Result<List<Album>>
}