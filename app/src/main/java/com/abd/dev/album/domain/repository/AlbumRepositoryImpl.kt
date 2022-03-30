package com.abd.dev.album.domain.repository

import com.abd.dev.album.data.local.db.AlbumDao
import com.abd.dev.album.data.local.utils.DataLoadingStore
import com.abd.dev.album.data.remote.api.AlbumApi
import com.abd.dev.album.domain.model.Album
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class AlbumRepositoryImpl @Inject constructor(
    private val api: AlbumApi,
    private val localDataSource: AlbumDao,
    private val mapperAlbum: AlbumMappers,
    private val dataStore: DataLoadingStore
) : AlbumRepository {

    override suspend fun loadAlbums(): Result<List<Album>> {
        val isDataLoaded = dataStore.isDataLoaded().first()
        if (isDataLoaded) {
            return Result.success(
                mapperAlbum.localToDomainMapper.mapList(
                    localDataSource.findAllAlbums()
                )
            )
        } else {
            val remoteAlbums = loadRemoteAlbums()
            return if (remoteAlbums.isSuccess) {
                Result.success(
                    mapperAlbum.localToDomainMapper.mapList(
                        localDataSource.findAllAlbums()
                    )
                )
            } else {
                Result.failure(remoteAlbums.exceptionOrNull() ?: Exception())
            }
        }
    }

    private suspend fun loadRemoteAlbums(): Result<Unit> {
        return try {
            val response = api.getAlbums()
            if (response.isSuccessful && response.body() != null) {
                localDataSource.insertAllAlbum(
                    mapperAlbum
                        .remoteToLocalAlbumMapper
                        .mapList(response.body()!!)
                )
                dataStore.setDataLoaded()
                Result.success(Unit)
            } else {
                Result.failure(Exception())
            }
        } catch (httpException: HttpException) {
            Result.failure(httpException)
        } catch (ioException: IOException) {
            Result.failure(ioException)
        }
    }
}