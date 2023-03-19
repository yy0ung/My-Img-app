package com.example.my_image_app

import com.example.my_image_app.utils.API
import retrofit2.Response
import retrofit2.create

class Repository {
    companion object{
        val instance = Repository()
    }

    private val iRetrofit : RetrofitInterface = RetrofitClient.getClient(API.BASE_URL)!!.create(RetrofitInterface::class.java)

    suspend fun searchImg(key : String, query : String, sort : String) : Response<RetrofitSearchImg>{
        return iRetrofit.searchImg(key, query, sort)
    }

    suspend fun searchVideo(key: String, query : String, sort : String) : Response<RetrofitSearchVideoDto>{
        return iRetrofit.searchVideo(key, query, sort)
    }

    suspend fun getPref(key : String, default : String) : String{
        return GlobalApplication.save.getPref(key, default)
    }
}