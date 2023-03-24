package com.example.my_image_app

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_image_app.retrofit.dto.RstListDto
import com.example.my_image_app.retrofit.dto.SaveItemDto
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val _repositoriesSearchRst = MutableLiveData<ArrayList<RstListDto>>()
    val repositories1 : MutableLiveData<ArrayList<RstListDto>>
        get() = _repositoriesSearchRst
    private val _repositoriesGetPref = MutableLiveData<ArrayList<SaveItemDto>>()
    val repositories3 : MutableLiveData<ArrayList<SaveItemDto>>
        get() = _repositoriesGetPref

    var imgAndVideo = ArrayList<RstListDto>()
    val unallocList = ArrayList<RstListDto>()


    init {
        Log.d(TAG, "start: ")
        imgAndVideo.clear()
        unallocList.clear()
    }
    
    private val key = "KakaoAK 771b6707ddc9077bf7ad7c7ae0a92272"


    suspend fun searchRst(key : String, query : String, sort : String, page : Int, size : Int){
        viewModelScope.launch {
            imgAndVideo.clear()
            repository.fetchSearchRst(key, query, sort, page, size, imgAndVideo)
            imgAndVideo.sortWith(compareByDescending{ it.datetime })

            repository.setTimeOrderList(imgAndVideo, unallocList)
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
            repository.loadNextPage(key, query, sort, page, size, data, adapter, unallocList, lastSize, total)
        }
    }

    suspend fun getPref(key : String, default : String){
        viewModelScope.launch {
            repository.getPref(key, default, _repositoriesGetPref)
        }
    }
}