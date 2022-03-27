package com.abd.dev.album.data.local.utils

import com.abd.dev.album.data.local.model.AlbumEntity
import com.abd.dev.album.data.remote.dto.AlbumDto
import com.abd.dev.album.utils.Mapper

class RemoteAlbumToLocalAlbumMapper : Mapper<AlbumDto, AlbumEntity> {

    override fun map(source: AlbumDto): AlbumEntity {
        return AlbumEntity(
            id = source.id,
            albumId = source.albumId,
            title = source.title,
            thumbnailUrl = source.thumbnailUrl,
            url = source.url,
        )
    }
}