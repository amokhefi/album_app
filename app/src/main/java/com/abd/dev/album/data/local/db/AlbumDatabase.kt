package com.abd.dev.album.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.abd.dev.album.data.local.model.AlbumEntity

@Database(entities = [AlbumEntity::class], version = 1)
abstract class AlbumDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
}