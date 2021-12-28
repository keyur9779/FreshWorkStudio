package com.app.freshworkstudio.dependencyInjections

import com.app.freshworkstudio.data.api.service.GiphyApiService
import com.app.freshworkstudio.data.repository.GiFavouriteRepository
import com.app.freshworkstudio.data.repository.GiphyTrendingRepository
import com.app.freshworkstudio.data.room.GFavouriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideGiphyTrendingRepository(
        giphyService: GiphyApiService,
        gFavouriteDao: GFavouriteDao
    ): GiphyTrendingRepository {
        return GiphyTrendingRepository(giphyService, gFavouriteDao)
    }

    @Provides
    @ViewModelScoped
    fun provideGiFavouriteRepository(
        gFavouriteDao: GFavouriteDao
    ): GiFavouriteRepository {
        return GiFavouriteRepository(gFavouriteDao)
    }
}