package com.abd.dev.album.utils

interface Mapper<S, D> {
    fun map(source: S): D
    fun mapList(source: List<S>): List<D> {
        return source.map {
            map(it)
        }
    }
}