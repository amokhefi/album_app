package com.abd.dev.album.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ALBUM")
data class AlbumEntity(
    @PrimaryKey
    val id: Int,
    val albumId: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
)
