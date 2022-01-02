package com.app.freshworkstudio.data.repository.repositoryService

import com.app.freshworkstudio.model.IOTaskResult
import com.app.freshworkstudio.model.entity.GifFavourite
import kotlinx.coroutines.flow.Flow

/**
 * Service method implementation for trending and searched Gif listing with marketing gif as Favourite or un Favourite
 * */
interface GiphyTrendingRepository {

    suspend fun insertFav(data: GifFavourite)
    suspend fun getGifByID(gifID: String): Flow<ArrayList<GifFavourite>>
    fun loadTrendingGif(page: Int, q: String, success: () -> Unit): Flow<IOTaskResult<Any>>
}