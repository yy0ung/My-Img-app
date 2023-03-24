package com.example.my_image_app.retrofit.dto

import com.google.gson.annotations.SerializedName

data class ImgDocumentsDto(
    @SerializedName("thumbnail_url")
    val thumbnail: String,
    val datetime: String,
)
