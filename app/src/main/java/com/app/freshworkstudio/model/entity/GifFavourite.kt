package com.app.freshworkstudio.model.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity()//primaryKeys = [("id")]
data class GifFavourite(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gifID: String,
    val url: String
) : Parcelable