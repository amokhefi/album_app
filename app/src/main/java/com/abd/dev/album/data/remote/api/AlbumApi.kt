package com.abd.dev.album.data.remote.api

import com.abd.dev.album.data.remote.dto.AlbumDto
import retrofit2.Response
import retrofit2.http.GET

interface AlbumApi {
    @GET("img/shared/technical-test.json")
    suspend fun getAlbums(): Response<List<AlbumDto>>
}