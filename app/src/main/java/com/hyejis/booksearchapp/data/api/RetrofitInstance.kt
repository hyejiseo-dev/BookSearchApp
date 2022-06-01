package com.hyejis.booksearchapp.data.api

import com.hyejis.booksearchapp.util.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance { //싱글턴 패턴

    private val okHttpClient: OkHttpClient by lazy {  //로그캣에서 패킷 내용 모니터링 가능
        val httpLoggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
    }

    private val retrofit: Retrofit by lazy {  //레트로핏 객체 생성
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    val api: BookSearchApi by lazy {  //api 인스턴스 생성
        retrofit.create(BookSearchApi::class.java)
    }

}