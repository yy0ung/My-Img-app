package com.example.my_image_app

import android.app.Application
import com.example.my_image_app.utils.SharedPreferenceUtil

class GlobalApplication : Application() {
    companion object {
        lateinit var save : SharedPreferenceUtil
    }

    override fun onCreate() {
        save = SharedPreferenceUtil(applicationContext)
        super.onCreate()
    }
}