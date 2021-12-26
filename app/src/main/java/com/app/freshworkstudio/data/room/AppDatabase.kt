package com.app.freshworkstudio.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.freshworkstudio.model.entity.GifFavourite

@Database(
    entities = [(GifFavourite::class)],
    version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gifFavouriteDao(): GFavouriteDao
}
