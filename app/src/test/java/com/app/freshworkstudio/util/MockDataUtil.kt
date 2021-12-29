/*
 * Designed and developed by 2019 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
