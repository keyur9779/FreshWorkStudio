package com.app.freshworkstudio.ui.viewDataModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.app.freshworkstudio.data.repository.GiFavouriteRepositoryImpl
import com.app.freshworkstudio.data.room.AppDatabase
import com.app.freshworkstudio.util.MainCoroutineRule
import com.app.freshworkstudio.util.MockDataUtil
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.concurrent.CountDownLatch

@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
@Config(manifest = "src/main/AndroidManifest.xml")
class FavouriteGifViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    private lateinit var db: AppDatabase
    private lateinit var viewModel: FavouriteGifViewModel

    @Before
    fun setUp() {

        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        viewModel = FavouriteGifViewModel(GiFavouriteRepositoryImpl(db.gifFavouriteDao()))
    }

    @Test
    fun deleteItem() = runBlocking {

        val item = MockDataUtil.gifModel()
        db.gifFavouriteDao().insertGif(item)
        val fetchLastTime = db.gifFavouriteDao().getGifById(item.gifID)

        // used to launch task concurrently to avoid blocking
        val latch = CountDownLatch(1)
        val job = launch(Dispatchers.IO) {
            viewModel.deleteItem(fetchLastTime)
            latch.countDown()
        }
        latch.await()
        job.cancel()
        val fetchDeletedItem = db.gifFavouriteDao().getGifById(item.gifID)
        // close db manually as it conflicting with coroutines
        db.close()
        Assert.assertTrue(fetchDeletedItem == null)
    }

    //  we need to load item first to verify the list
    @ExperimentalCoroutinesApi
    @Test
    fun fetchItem() = runBlocking {
        val data = 1000
// view-model doesn't not have insert method so we have to write it manually - to mock entry
        val gif = MockDataUtil.gifModel()
        for (i in 0 until data) {
            db.gifFavouriteDao().insertGif(gif)
        }

        val latch = CountDownLatch(1)

        viewModel.localGifList.observeForever() {

            // static value to make sure condition matches

            assertThat(it.size).isEqualTo(data)
            latch.countDown()
        }

        latch.await()

    }

    @After
    fun closeDB() {
        db.close()
    }
}