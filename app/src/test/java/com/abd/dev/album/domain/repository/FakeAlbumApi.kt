package com.abd.dev.album.domain.repository

import android.content.res.Resources
import com.abd.dev.album.data.remote.api.AlbumApi
import com.abd.dev.album.data.remote.dto.AlbumDto
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.IOException
import okio.buffer
import okio.source
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FakeAlbumApi : AlbumApi {

    var serverError = false

    private val server = MockWebServer()
    private val service = Retrofit.Builder()
        .baseUrl(server.url(""))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AlbumApi::class.java)

    private fun enqueueMockResponse(fileName: String) {
        javaClass.classLoader?.let {
            val inputStream = it.getResourceAsStream(fileName)
            val source = inputStream.source().buffer()
            val mockResponse = MockResponse()
            mockResponse.setBody(source.readString(Charsets.UTF_8))
            server.enqueue(mockResponse)
        }
    }

    override suspend fun getAlbums(): Response<List<AlbumDto>> {
        when {
            serverError -> {
                throw HttpException(
                    Response.error<Any>(
                        500,
                        "Test Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
                    )
                )
            }
            else -> {
                enqueueMockResponse("album_success_response.json")
            }
        }
        return service.getAlbums()
    }

    fun shutdownServer() {
       server.shutdown()
    }

}