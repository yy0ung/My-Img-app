package com.example.my_image_app

data class RetrofitSearchVideoDto(
    val meta : MetaDto,
    val documents : List<VideoDocumentsDto>
)
