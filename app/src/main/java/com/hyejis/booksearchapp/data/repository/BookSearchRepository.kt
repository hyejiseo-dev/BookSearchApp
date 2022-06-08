package com.hyejis.booksearchapp.data.repository

import androidx.paging.PagingData
import com.hyejis.booksearchapp.data.model.Book
import com.hyejis.booksearchapp.data.model.SearchResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface BookSearchRepository {
    suspend fun searchBooks(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): Response<SearchResponse>

    //Room
    suspend fun insertBooks(book: Book)

    suspend fun deleteBooks(book: Book)

    fun getFavoriteBooks(): Flow<List<Book>>

    //Datastore
    suspend fun saveSortMode(mode: String)

    suspend fun getSortMode(): Flow<String>

    //Paging
    fun getFavoritePagingBooks(): Flow<PagingData<Book>>
}