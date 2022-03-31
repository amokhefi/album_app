package com.abd.dev.album.domain.repository

import com.abd.dev.album.data.local.db.AlbumDao
import com.abd.dev.album.data.local.model.AlbumEntity

class FakeAlbumDao : AlbumDao {

    private val data = mutableListOf<AlbumEntity>()

    override suspend fun insert(album: AlbumEntity) {
        data.add(album)
    }

    override suspend fun insertAllAlbum(albums: List<AlbumEntity>) {
        data.addAll(albums)
    }

    override suspend fun findAlbumById(id: Int): AlbumEntity? {
        return data.firstOrNull { it.id == id }
    }

    override suspend fun findAlbumByCategory(albumId: Int): List<AlbumEntity> {
        return data.filter { it.albumId == albumId }.sortedBy { it.title }
    }

    override suspend fun findAllAlbums(): List<AlbumEntity> {
        return data
    }
}