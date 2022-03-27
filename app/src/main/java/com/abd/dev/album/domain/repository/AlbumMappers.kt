package com.abd.dev.album.domain.repository

import com.abd.dev.album.data.local.utils.RemoteAlbumToLocalAlbumMapper
import com.abd.dev.album.domain.utils.LocalAlbumToDomainMapper

data class AlbumMappers(
    val remoteToLocalAlbumMapper: RemoteAlbumToLocalAlbumMapper,
    val localToDomainMapper: LocalAlbumToDomainMapper
)