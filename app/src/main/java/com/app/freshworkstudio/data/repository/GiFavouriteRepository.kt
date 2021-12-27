package com.app.freshworkstudio.data.repository

import androidx.annotation.WorkerThread
import com.app.freshworkstudio.data.room.GFavouriteDao
import com.app.freshworkstudio.model.entity.GifFavourite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion


/**
 * class is used to fetch and save data related to trending and searched gif.
 *
 * @param giphyService = list of network methods
 * */
class GiFavouriteRepository constructor(
    private val gFavouriteDao: GFavouriteDao
) {

    @WorkerThread
    suspend fun delete(data: GifFavourite) {
        gFavouriteDao.deleteByGif(data)
    }


    /*
    * This method is used to fetch gif from local room data
    * */
    @WorkerThread
    fun loadFavGif(success: () -> Unit) =
        gFavouriteDao.getGifList()
            .onCompletion { success() }.flowOn(Dispatchers.IO)

}
