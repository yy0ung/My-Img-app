package com.example.my_image_app

import com.google.gson.annotations.SerializedName

data class MetaDto(
    @SerializedName("total_count")
    val totalCnt : Int,
    @SerializedName("pageable_count")
    val pageCnt : Int,
    @SerializedName("is_end")
    val isEnd : Boolean
)
