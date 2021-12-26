package com.app.freshworkstudio.data.room

import androidx.room.*
import com.app.freshworkstudio.model.entity.GifFavourite
import kotlinx.coroutines.flow.Flow

@Dao
interface GFavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertGenre(gif: GifFavourite)

    @Query("SELECT * FROM GifFavourite")
    suspend  fun getGifList(): Flow<List<GifFavourite>>

    @Delete
    suspend fun deleteByGif(gif: GifFavourite)

}
