package com.example.my_image_app

import com.google.gson.annotations.SerializedName

data class DocumentsDto(
    @SerializedName("thumbnail_url")
    val thumbnail: String,
    val datetime: String,
)
