package com.app.freshworkstudio.data.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.app.freshworkstudio.data.api.service.GiphyApiService
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GiphyTrendingRepository constructor(
    private val giphyService: GiphyApiService
) {


    @WorkerThread
    fun loadTrendingGif(page: Int = 0) = flow {

        Log.d("keyur", "fit1  and  page $page")

        val response = giphyService.fetchTrendingGif("id", 20,page)
        response.suspendOnSuccess {

            emit("Results.Success(data.results)")
            /*emit(Results.Success(data.results))

            val dataString = Gson().toJson(data)

            fileMovieDao.insertMovieByPage(FilmMovies(null, page, id, dataString))*/
        }
    }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun searchGif(query:String,page: Int = 0) = flow {

        Log.d("keyur", "fit1  and  page $page")

        val response = giphyService.fetchSearchGif("id", 20,page,query)
        response.suspendOnSuccess {

            emit("Results.Success(data.results)")
            /*emit(Results.Success(data.results))

            val dataString = Gson().toJson(data)

            fileMovieDao.insertMovieByPage(FilmMovies(null, page, id, dataString))*/
        }
    }.flowOn(Dispatchers.IO)


}
