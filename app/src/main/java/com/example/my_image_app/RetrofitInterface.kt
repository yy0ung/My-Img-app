package com.example.my_image_app

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitInterface {
    @GET("/v2/search/image")
    //@Headers("Content-type: application/json")
    suspend fun searchImg(@Header("Authorization")key : String, @Query("query") query : String, @Query("sort") sort : String)
        : Response<RetrofitSearchImg>

    @GET("/v2/search/vclip")
    suspend fun searchVideo(@Header("Authorization")key : String, @Query("query") query : String, @Query("sort") sort : String)
            : Response<RetrofitSearchVideoDto>

}