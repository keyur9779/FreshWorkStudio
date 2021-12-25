package com.app.freshworkstudio.data.api.service

import com.app.freshworkstudio.model.GiphyResponseModel
import com.skydoves.sandwich.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApiService {

    @GET("/v1/gifs/trending")
    suspend fun fetchTrendingGif(
        @Query("api_key") key: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<GiphyResponseModel>


    @GET("/v1/gifs/search")
    suspend fun fetchSearchGif(
        @Query("api_key") key: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("q") query: String
    ): Response<GiphyResponseModel>
}
