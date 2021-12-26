package com.app.freshworkstudio.data.repository

import androidx.annotation.WorkerThread
import com.app.freshworkstudio.data.room.GFavouriteDao
import com.app.freshworkstudio.model.IOTaskResult
import com.app.freshworkstudio.model.entity.GifFavourite
import com.app.freshworkstudio.utils.DataUtils.apiKEY
import com.app.freshworkstudio.utils.DataUtils.api_key
import com.app.freshworkstudio.utils.DataUtils.delay
import com.app.freshworkstudio.utils.DataUtils.limit
import com.app.freshworkstudio.utils.DataUtils.offset
import com.app.freshworkstudio.utils.DataUtils.pageCount
import com.app.freshworkstudio.utils.DataUtils.query
import com.app.freshworkstudio.utils.DataUtils.search
import com.app.freshworkstudio.utils.DataUtils.trend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
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


     fun delete(data: GifFavourite) {
        gFavouriteDao.deleteByGifId(data)
    }


    /*
    * This method is used to fetch gif from local room data
    * */
    @WorkerThread
    fun loadFavGif(success: () -> Unit) = gFavouriteDao.getGifList().onCompletion { success() }.flowOn(Dispatchers.IO)

}
