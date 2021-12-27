package com.app.freshworkstudio.data.repository

import androidx.annotation.WorkerThread
import com.app.freshworkstudio.BuildConfig.API_KEY
import com.app.freshworkstudio.data.api.service.GiphyApiService
import com.app.freshworkstudio.data.room.GFavouriteDao
import com.app.freshworkstudio.model.IOTaskResult
import com.app.freshworkstudio.model.entity.GifFavourite
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
class GiphyTrendingRepository constructor(
    private val giphyService: GiphyApiService,
    private val gFavouriteDao: GFavouriteDao
) {

    @WorkerThread
    suspend fun insertFav(data: GifFavourite) {

        // Just making sure we are not duplicating by any means, as url of
        // gif are getting different all time, so sometimes room is not able to resolve conflicts replace
        val item = gFavouriteDao.getGifById(data.gifID)

        if (item == null) {
            gFavouriteDao.insertGenre(data)
        } else {
            gFavouriteDao.deleteByGif(item)
        }
    }

    /*
    * This method is used to fetch both trending and searched gif data, I have optimized it bit with QueryMap to
    * avoid maintaining multiple list for gifs.
    * */
    @WorkerThread
    fun loadTrendingGif(page: Int, q: String, success: () -> Unit) = flow {

        //TODO : Please remove this delay when releasing to production as added to slow down downloading process bit
        kotlinx.coroutines.delay(delay.toLong())


        val map = mutableMapOf<String, String>()
        map[api_key] = API_KEY
        map[limit] = pageCount.toString()
        map[offset] = page.toString()
        val queryPath = if (q.isEmpty()) {
            trend
//            "$trend?api_key=$apiKEY&limit=$pageCount&offset=$page"
        } else {
            map[query] = q
            search
//            "$search?api_key=$apiKEY&limit=$pageCount&offset=$page&q=$query"

        }

        val response =
            giphyService.fetchTrendingGif(queryPath/*, apiKEY, pageCount, page,query*/, map)

        if (response.isSuccessful) {
            val body = response.body()
            body?.let {
                if (it.data.isNotEmpty()) {
                    emit(IOTaskResult.OnSuccess(it))
                } else {
                    emit(IOTaskResult.OnFailed("No Result for your searched query."))
                }
            } ?: kotlin.run {
                emit(IOTaskResult.OnFailed("No Result for your searched query."))
            }
        } else {
            // can be improve error handling by adding error code based message - I'M just passing all error as message
            emit(IOTaskResult.OnFailed("Retry with error ${response.errorBody()?.string()}"))
        }
    }.catch { e ->
        // can be improve error handling based on type of exception - I'M just passing all error as message
        emit(IOTaskResult.OnFailed("Retry with error ${e.message}"))
        return@catch
    }.onCompletion {
        // higher order function to send callback
        success()
    }.flowOn(Dispatchers.IO)
}
