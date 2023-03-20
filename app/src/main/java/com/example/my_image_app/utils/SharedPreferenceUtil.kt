package com.example.my_image_app.utils

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.my_image_app.RstListDto
import com.example.my_image_app.SaveItemDto
import com.google.gson.GsonBuilder


class SharedPreferenceUtil(context: Context) {
    private val save : SharedPreferences = context.getSharedPreferences("save", Context.MODE_PRIVATE)
    private val gson = GsonBuilder().create()

    fun getPref(key: String, default: String): ArrayList<SaveItemDto> {
        var rst = ArrayList<SaveItemDto>()
        val getSp = save.getString(key, default).toString()
        if(getSp!=default){
            rst = gson.fromJson(getSp, Array<SaveItemDto>::class.java).toList() as ArrayList<SaveItemDto>
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

    // sp 에 있는 사진인지 확인하기
    fun checkPref(key: String, thumbnail : String) : Boolean {
        val curSp = getPref(key, "null")
        return curSp.contains(SaveItemDto(thumbnail))

    }

    // sp 에서 선택한 이미지 지우기
    fun removePref(key: String, thumbnail : String){
        val curSp = getPref(key, "null")
        curSp.remove(SaveItemDto(thumbnail))
        save.edit().putString(key, gson.toJson(curSp)).apply()

    }
}
