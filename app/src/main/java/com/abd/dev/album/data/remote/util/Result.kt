package com.abd.dev.album.data.remote.util

sealed class Result<out T> {
    data class Success<T>(val result: T) : Result<T>()
    data class Error(val ex: Exception) : Result<Nothing>()
}