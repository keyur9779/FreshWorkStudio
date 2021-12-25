package com.app.freshworkstudio.model

data class GiphyResponseModel(
    var data: List<GifData>? = null,
    var meta: Meta? = null,
    var pagination: Pagination? = null,
)