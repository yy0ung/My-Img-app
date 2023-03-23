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
            repository.fetchSearchRst(key, query, sort, page, size, imgAndVideo)
            imgAndVideo.sortWith(compareByDescending{ it.datetime })

            val index = if(repository.imgLast.toString()> repository.videoLast.toString()){
                repository.imgLast.toString()
            }else{
                repository.videoLast.toString()
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

    suspend fun loadNextPage(key : String,
                     query: String,
                     sort: String,
                     page: Int,
                     size: Int,
                     data : MutableList<RstListDto>,
                     adapter : SearchItemAdapter,
                     lastSize : Int, total : Int){
        viewModelScope.launch {
            Log.d(TAG, "loadNextPage: before $unallocList")
            repository.loadNextPage(key, query, sort, page, size, data, adapter, unallocList, lastSize, total)
            Log.d(TAG, "loadNextPage: after $unallocList")
        }
    }

    suspend fun getPref(key : String, default : String){
        viewModelScope.launch {
            repository.getPref(key, default, _repositoriesGetPref)
        }
    }
}