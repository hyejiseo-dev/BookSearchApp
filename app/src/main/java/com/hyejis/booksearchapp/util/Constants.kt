package com.hyejis.booksearchapp.util

import com.hyejis.booksearchapp.BuildConfig

object Constants {
    const val BASE_URL = "https://dapi.kakao.com/"
    const val API_KEY = BuildConfig.bookApiKey  //노출 위험! (Secret gradle plugin 설정)
}