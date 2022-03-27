package com.abd.dev.album.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abd.dev.album.data.local.model.AlbumEntity

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(album: AlbumEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAlbum(albums: List<AlbumEntity>)

    @Query("SELECT * from AlbumEntity WHERE id =:id")
    suspend fun findAlbumById(id: Int): AlbumEntity?

    @Query("SELECT * from AlbumEntity WHERE albumId =:albumId ORDER BY title")
    suspend fun findAlbumByCategory(albumId: Int): List<AlbumEntity>

}