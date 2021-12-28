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

package com.app.freshworkstudio.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.freshworkstudio.util.MockDataUtil.gifModel
import junit.framework.Assert.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.concurrent.CountDownLatch

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21])
class GifDaoTest : RoomDatabase() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    /*
    * Validate db insertion operation
    * */
    @Test
    fun insertRowValidation() {
        val gif = gifModel()
        runBlocking {
            db.gifFavouriteDao().insertGif(gif)
            val loadFromDB = db.gifFavouriteDao().getGifById(gif.gifID)
            assertThat(loadFromDB.gifID, `is`(gif.gifID))
            assertThat(loadFromDB.url, `is`(gif.url))
        }
    }


    /*
    * validate favourite list fetching query
    * */
    @Test
    fun getGifListValidation() {
        runBlocking {
            val gif = gifModel()
            db.gifFavouriteDao().insertGif(gif)

            // used to launch task concurrently to avoid blocking
            val latch = CountDownLatch(1)
            val job = launch(Dispatchers.IO) {

                db.gifFavouriteDao().getGifList().collect { value ->

                    assertTrue(value.isNotEmpty())

                    // remove lock so can execute in runBlocking
                    latch.countDown()
                }
            }
            latch.await()
            job.cancel()
        }
    }


    /*
    * Validate un-favourite db operation
    * */
    @Test
    fun deleteRow() {
        val gif = gifModel()
        runBlocking {
            db.gifFavouriteDao().insertGif(gif)
            val loadFromDB = db.gifFavouriteDao().getGifById(gif.gifID)
            assertThat(loadFromDB.gifID, `is`(gif.gifID))
            assertThat(loadFromDB.url, `is`(gif.url))
            db.gifFavouriteDao().deleteByGif(loadFromDB)
            val loadDeleted = db.gifFavouriteDao().getGifById(gif.gifID)
            assertNull(loadDeleted)


        }
    }
}
