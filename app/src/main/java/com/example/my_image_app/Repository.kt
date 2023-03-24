package com.example.my_image_app

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.my_image_app.utils.API
import com.example.my_image_app.utils.GlobalApplication
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
        searchImg(key, query, sort, page, size, imgAndVideo)
        searchVideo(key, query, sort, page, imgAndVideo)
    }

    private suspend fun searchImg(key : String, query : String, sort : String, page : Int, size : Int, imgAndVideo : ArrayList<RstListDto>){
        iRetrofit.searchImg(key, query, sort, page, size).let { response ->
            if (response.isSuccessful && response.body()!=null) {
                for (i in 0 until response.body()!!.documents.size){
                    imgAndVideo.add(RstListDto(response.body()!!.documents[i].thumbnail, response.body()!!.documents[i].datetime))
                }
                imgLast = response.body()!!.documents[response.body()!!.documents.size-1].datetime
                isImageEnd = response.body()!!.meta.isEnd
            }
        }
    }

    private suspend fun searchVideo(key: String, query : String, sort : String, page : Int, imgAndVideo : ArrayList<RstListDto>) {
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

        val isImgEnd = iRetrofit.searchImg(key, query, sort, page, size).body()?.meta?.isEnd
        val isVideoEnd = iRetrofit.searchVideo(key, query, sort, page).body()?.meta?.isEnd
        val temp = ArrayList<RstListDto>()
        temp.addAll(lastList)

        if(isImgEnd!=true && isVideoEnd!=true){
            fetchSearchRst(key, query, sort, page, size, temp)
        }else if(isImgEnd==true && isVideoEnd!=true){
            searchVideo(key, query, sort, page, temp)
        }else if(isImgEnd!=true && isVideoEnd==true){
            searchImg(key, query, sort, page, size, temp)
        }else{
            isLastPage = true
        }

        setTimeOrderList(temp, lastList)
        data.addAll(temp)
        adapter.notifyItemRangeChanged(lastSize, total)
        //adapter.notifyDataSetChanged()
    }

    fun setTimeOrderList(arr : ArrayList<RstListDto>, lastList : ArrayList<RstListDto>){
        val index = if(imgLast.toString()> videoLast.toString()){
            imgLast.toString()
        }else{
            videoLast.toString()
        }
        arr.sortWith(compareByDescending { it.datetime })
        val lSize = lastList.size
        lastList.clear()

        for(i in 19+lSize downTo 0){
            if(arr[i].datetime==index){
                break
            }else{
                lastList.add(arr[i])
                arr.remove(arr[i])
            }
        }
    }
}