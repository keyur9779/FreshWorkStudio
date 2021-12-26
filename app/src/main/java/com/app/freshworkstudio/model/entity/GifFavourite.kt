package com.app.freshworkstudio.model.entity

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(primaryKeys = [("id")])
data class GifFavourite(
    var id: Int? = null,
    val gifID:String,
    val url: String
) : Parcelable