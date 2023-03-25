package com.example.my_image_app

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.example.my_image_app.retrofit.RetrofitClient
import com.example.my_image_app.retrofit.RetrofitInterface
import com.example.my_image_app.retrofit.dto.RstListDto
import com.example.my_image_app.retrofit.dto.SaveItemDto
import com.example.my_image_app.search.SearchItemAdapter
import com.example.my_image_app.utils.API
import com.example.my_image_app.utils.GlobalApplication

class Repository {
    companion object{
        val instance = Repository()
    }

    private val iRetrofit : RetrofitInterface =
        RetrofitClient.getClient(API.BASE_URL)!!.create(RetrofitInterface::class.java)
    var isLastPage = false
    private var imgLast : String? = null
    private var videoLast : String? = null
    private var isImagePageEnd : Boolean? = null
    private var isVideoPageEnd : Boolean? = null

    suspend fun fetchSearchRst(key : String,
                               query : String,
                               sort : String,
                               page : Int,
                               size : Int,
                               imgAndVideo : ArrayList<RstListDto>){
        searchImg(key, query, sort, page, size, imgAndVideo)
        searchVideo(key, query, sort, page, size, imgAndVideo)
    }

    private suspend fun searchImg(key : String,
                                  query : String,
                                  sort : String,
                                  page : Int,
                                  size : Int,
                                  imgAndVideo : ArrayList<RstListDto>){
        iRetrofit.searchImg(key, query, sort, page, size).let { response ->
            if (response.isSuccessful && response.body()!=null) {
                for (i in 0 until response.body()!!.documents.size){
                    imgAndVideo.add(RstListDto(response.body()!!.documents[i].thumbnail, response.body()!!.documents[i].datetime))
                }
                imgLast = response.body()!!.documents[response.body()!!.documents.size-1].datetime
                isImagePageEnd = response.body()!!.meta.isEnd
            }
        }
    }

    private suspend fun searchVideo(key: String,
                                    query : String,
                                    sort : String,
                                    page : Int,
                                    size : Int,
                                    imgAndVideo : ArrayList<RstListDto>) {
        iRetrofit.searchVideo(key, query, sort, page, size).let { response ->
            if (response.isSuccessful) {
                for (i in 0 until response.body()!!.documents.size){
                    imgAndVideo.add(RstListDto(response.body()!!.documents[i].thumbnail, response.body()!!.documents[i].datetime))
                }
                videoLast = response.body()!!.documents[response.body()!!.documents.size-1].datetime
                isVideoPageEnd = response.body()!!.meta.isEnd
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    suspend fun loadNextPage(key : String,
                             query: String,
                             sort: String,
                             page: Int,
                             size: Int,
                             data : MutableList<RstListDto>,
                             adapter : SearchItemAdapter,
                             remainList : ArrayList<RstListDto>,
                             lastSize : Int,
                             total : Int){

        val isImgEnd = iRetrofit.searchImg(key, query, sort, page, size).body()?.meta?.isEnd
        val isVideoEnd = iRetrofit.searchVideo(key, query, sort, page, size).body()?.meta?.isEnd
        val tempList = ArrayList<RstListDto>()

        tempList.addAll(remainList)

        if(isImgEnd!=true && isVideoEnd!=true){
            fetchSearchRst(key, query, sort, page, size, tempList)
        }else if(isImgEnd==true && isVideoEnd!=true){
            searchVideo(key, query, sort, page, size, tempList)
        }else if(isImgEnd!=true && isVideoEnd==true){
            searchImg(key, query, sort, page, size, tempList)
        }else{
            isLastPage = true
        }

        setTimeOrderList(tempList, remainList, size)
        data.addAll(tempList)
        adapter.notifyItemRangeChanged(lastSize, total)
    }

    fun setTimeOrderList(originalList : ArrayList<RstListDto>, remainList : ArrayList<RstListDto>, size : Int) : ArrayList<RstListDto>{
        val index = if(imgLast.toString()> videoLast.toString()){
            imgLast.toString()
        }else{
            videoLast.toString()
        }
        originalList.sortWith(compareByDescending { it.datetime })
        val lSize = remainList.size
        remainList.clear()

        for(i in (size*2)+lSize-1 downTo 0){
            if(originalList[i].datetime==index){
                break
            }else{
                remainList.add(originalList[i])
                originalList.remove(originalList[i])
            }
        }
        return originalList
    }

    fun getPref(key : String, default : String, list : MutableLiveData<ArrayList<SaveItemDto>>){
        val saveList = GlobalApplication.save.getPref(key, default)
        list.postValue(saveList)
    }
}