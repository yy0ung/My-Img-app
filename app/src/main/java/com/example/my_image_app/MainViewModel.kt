package com.example.my_image_app

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.security.auth.callback.Callback
import kotlin.collections.ArrayList

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val _repositoriesSearchRst = MutableLiveData<ArrayList<RstListDto>>()
    val repositories1 : MutableLiveData<ArrayList<RstListDto>>
        get() = _repositoriesSearchRst
    private val _repositoriesGetPref = MutableLiveData<ArrayList<SaveItemDto>>()
    val repositories3 : MutableLiveData<ArrayList<SaveItemDto>>
        get() = _repositoriesGetPref

    init {
        Log.d(TAG, "start: ")
    }
    
    private val imgAndVideo = ArrayList<RstListDto>()
    val unallocList = ArrayList<RstListDto>()
    private val key = "KakaoAK 771b6707ddc9077bf7ad7c7ae0a92272"
    var imgLast : String? = null
    var videoLast : String? = null


    suspend fun searchRst(key : String, query : String, sort : String, page : Int, size : Int){
        viewModelScope.launch {
            val bool = GlobalApplication.save.getPref("key", "null")
            repository.searchImg(key, query, sort, page, size).let { response ->
                if (response.isSuccessful && response.body()!=null) {
                    for (i in 0 until response.body()!!.documents.size){
                        imgAndVideo.add(RstListDto(response.body()!!.documents[i].thumbnail, response.body()!!.documents[i].datetime))
                    }
                    imgLast = response.body()!!.documents[response.body()!!.documents.size-1].datetime
                }
            }
            repository.searchVideo(key, query, sort, page).let { response ->
                if (response.isSuccessful) {
                    for (i in 0 until response.body()!!.documents.size){
                        imgAndVideo.add(RstListDto(response.body()!!.documents[i].thumbnail, response.body()!!.documents[i].datetime))
                    }
                    videoLast = response.body()!!.documents[response.body()!!.documents.size-1].datetime
                }
            }
            imgAndVideo.sortWith(compareByDescending{ it.datetime })

            val index = if(imgLast.toString()> videoLast.toString()){
                imgLast.toString()
            }else{
                videoLast.toString()
            }
            for(i in 19 downTo 0){
                if(imgAndVideo[i].datetime==index){
                    break
                }else{
                    unallocList.add(imgAndVideo[i])
                    imgAndVideo.remove(imgAndVideo[i])
                }
            }
            Log.d(TAG, "searchRst: $index")
            Log.d(TAG, "searchRst: $imgAndVideo")
            Log.d(TAG, "searchRst: $unallocList")

            _repositoriesSearchRst.postValue(imgAndVideo)

        }
    }


    suspend fun getPref(key : String, default : String){
        viewModelScope.launch {
            repository.getPref(key, default).let { response->
                _repositoriesGetPref.postValue(response)
            }
        }
    }
}