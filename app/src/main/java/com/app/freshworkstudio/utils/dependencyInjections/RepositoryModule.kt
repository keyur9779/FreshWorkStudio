package com.app.freshworkstudio.utils.dependencyInjections

import com.app.freshworkstudio.data.api.service.GiphyApiService
import com.app.freshworkstudio.data.repository.GiphyTrendingRepository
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
        giphyService: GiphyApiService
    ): GiphyTrendingRepository {
        return GiphyTrendingRepository(giphyService)
    }
}