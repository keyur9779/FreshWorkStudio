package com.app.freshworkstudio.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.freshworkstudio.model.entity.GifFavourite

@Dao
interface GFavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenre(genre: List<GifFavourite>)

    @Query("SELECT * FROM GifFavourite")
    fun getGenreList(): List<GifFavourite>
}
