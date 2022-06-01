package com.hyejis.booksearchapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Meta(
    @field:Json(name = "is_end")
    val is_end: Boolean,
    @field:Json(name = "pageable_count")
    val pageable_count: Int,
    @field:Json(name = "total_count")
    val total_count: Int
)