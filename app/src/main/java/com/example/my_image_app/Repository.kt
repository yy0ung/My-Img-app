package com.example.my_image_app

import com.example.my_image_app.utils.API
import retrofit2.Call
import retrofit2.Response

class Repository {
    companion object{
        val instance = Repository()
    }

    private val iRetrofit : RetrofitInterface = RetrofitClient.getClient(API.BASE_URL)!!.create(RetrofitInterface::class.java)

    suspend fun searchImg(key : String, query : String, sort : String, page : Int, size : Int) : Response<RetrofitSearchImg>{
        return iRetrofit.searchImg(key, query, sort, page, size)
    }

    suspend fun searchVideo(key: String, query : String, sort : String, page : Int) : Response<RetrofitSearchVideoDto>{
        return iRetrofit.searchVideo(key, query, sort, page)
    }

    suspend fun getPref(key : String, default : String) : ArrayList<SaveItemDto>{
        return GlobalApplication.save.getPref(key, default)
    }

    fun test(key : String, query : String, sort : String, page : Int, size : Int) : Call<RetrofitSearchImg>{
        return iRetrofit.test(key, query, sort, page, size)
    }
}