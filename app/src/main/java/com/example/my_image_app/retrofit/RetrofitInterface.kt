package com.example.my_image_app.retrofit

import com.example.my_image_app.retrofit.dto.RetrofitSearchImgDto
import com.example.my_image_app.retrofit.dto.RetrofitSearchVideoDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitInterface {
    @GET("/v2/search/image")
    suspend fun searchImg(
        @Header("Authorization")key : String,
        @Query("query") query : String,
        @Query("sort") sort : String,
        @Query("page") page : Int,
        @Query("size") size : Int) : Response<RetrofitSearchImgDto>

    @GET("/v2/search/vclip")
    suspend fun searchVideo(
        @Header("Authorization")key : String,
        @Query("query") query : String,
        @Query("sort") sort : String,
        @Query("page") page : Int,
        @Query("size") size : Int) : Response<RetrofitSearchVideoDto>

}