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

    // image 와 video list 둘 다 마지막 페이지일 경우 isLastPage = true 로 변경하여 scroll 불가능하게 함.
    var isLastPage = false

    // 현재 불러온 결과 page 마지막 item 의 datetime 을 각각 저장
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
                val res = response.body()!!
                for (i in 0 until res.documents.size){
                    imgAndVideo.add(RstListDto(res.documents[i].thumbnail, res.documents[i].datetime))
                }
                imgLast = res.documents[res.documents.size-1].datetime
                isImagePageEnd = res.meta.isEnd
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
            if (response.isSuccessful && response.body()!=null) {
                val res = response.body()!!
                for (i in 0 until res.documents.size){
                    imgAndVideo.add(RstListDto(res.documents[i].thumbnail, res.documents[i].datetime))
                }
                videoLast = res.documents[res.documents.size-1].datetime
                isVideoPageEnd = res.meta.isEnd
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


    /*
    image search 결과 1 page 의 datetime 범위 : 2023-03-25 ~ 2023-03-26
                     2 page 의 datetime 범위 : 2023-03-24 ~ 2023-03-25
    video search 결과 1 page 의 datetime 범위 : 2023-03-20 ~ 2023-03-26

    위와 같이 가정할 때, page 1 의 결과끼리 모두 정렬하고 난 후 page 2 를 불러오면 page 1 의 datetime 보다 더 최신의 page 2 의 datetime 이 존재하게 된다.
    즉 각 page 안에서만 정렬이 되고 전체 list 의 최신순 정렬이 보장되지 않는다.

    따라서 각 페이지별 image 와 video 의 마지막 datetime 을 찾아 더 최신의 마지막 datetime 까지만 해당 페이지를 정렬하고,
    그 이후의 item 은 "remainList" 에 저장해 그 다음 페이지와 합쳐 정렬시킨다.
     */
    fun setTimeOrderList(originalList : ArrayList<RstListDto>, remainList : ArrayList<RstListDto>, size : Int) : ArrayList<RstListDto>{
        // 더 최신의 마지막 datetime 찾기 (불러온 image list 의 마지막, video list 의 마지막 중 더 최신인 것)
        val index = if(imgLast.toString()> videoLast.toString()){
            imgLast.toString()
        }else{
            videoLast.toString()
        }
        originalList.sortWith(compareByDescending { it.datetime })
        val lSize = remainList.size
        remainList.clear()

        // 불러온 페이지의 제일 마지막부터 "더 최신의 마지막 datetime" 까지 반복하여
        // 최신순 정렬이 완성된 list & 다음 페이지와 합쳐져 다시 정렬될 list 로 나눈다.
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