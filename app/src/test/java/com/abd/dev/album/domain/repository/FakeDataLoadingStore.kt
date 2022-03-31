package com.abd.dev.album.domain.repository

import com.abd.dev.album.data.local.utils.DataLoadingStore
import kotlinx.coroutines.flow.flow

class FakeDataLoadingStore : DataLoadingStore {

    private var isDataLoaded = false
    override fun isDataLoaded() = flow {
        emit(isDataLoaded)
    }

    override suspend fun setDataLoaded() {
        isDataLoaded = true
    }
}