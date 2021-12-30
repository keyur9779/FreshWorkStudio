package com.app.freshworkstudio.data.api.service

import com.app.freshworkstudio.model.GiphyResponseModel
import com.app.freshworkstudio.utils.DataUtils.urlPath
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 *
 * Network api list based on network and room
 * */
interface GiphyApiService {
    /*
    * generic method to fetch both trending and searched GIF
    * */
    @GET("/v1/gifs/{urlPath}")
    suspend fun fetchTrendingGif(
        @Path(value = urlPath) path: String,
        @QueryMap(encoded = true) params: Map<String, String>
    ): Response<GiphyResponseModel>
}
