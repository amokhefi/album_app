package com.abd.dev.album.data.local.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.abd.dev.album.data.local.model.AlbumEntity
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class AlbumDaoTest {

    private lateinit var database: AlbumDatabase
    private lateinit var dao: AlbumDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AlbumDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.albumDao()
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun insertAlbumTest() = runTest {
        val album = AlbumEntity(1, 2, "title", "url","")
        dao.insert(album)
        val allAlbums = dao.findAllAlbums()
        assertThat(allAlbums).contains(album)
    }

    @Test
    fun insertAllAlbumTest() = runTest {
        val albums = listOf(
            AlbumEntity(1, 2, "title", "url",""),
            AlbumEntity(2, 2, "title", "url",""),
            AlbumEntity(3, 2, "title", "url",""),
            AlbumEntity(4, 2, "title", "url","")
        )
        dao.insertAllAlbum(albums)
        val allAlbums = dao.findAllAlbums()
        assertThat(allAlbums.size).isEqualTo(albums.size)
    }

    @Test
    fun findByIdTest() = runTest {
        val album = AlbumEntity(1, 2, "title", "url","")
        dao.insert(album)
        val found = dao.findAlbumById(album.id)
        assertThat(album).isEqualTo(found)
    }

    @Test
    fun findByCategoryIdTest() = runTest {
        val album = AlbumEntity(1, 2, "title", "url","")
        dao.insert(album)
        val found = dao.findAlbumByCategory(album.albumId)
        assertThat(found).contains(album)
    }

    @Test
    fun findByCategoryOrderTest() = runTest {
        val albums = listOf(
            AlbumEntity(1, 2, "Y", "url",""),
            AlbumEntity(2, 2, "A", "url",""),
            AlbumEntity(3, 2, "D", "url",""),
            AlbumEntity(4, 5, "title", "url","")
        )
        dao.insertAllAlbum(albums)
        val found = dao.findAlbumByCategory(2)
        assertThat(found.size).isEqualTo(3)
        assertThat(found[0].title).isEqualTo("A")
        assertThat(found[1].title).isEqualTo("D")
        assertThat(found[2].title).isEqualTo("Y")

    }
}