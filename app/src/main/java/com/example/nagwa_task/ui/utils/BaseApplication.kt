package com.example.nagwa_task.ui.utils

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application(){

    override fun onCreate() {
        super.onCreate()
    }
}