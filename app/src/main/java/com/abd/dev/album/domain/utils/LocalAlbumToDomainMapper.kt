package com.abd.dev.album.domain.utils

import com.abd.dev.album.data.local.model.AlbumEntity
import com.abd.dev.album.domain.model.Album
import com.abd.dev.album.utils.Mapper

class LocalAlbumToDomainMapper: Mapper<AlbumEntity, Album> {
    override fun map(source: AlbumEntity): Album {
        return Album(
            id = source.id,
            albumId = source.albumId,
            title = source.title,
            thumbnailUrl = source.thumbnailUrl,
            url = source.url,
        )
    }
}