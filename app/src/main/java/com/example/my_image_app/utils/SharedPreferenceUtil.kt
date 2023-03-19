package com.example.my_image_app.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceUtil(context: Context) {
    private val save : SharedPreferences = context.getSharedPreferences("save", Context.MODE_PRIVATE)

    fun getPref(key : String, default : String) : String{
        return save.getString(key, default).toString()
    }

    fun setPref(key : String, value : String){
        save.edit().putString(key, value).apply()
    }
}