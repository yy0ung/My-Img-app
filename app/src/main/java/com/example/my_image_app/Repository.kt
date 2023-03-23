package com.example.my_image_app

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import com.example.my_image_app.utils.API
import retrofit2.Response

class Repository {
    companion object{
        val instance = Repository()
    }

    private val iRetrofit : RetrofitInterface = RetrofitClient.getClient(API.BASE_URL)!!.create(RetrofitInterface::class.java)
    var isLastPage = false

    suspend fun searchImg(key : String, query : String, sort : String, page : Int, size : Int) : Response<RetrofitSearchImg>{
        return iRetrofit.searchImg(key, query, sort, page, size)
    }

    suspend fun searchVideo(key: String, query : String, sort : String, page : Int) : Response<RetrofitSearchVideoDto>{
        return iRetrofit.searchVideo(key, query, sort, page)
    }

    suspend fun getPref(key : String, default : String) : ArrayList<SaveItemDto>{
        return GlobalApplication.save.getPref(key, default)
    }

    @SuppressLint("NotifyDataSetChanged")
    suspend fun loadNextPage(
        key : String,
        query: String,
        sort: String,
        page: Int,
        size: Int,
        data : MutableList<RstListDto>,
        adapter : SearchItemAdapter,
        lastList : ArrayList<RstListDto>){
        // 성공 못했을때 분기처리
        val resImg = searchImg(key, query, sort, page, size).body()?.documents!!
        val resVideo = searchVideo(key, query, sort, page).body()?.documents!!

        val isImgEnd = searchImg(key, query, sort, page, size).body()?.meta?.isEnd
        val isVideoEnd = searchVideo(key, query, sort, page).body()?.meta?.isEnd

        val temp = ArrayList<RstListDto>()
        temp.addAll(lastList)
        if(isImgEnd!=true && isVideoEnd!=true){
            for(i in resImg.indices){
                temp.add(RstListDto(resImg[i].thumbnail, resImg[i].datetime))
                temp.add(RstListDto(resVideo[i].thumbnail, resVideo[i].datetime))
            }
        }else if(isImgEnd==true && isVideoEnd!=true){
            for(i in resImg.indices){
                temp.add(RstListDto(resImg[i].thumbnail, resImg[i].datetime))
            }
        }else if(isImgEnd!=true && isVideoEnd==true){
            for(i in resImg.indices){
                temp.add(RstListDto(resVideo[i].thumbnail, resVideo[i].datetime))
            }
        }else{
            isLastPage = true
        }


        temp.sortWith(compareByDescending { it.datetime })
        data.addAll(temp)
        adapter.notifyDataSetChanged()
        Log.d(ContentValues.TAG, "onResponse: ㅇㅇㅇㅇㅇ")
    }
}