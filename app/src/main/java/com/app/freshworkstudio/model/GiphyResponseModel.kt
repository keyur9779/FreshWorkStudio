package com.app.freshworkstudio.model

data class GiphyResponseModel(
    var data: List<GifData> = emptyList(),
    var meta: Meta? = null,
    var pagination: Pagination? = null,
)