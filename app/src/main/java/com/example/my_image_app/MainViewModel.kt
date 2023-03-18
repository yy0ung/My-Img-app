package com.example.my_image_app

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val _repositoriesSearchImg = MutableLiveData<RetrofitSearchImg>()
    val repositories1 : MutableLiveData<RetrofitSearchImg>
        get() = _repositoriesSearchImg

    init {
        Log.d(TAG, "start: ")
    }

    suspend fun searchImg(key : String, query : String, sort : String){
        viewModelScope.launch {
            repository.searchImg(key, query, sort).let { response ->
                if(response.isSuccessful){
                    _repositoriesSearchImg.postValue(response.body())
                }else{
                    Log.d(TAG, "searchImg: $response")
                }
            }
        }
    }
}