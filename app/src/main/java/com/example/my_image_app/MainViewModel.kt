package com.example.my_image_app

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_image_app.retrofit.dto.RstListDto
import com.example.my_image_app.retrofit.dto.SaveItemDto
import com.example.my_image_app.search.SearchItemAdapter
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val _repositoriesSearchRst = MutableLiveData<ArrayList<RstListDto>>()
    val repositories1 : MutableLiveData<ArrayList<RstListDto>>
        get() = _repositoriesSearchRst

    private val _repositoriesGetPref = MutableLiveData<ArrayList<SaveItemDto>>()
    val repositories2 : MutableLiveData<ArrayList<SaveItemDto>>
        get() = _repositoriesGetPref

    private var imgAndVideo = ArrayList<RstListDto>()
    val remainList = ArrayList<RstListDto>()


    init {
        imgAndVideo.clear()
        remainList.clear()
    }


    suspend fun searchRst(key : String,
                          query : String,
                          sort : String,
                          page : Int,
                          size : Int){
        viewModelScope.launch {
            imgAndVideo.clear()
            repository.fetchSearchRst(key, query, sort, page, size, imgAndVideo)
            imgAndVideo.sortWith(compareByDescending{ it.datetime })
            val list = repository.setTimeOrderList(imgAndVideo, remainList, size)
            _repositoriesSearchRst.postValue(list)

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
            repository.loadNextPage(key, query, sort, page, size, data, adapter, remainList, lastSize, total)
        }
    }

    fun getPref(key : String, default : String){
        viewModelScope.launch {
            repository.getPref(key, default, _repositoriesGetPref)
        }
    }
}