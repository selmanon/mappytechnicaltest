package com.test.mappytest

import android.app.Application
import com.test.mappytest.data.FizzBuzzDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FizzBuzzApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}