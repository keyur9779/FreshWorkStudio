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
    fun insertGenre(gif: GifFavourite)

    @Query("SELECT * FROM GifFavourite")
    fun getGifList(): Flow<List<GifFavourite>>

    @Query("SELECT COUNT(gifID) FROM GifFavourite WHERE gifID = :gifID")
    fun getGifById(gifID: String): Int


    @Delete
    fun deleteByGif(gif: GifFavourite)

}
