package com.app.freshworkstudio.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.app.freshworkstudio.model.entity.GifFavourite
import kotlinx.coroutines.flow.Flow


@Dao
interface GFavouriteDao {
    @Insert()
    suspend fun insertGenre(gif: GifFavourite)

    @Query("SELECT * FROM GifFavourite")
    fun getGifList(): Flow<List<GifFavourite>>

    @Query("SELECT * FROM GifFavourite WHERE gifID = :gifID")
    suspend fun getGifById(gifID: String): GifFavourite

    @Delete
    suspend fun deleteByGif(gif: GifFavourite)

}
