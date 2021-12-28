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

package com.app.freshworkstudio.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.freshworkstudio.BuildConfig
import com.app.freshworkstudio.data.api.service.GiphyApiService
import com.app.freshworkstudio.util.ApiAbstract
import com.app.freshworkstudio.utils.DataUtils
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.io.IOException

class GiphyServiceTest : ApiAbstract<GiphyApiService>() {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()
    private val requestHeader = HashMap<String, String>()

    private lateinit var service: GiphyApiService

    @Before
    fun initService() {
        this.service = createService(GiphyApiService::class.java)
    }

    @Throws(IOException::class)
    @Test
    fun fetchTrendingGif() = runBlocking {

        requestHeader[DataUtils.api_key] = BuildConfig.API_KEY
        requestHeader[DataUtils.limit] = DataUtils.pageCount.toString()
        requestHeader[DataUtils.offset] = DataUtils.loading.toString()

        val response = service.fetchTrendingGif(DataUtils.trend, requestHeader)

        assertThat(response.isSuccessful, `is`(true))
        assertThat(response.body()?.data?.size, `is`(20))
    }

    @Throws(IOException::class)
    @Test
    fun fetchTrendingGifWithNoKey() = runBlocking {

        requestHeader[DataUtils.limit] = DataUtils.pageCount.toString()
        requestHeader[DataUtils.offset] = DataUtils.loading.toString()

        val response = service.fetchTrendingGif(DataUtils.trend, requestHeader)

        assertThat(response.isSuccessful, `is`(false))
        assertThat(response.code(), `is`(401))
    }

    @Throws(IOException::class)
    @Test
    fun fetchSearchedGif() = runBlocking {
        requestHeader[DataUtils.api_key] = BuildConfig.API_KEY
        requestHeader[DataUtils.limit] = DataUtils.pageCount.toString()
        requestHeader[DataUtils.offset] = DataUtils.loading.toString()
        requestHeader[DataUtils.query] = "funny"

        val response = service.fetchTrendingGif(DataUtils.trend, requestHeader)

        assertThat(response.isSuccessful, `is`(true))
        assertThat(response.body()?.data?.size, `is`(20))
    }

    @Throws(IOException::class)
    @Test
    fun fetchSearchedGifWithEmptyQuery() = runBlocking {
        requestHeader[DataUtils.api_key] = BuildConfig.API_KEY
        requestHeader[DataUtils.limit] = DataUtils.pageCount.toString()
        requestHeader[DataUtils.offset] = DataUtils.loading.toString()
        requestHeader[DataUtils.query] = ""

        val response = service.fetchTrendingGif(DataUtils.trend, requestHeader)
        assertThat(response.isSuccessful, `is`(true))
    }

}
