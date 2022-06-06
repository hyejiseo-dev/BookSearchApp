package com.hyejis.booksearchapp.data.repository

import androidx.lifecycle.LiveData
import com.hyejis.booksearchapp.data.api.RetrofitInstance.api
import com.hyejis.booksearchapp.data.db.BookSearchDatabase
import com.hyejis.booksearchapp.data.model.Book
import com.hyejis.booksearchapp.data.model.SearchResponse
import retrofit2.Response

class BookSearchRepositoryImpl(
    private val db: BookSearchDatabase
) : BookSearchRepository {
    override suspend fun searchBooks(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): Response<SearchResponse> {
        return api.searchBooks(query, sort, page, size)
    }

    override suspend fun insertBooks(book: Book) {
        db.bookSearchDao().insertBook(book)
    }

    override suspend fun deleteBooks(book: Book) {
        db.bookSearchDao().deleteBook(book)
    }

    override fun getFavoriteBooks(): LiveData<List<Book>> {
        return db.bookSearchDao().getFavoritBooks()
    }

}