package com.app.freshworkstudio.model

data class GiphyResponseModel(
    val data: List<Data>,
    val meta: Meta,
    val pagination: Pagination
)