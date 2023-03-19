package com.example.my_image_app.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.my_image_app.SaveListDto
import com.google.gson.GsonBuilder


class SharedPreferenceUtil(context: Context) {
    private val save : SharedPreferences = context.getSharedPreferences("save", Context.MODE_PRIVATE)
    private val gson = GsonBuilder().create()

    fun getPref(key: String, default: String): ArrayList<SaveListDto> {
        var rst = ArrayList<SaveListDto>()
        val temp = save.getString("key", "no").toString()
        if(temp!="no"){
            rst = gson.fromJson(temp, Array<SaveListDto>::class.java).toList() as ArrayList<SaveListDto>
        }

        return rst
    }

    @SuppressLint("CommitPrefEdits")
    fun setPref(key : String, value : SaveListDto){
        val temp = getPref(key, "no")
        save.edit().clear()
        temp.add(SaveListDto(value.thumbnail, value.datetime))
        save.edit().putString(key, gson.toJson(temp)).apply()

    }
}
