package com.abd.dev.album.data.local.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.abd.dev.album.data.local.utils.DataLoadingStore.PreferenceKeys.IS_DATA_LOADED
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

const val DATA_STORE_NAME = "data_store_preferences"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

class DataLoadingStore @Inject constructor(
    @ApplicationContext context: Context,
) {

    private val dataStore = context.dataStore

    suspend fun setDataLoaded() {
        dataStore.edit { data ->
            data[IS_DATA_LOADED] = true
        }
    }

    fun isDataLoaded(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IS_DATA_LOADED] ?: false
        }
    }

    private object PreferenceKeys {
        val IS_DATA_LOADED = booleanPreferencesKey("data_loaded")
    }
}


