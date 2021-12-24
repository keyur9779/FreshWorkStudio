package com.app.freshworkstudio

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FreshWorkApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}
