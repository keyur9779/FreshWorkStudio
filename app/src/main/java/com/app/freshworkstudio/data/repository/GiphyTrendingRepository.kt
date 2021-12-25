package com.app.freshworkstudio.data.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.app.freshworkstudio.data.api.service.GiphyApiService
import com.app.freshworkstudio.model.IOTaskResult
import com.app.freshworkstudio.utils.DataUtils.apiKEY
import com.app.freshworkstudio.utils.DataUtils.delay
import com.app.freshworkstudio.utils.DataUtils.pageCount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion

class GiphyTrendingRepository constructor(
    private val giphyService: GiphyApiService
) {

    //Flow<IOTaskResult<T>>
    @WorkerThread
    fun loadTrendingGif(page: Int, success: () -> Unit) = flow<IOTaskResult<Any>> {

        //TODO : Please remove this delay when releasing to production as added to slow down downloading process bit
        kotlinx.coroutines.delay(delay.toLong())
        val response = giphyService.fetchTrendingGif(apiKEY, pageCount, page)

        if (response.isSuccessful) {
            emit(IOTaskResult.OnSuccess(response.body()!!))
        } else {
            emit(IOTaskResult.OnFailed("Retry with error ${response.errorBody()?.string()}"))
        }
    }.catch { e ->

        emit(IOTaskResult.OnFailed("Retry with error ${e.message}"))
        return@catch
    }.onCompletion { success() }.flowOn(Dispatchers.IO)


    /* val response = networkApiCall()
     if (response.isSuccessful) {
         response.body()?.let {
             emit(IOTaskResult.OnSuccess(it))
         }
             ?: emit(IOTaskResult.OnFailed(IOException("API call successful but empty response body")))
         return@flow
     }
     emit(
     IOTaskResult.OnFailed(
     IOException(
     "API call failed with error - ${response.errorBody()
     ?.string() ?: messageInCaseOfError}"
     )
     )
     )
     return@flow
 }.catch { e ->
     emit(IOTaskResult.OnFailed(IOException("Exception during network API call: ${e.message}")))
     return@catch
 }*/

    @WorkerThread
    fun searchGif(query: String, page: Int = 0, success: () -> Unit) = flow {

        Log.d("keyur", "fit1  and  page $page")

        val response = giphyService.fetchSearchGif("id", 20, page, query)
        if (response.isSuccessful) {
            emit(IOTaskResult.OnSuccess(response.body()!!))
        } else {
            emit(IOTaskResult.OnFailed("Retry with error ${response.errorBody()?.string()}"))
        }
    }.catch { e ->

        emit(IOTaskResult.OnFailed("Retry with error ${e.message}"))
        return@catch
    }.onCompletion { success() }.flowOn(Dispatchers.IO)


}
