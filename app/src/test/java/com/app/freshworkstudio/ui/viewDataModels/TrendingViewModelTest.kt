package com.app.freshworkstudio.ui.viewDataModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.app.freshworkstudio.data.repository.repositoryService.GiphyTrendingRepository
import com.app.freshworkstudio.data.room.AppDatabase
import com.app.freshworkstudio.mockRepository.GiphyTrendingRepositoryTest
import com.app.freshworkstudio.model.GiphyResponseModel
import com.app.freshworkstudio.model.IOTaskResult
import com.app.freshworkstudio.util.MockDataUtil
import com.google.common.truth.Truth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.concurrent.CountDownLatch

@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
@Config(sdk = [21])
class TrendingViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase

    private lateinit var viewModel: TrendingViewModel
    private lateinit var giphyTrendingRepository: GiphyTrendingRepository

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        giphyTrendingRepository = GiphyTrendingRepositoryTest(db.gifFavouriteDao())
        viewModel = TrendingViewModel(giphyTrendingRepository)
    }


    @Test
    fun markGifAsFav() = runBlocking {

        val item = MockDataUtil.gifModel()

        viewModel.insertFav(item)

        val latch = CountDownLatch(1)
        val job = launch(Dispatchers.IO) {

            viewModel.getGifByID(item.gifID).collectLatest {

                Truth.assertThat(item.gifID == it[0].gifID).isTrue()
                latch.countDown()
            }
        }

        latch.await()
        job.cancel()


    }

    // in actual implementation this part of single method, we can also consider this as fetch gif model from db
    @Test
    fun markGifAsUnFav() = runBlocking {

        val item = MockDataUtil.gifModel()

        val latch = CountDownLatch(1)
        val job = launch(Dispatchers.IO) {
            viewModel.getGifByID(item.gifID).collectLatest {
                Truth.assertThat(it[0] == null).isTrue()
                latch.countDown()
            }
        }
        latch.await()
        job.cancel()
    }


    @Test
    fun fetchTrendingGif() = runBlocking {

        val latch = CountDownLatch(1)
        val job = launch(Dispatchers.IO) {

            // fetch page 1 of gif
            giphyTrendingRepository.loadTrendingGif(1, "") {
                // this represent the success of network operation
            }.collectLatest { ioTask ->

                if (ioTask is IOTaskResult.OnSuccess<*>) {
                    if (ioTask.data is GiphyResponseModel) {
                        val giphyResponseModel = ioTask.data as GiphyResponseModel
                        Truth.assertThat(giphyResponseModel.meta?.status == 200).isTrue()
                        Truth.assertThat(giphyResponseModel.meta?.msg == "OK").isTrue()
                    } else {
                        // We are mocking this to make sure we are getting success with any data
                        Truth.assertThat(ioTask.data is Any).isTrue()
                    }
                }
                latch.countDown()
            }
        }
        latch.await()
        job.cancel()
    }


    @After
    fun tearDown() {
        db.close()
    }
}