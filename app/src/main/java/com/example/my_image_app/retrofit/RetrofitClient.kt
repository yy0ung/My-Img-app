package com.example.my_image_app.retrofit

import android.content.ContentValues.TAG
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var retrofitClient : Retrofit? = null

    fun getClient(baseUrl : String) : Retrofit? {
        val client = OkHttpClient.Builder()
        // 통신과정을 로그 찍어서 확인
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            //Log.d(TAG, "log: $message")
        }
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        client.addInterceptor(loggingInterceptor)

        if(retrofitClient == null){
            retrofitClient = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofitClient
    }
}