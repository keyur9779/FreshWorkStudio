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

    @Test
    fun loadTest() {
        runBlocking {

            val gif = gifModel()

            for (i in 0..1000) {
                db.gifFavouriteDao().insertGif(gif)
            }
            // used to launch task concurrently to avoid blocking
            val latch = CountDownLatch(1)
            val job = launch(Dispatchers.IO) {

                db.gifFavouriteDao().getGifList().collect { value ->

                    assertTrue(value.size == 1000)

                    // remove lock so can execute in runBlocking
                    latch.countDown()
                }
            }
            latch.await()
            job.cancel()
        }
    }
}
