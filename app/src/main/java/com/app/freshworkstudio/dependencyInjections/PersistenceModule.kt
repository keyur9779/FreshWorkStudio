package com.app.freshworkstudio.dependencyInjections

import android.content.Context
import androidx.room.Room
import com.app.freshworkstudio.data.room.AppDatabase
import com.app.freshworkstudio.data.room.GFavouriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    fun provideRoomDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, "freshWorkApp.db")
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideGenresDao(appDatabase: AppDatabase): GFavouriteDao {
        return appDatabase.gifFavouriteDao()
    }
}