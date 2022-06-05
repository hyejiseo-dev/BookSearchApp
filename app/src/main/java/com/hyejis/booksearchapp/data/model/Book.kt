package com.hyejis.booksearchapp.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize   //safeargs를 위한 설정
@JsonClass(generateAdapter = true)
@Entity(tableName = "books")
data class Book(
    val authors: List<String>,  //컨버터 사용
    val contents: String,
    val datetime: String,
    @PrimaryKey(autoGenerate = false) //고유키 지정
    val isbn: String,
    val price: Int,
    val publisher: String,
    @ColumnInfo(name = "sale_price")  //키값의 이름을 설정
    @field:Json(name = "sale_price")
    val sale_price: Int,
    val status: String,
    val thumbnail: String,
    val title: String,
    val translators: List<String>,
    val url: String
) : Parcelable