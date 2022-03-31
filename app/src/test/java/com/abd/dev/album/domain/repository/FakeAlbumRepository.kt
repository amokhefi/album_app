package com.abd.dev.album.domain.repository

import com.abd.dev.album.domain.model.Album
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response.error
import java.io.IOException

class FakeAlbumRepository : AlbumRepository {

    var isServerError = false
    var simulateNetworkError = false

    private val items = listOf(
        Album(1, 2, "title", "url", ""),
        Album(2, 2, "title", "url", ""),
        Album(3, 2, "title", "url", ""),
        Album(4, 2, "title", "url", "")
    )

    override suspend fun loadAlbums(): Result<List<Album>> {

        if (isServerError) return Result.failure(

            HttpException(
                error<Any>(
                    503,
                    "Test Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
                )
            )
        )
        if (simulateNetworkError) return Result.failure(IOException())
        return Result.success(items)
    }
}