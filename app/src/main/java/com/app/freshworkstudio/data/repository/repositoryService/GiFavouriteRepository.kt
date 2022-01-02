package com.app.freshworkstudio.data.repository.repositoryService

import com.app.freshworkstudio.model.entity.GifFavourite
import kotlinx.coroutines.flow.Flow

/**
 * Service method for favourite gif list
 *
 * */
interface GiFavouriteRepository {
    suspend fun delete(data: GifFavourite)
    fun loadFavGif(success: () -> Unit): Flow<List<GifFavourite>>
}