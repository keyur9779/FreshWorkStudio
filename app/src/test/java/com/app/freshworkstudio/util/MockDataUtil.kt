
package com.app.freshworkstudio.util

import com.app.freshworkstudio.model.entity.GifFavourite


object MockDataUtil {
    fun gifModel(): GifFavourite {
        return GifFavourite(
            gifID = "WBeiPUdgSViR49DANM",
            title = "mynameis",
            url = "https://media4.giphy.com/media/WBeiPUdgSViR49DANM/giphy.gif?cid=2226fb27260uxm7778p20612cbk0qevjf43yfj7t8oi71y5f&rid=giphy.gif&ct=g"
        )
    }
}
