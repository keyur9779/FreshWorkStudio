package com.app.freshworkstudio.data.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.app.freshworkstudio.data.api.service.GiphyApiService
import com.app.freshworkstudio.model.IOTaskResult
import com.app.freshworkstudio.utils.DataUtils
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

class GiphyTrendingRepository constructor(
    private val giphyService: GiphyApiService
) {


    /*
    * This method is used to fetch both trending and searched gif data, I have optimized it bit with QueryMap to
    * avoid maintaining multiple list for gifs.
    * */
    @WorkerThread
    fun loadTrendingGif(page: Int, q: String, success: () -> Unit) = flow{

        //TODO : Please remove this delay when releasing to production as added to slow down downloading process bit
        kotlinx.coroutines.delay(delay.toLong())


        val map = mutableMapOf<String, String>()
        map[api_key] = apiKEY
        map[limit] = pageCount.toString()
        map[offset] = page.toString()
        val queryPath = if (query.isEmpty()) {
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
            emit(IOTaskResult.OnSuccess(response.body()!!))
        } else {
            emit(IOTaskResult.OnFailed("Retry with error ${response.errorBody()?.string()}"))
        }
    }.catch { e ->

        emit(IOTaskResult.OnFailed("Retry with error ${e.message}"))
        return@catch
    }.onCompletion { success() }.flowOn(Dispatchers.IO)
/*


    @WorkerThread
    fun searchGif(query: String, page: Int = DataUtils.item, success: () -> Unit) = flow {

        Log.d("keyur", "fit1  and  page $page")

        val response = giphyService.fetchSearchGif(apiKEY, pageCount, page, query)
        if (response.isSuccessful) {
            emit(IOTaskResult.OnSuccess(response.body()!!))
        } else {
            emit(IOTaskResult.OnFailed("Retry with error ${response.errorBody()?.string()}"))
        }
    }.catch { e ->
        emit(IOTaskResult.OnFailed("Retry with error ${e.message}"))
        return@catch
    }.onCompletion { success() }.flowOn(Dispatchers.IO)
*/


}
