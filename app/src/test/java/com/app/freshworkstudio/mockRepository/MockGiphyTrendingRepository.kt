package com.app.freshworkstudio.mockRepository

import com.app.freshworkstudio.BuildConfig
import com.app.freshworkstudio.FreshWorkApp
import com.app.freshworkstudio.R
import com.app.freshworkstudio.data.api.service.GiphyApiService
import com.app.freshworkstudio.data.repository.repositoryService.GiphyTrendingRepository
import com.app.freshworkstudio.data.room.GFavouriteDao
import com.app.freshworkstudio.model.IOTaskResult
import com.app.freshworkstudio.model.entity.GifFavourite
import com.app.freshworkstudio.util.ApiAbstract
import com.app.freshworkstudio.utils.DataUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class GiphyTrendingRepositoryTest(private val gFavouriteDao: GFavouriteDao) :
    ApiAbstract<GiphyApiService>(), GiphyTrendingRepository {


    // we have to implement fake GiphyApiService to mock GiphyTrendingRepositoryiIMPL
    private var giphyService: GiphyApiService = createService(GiphyApiService::class.java)

    override suspend fun insertFav(data: GifFavourite) {

        gFavouriteDao.insertGif(data)

    }

    override suspend fun getGifByID(gifID: String) = flow {
        val item = gFavouriteDao.getGifById(gifID)
        emit(arrayListOf(item))
    }.flowOn(Dispatchers.IO)

    override fun loadTrendingGif(
        page: Int,
        q: String,
        success: () -> Unit
    ) = flow {

        //kotlinx.coroutines.delay(delay.toLong())

        //Build request map with url path
        val map = mutableMapOf<String, String>()
        map[DataUtils.api_key] = BuildConfig.API_KEY
        map[DataUtils.limit] = DataUtils.pageCount.toString()
        map[DataUtils.offset] = page.toString()

        val queryPath = if (q.isEmpty()) {
            DataUtils.trend
        } else {
            map[DataUtils.query] = q
            DataUtils.search
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