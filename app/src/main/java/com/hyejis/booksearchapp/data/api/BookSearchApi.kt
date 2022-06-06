package com.hyejis.booksearchapp.data.api

import com.hyejis.booksearchapp.data.model.SearchResponse
import com.hyejis.booksearchapp.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * 카카오 책 검색 API
 * https://developers.kakao.com/docs/latest/ko/daum-search/dev-guide#search-book
 * **/
interface BookSearchApi {
    @Headers("Authorization: KakaoAK $API_KEY")
    @GET("v3/search/book")
    suspend fun searchBooks(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<SearchResponse>
}