package com.example.my_image_app.utils

import android.app.Application

class GlobalApplication : Application() {
    companion object {
        lateinit var save : SharedPreferenceUtil
    }

    override fun onCreate() {
        save = SharedPreferenceUtil(applicationContext)
        super.onCreate()
    }
}