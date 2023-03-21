package com.example.my_image_app

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
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

    suspend fun searchRst(key : String, query : String, sort : String, page : Int){
        viewModelScope.launch {
            repository.searchImg(key, query, sort, page).let { response ->
                if (response.isSuccessful && response.body()!=null) {
                    for (i in 0 until response.body()!!.documents.size){
                        imgAndVideo.add(RstListDto(response.body()!!.documents[i].thumbnail, response.body()!!.documents[i].datetime))
                    }
                }
            }
            repository.searchVideo(key, query, sort, page).let { response ->
                if (response.isSuccessful) {
                    for (i in 0 until response.body()!!.documents.size){
                        imgAndVideo.add(RstListDto(response.body()!!.documents[i].thumbnail, response.body()!!.documents[i].datetime))
                    }
                }
            }
            imgAndVideo.sortWith(compareByDescending{ it.datetime })
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