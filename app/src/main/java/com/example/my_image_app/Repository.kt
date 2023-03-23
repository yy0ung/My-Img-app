package com.example.my_image_app

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.my_image_app.utils.API
import retrofit2.Response

class Repository {
    companion object{
        val instance = Repository()
    }

    private val iRetrofit : RetrofitInterface = RetrofitClient.getClient(API.BASE_URL)!!.create(RetrofitInterface::class.java)
    var isLastPage = false
    var imgLast : String? = null
    var videoLast : String? = null
    var isImageEnd : Boolean? = null
    var isVideoEnd : Boolean? = null

    suspend fun fetchSearchRst(
        key : String, query : String, sort : String, page : Int, size : Int, imgAndVideo : ArrayList<RstListDto>){
        iRetrofit.searchImg(key, query, sort, page, size).let { response ->
            if (response.isSuccessful && response.body()!=null) {
                for (i in 0 until response.body()!!.documents.size){
                    imgAndVideo.add(RstListDto(response.body()!!.documents[i].thumbnail, response.body()!!.documents[i].datetime))
                }
                imgLast = response.body()!!.documents[response.body()!!.documents.size-1].datetime
                isImageEnd = response.body()!!.meta.isEnd
            }
        }
        iRetrofit.searchVideo(key, query, sort, page).let { response ->
            if (response.isSuccessful) {
                for (i in 0 until response.body()!!.documents.size){
                    imgAndVideo.add(RstListDto(response.body()!!.documents[i].thumbnail, response.body()!!.documents[i].datetime))
                }
                videoLast = response.body()!!.documents[response.body()!!.documents.size-1].datetime
                isVideoEnd = response.body()!!.meta.isEnd
            }
        }
    }

    suspend fun searchImg(key : String, query : String, sort : String, page : Int, size : Int) : Response<RetrofitSearchImg>{
        return iRetrofit.searchImg(key, query, sort, page, size)
    }

    suspend fun searchVideo(key: String, query : String, sort : String, page : Int) : Response<RetrofitSearchVideoDto>{
        return iRetrofit.searchVideo(key, query, sort, page)
    }

    suspend fun getPref(key : String, default : String, list : MutableLiveData<ArrayList<SaveItemDto>>){
        val saveList = GlobalApplication.save.getPref(key, default)
        list.postValue(saveList)
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
        lastList : ArrayList<RstListDto>, lastSize : Int, total : Int){
        // 성공 못했을때 분기처리
        val resImg = searchImg(key, query, sort, page, size).body()?.documents!!
        val resVideo = searchVideo(key, query, sort, page).body()?.documents!!

        val isImgEnd = searchImg(key, query, sort, page, size).body()?.meta?.isEnd
        val isVideoEnd = searchVideo(key, query, sort, page).body()?.meta?.isEnd
        val temp = ArrayList<RstListDto>()
        temp.addAll(lastList)
        val imgL = resImg[resImg.size-1].datetime
        val vL = resVideo[resVideo.size-1].datetime


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

        val index = if(imgL.toString()> vL.toString()){
            imgL.toString()
        }else{
            vL.toString()
        }
        temp.sortWith(compareByDescending { it.datetime })
        val lSize = lastList.size
        lastList.clear()

        for(i in 19+lSize downTo 0){
            if(temp[i].datetime==index){
                break
            }else{
                lastList.add(temp[i])
                temp.remove(temp[i])
            }
        }

        data.addAll(temp)
        adapter.notifyItemRangeChanged(lastSize, total)
        //adapter.notifyDataSetChanged()
        Log.d(ContentValues.TAG, "onResponse: ㅇㅇㅇㅇㅇ")
    }
}