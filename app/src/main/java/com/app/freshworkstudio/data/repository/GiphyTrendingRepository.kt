package com.app.freshworkstudio.data.repository

import androidx.annotation.WorkerThread
import com.app.freshworkstudio.BuildConfig.API_KEY
import com.app.freshworkstudio.FreshWorkApp
import com.app.freshworkstudio.R
import com.app.freshworkstudio.data.api.service.GiphyApiService
import com.app.freshworkstudio.data.room.GFavouriteDao
import com.app.freshworkstudio.model.IOTaskResult
import com.app.freshworkstudio.model.entity.GifFavourite
import com.app.freshworkstudio.utils.DataUtils.api_key
import com.app.freshworkstudio.utils.DataUtils.limit
import com.app.freshworkstudio.utils.DataUtils.offset
import com.app.freshworkstudio.utils.DataUtils.pageCount
import com.app.freshworkstudio.utils.DataUtils.query
import com.app.freshworkstudio.utils.DataUtils.search
import com.app.freshworkstudio.utils.DataUtils.trend
import com.skydoves.whatif.whatIf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion


/**
 * class is used to fetch and save data related to trending and searched gif.
 *
 * @param giphyService = list of network methods
 * @param gFavouriteDao = room interface
 * */
class GiphyTrendingRepository constructor(
    private val giphyService: GiphyApiService,
    private val gFavouriteDao: GFavouriteDao
) {

    @WorkerThread
    suspend fun insertFav(data: GifFavourite) {

        // Just making sure we are not duplicating by any means, as url of
        // gif are getting different all time, so sometimes room is not able to resolve conflicts replace
        val item = gFavouriteDao.getGifById(data.gifID)

        whatIf(item != null,
            { gFavouriteDao.deleteByGif(item) },
            { gFavouriteDao.insertGif(data) }
        )
        /*if (item != null) {

        } else {
            gFavouriteDao.insertGif(data)
        }*/
    }

    @WorkerThread
    suspend fun getGifByID(gifID: String) = flow {
        val item = gFavouriteDao.getGifById(gifID)
        emit(arrayListOf(item))
    }.flowOn(Dispatchers.IO)

    /*
    * This method is used to fetch both trending and searched gif data, I have optimized it bit with QueryMap to
    * avoid maintaining multiple list for gifs.
    * */
    @WorkerThread
    fun loadTrendingGif(page: Int, q: String, success: () -> Unit) = flow {

        //kotlinx.coroutines.delay(delay.toLong())

        //Build request map with url path
        val map = mutableMapOf<String, String>()
        map[api_key] = API_KEY
        map[limit] = pageCount.toString()
        map[offset] = page.toString()

        val queryPath = if (q.isEmpty()) {
            trend
        } else {
            map[query] = q
            search
        }

        val response = giphyService.fetchTrendingGif(queryPath, map)

        emit(if (response.isSuccessful) {
            val body = response.body()
            body?.let {
                IOTaskResult.OnSuccess(it)
            } ?: kotlin.run {
                IOTaskResult.OnFailed(FreshWorkApp.context.getString(R.string.no_result))
            }
        } else {
            // can be improve error handling by adding error code based message - I'M just passing all error as message
            IOTaskResult.OnFailed(
                "${FreshWorkApp.context.getString(R.string.error)} ${
                    response.errorBody()?.string()
                }"
            )
        })
    }.catch { e ->
        // can be improve error handling based on type of exception - I'M just passing all error as message
        emit(IOTaskResult.OnFailed("${FreshWorkApp.context.getString(R.string.error)} ${e.message}"))
        return@catch
    }.onCompletion {
        // higher order function to send callback
        success()
    }.flowOn(Dispatchers.IO)
}
