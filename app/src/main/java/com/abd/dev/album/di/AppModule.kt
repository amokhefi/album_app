package com.abd.dev.album.di

import android.content.Context
import androidx.room.Room
import com.abd.dev.album.BuildConfig
import com.abd.dev.album.data.local.db.AlbumDatabase
import com.abd.dev.album.data.local.utils.DATABASE_NAME
import com.abd.dev.album.data.local.utils.DataLoadingStore
import com.abd.dev.album.data.local.utils.RemoteAlbumToLocalAlbumMapper
import com.abd.dev.album.data.remote.api.AlbumApi
import com.abd.dev.album.domain.repository.AlbumMappers
import com.abd.dev.album.domain.repository.AlbumRepositoryImpl
import com.abd.dev.album.domain.utils.LocalAlbumToDomainMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAlbumApiService(): AlbumApi {
        return Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AlbumApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AlbumDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            AlbumDatabase::class.java, DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun provideAlbumMapper() =
        AlbumMappers(
            RemoteAlbumToLocalAlbumMapper(),
            LocalAlbumToDomainMapper()
        )

    @Singleton
    @Provides
    fun provideAlbumRepository(
        api: AlbumApi,
        database: AlbumDatabase,
        mappers: AlbumMappers,
        dataStore: DataLoadingStore
    ) = AlbumRepositoryImpl(
        api, database.albumDao(), mappers, dataStore
    )

    @Singleton
    @Provides
    fun provideDataStore(
        @ApplicationContext context: Context
    ) = DataLoadingStore(context)
}