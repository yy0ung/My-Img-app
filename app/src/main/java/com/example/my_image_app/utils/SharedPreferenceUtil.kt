package com.example.my_image_app.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.my_image_app.retrofit.dto.SaveItemDto
import com.google.gson.GsonBuilder


class SharedPreferenceUtil(context: Context) {
    private val save : SharedPreferences = context.getSharedPreferences("save", Context.MODE_PRIVATE)
    private val gson = GsonBuilder().create()

    fun getPref(key: String, default: String): ArrayList<SaveItemDto> {
        var rst = ArrayList<SaveItemDto>()
        val getSp = save.getString(key, default).toString()
        if(getSp!=default){
            rst = ArrayList(gson.fromJson(getSp, Array<SaveItemDto>::class.java).toList())
        }
        return rst
    }

    @SuppressLint("CommitPrefEdits")
    fun setPref(key : String, thumbnail : String){
        // 원래 배열 가져오기
        val temp = getPref(key, "null")
        // 원래 sp 초기화
        save.edit().clear()
        temp.add(SaveItemDto(thumbnail))
        save.edit().putString(key, gson.toJson(temp)).apply()
    }

    // sp 에서 선택한 이미지 지우기
    fun removePref(key: String, thumbnail : String){
        val curSp = getPref(key, "null")
        curSp.remove(SaveItemDto(thumbnail))
        save.edit().putString(key, gson.toJson(curSp)).apply()

    }
}
