package com.hyejis.booksearchapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hyejis.booksearchapp.data.api.RetrofitInstance.api
import com.hyejis.booksearchapp.data.db.BookSearchDatabase
import com.hyejis.booksearchapp.data.model.Book
import com.hyejis.booksearchapp.data.model.SearchResponse
import com.hyejis.booksearchapp.data.repository.BookSearchRepositoryImpl.PreferenceKeys.SORT_MODE
import com.hyejis.booksearchapp.util.Constants.PAGING_SIZE
import com.hyejis.booksearchapp.util.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException
import retrofit2.Response

class BookSearchRepositoryImpl(
    private val db: BookSearchDatabase,
    private val dataStore: DataStore<Preferences>
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

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return db.bookSearchDao().getFavoritBooks()
    }

    //DataStore
    private object PreferenceKeys {
        val SORT_MODE = stringPreferencesKey("sort_mode")  //저장할 타입 설정
    }

    override suspend fun saveSortMode(mode: String) {  //코루틴 안에서 저장하는 작업 (suspend)
        dataStore.edit { prefs ->
            prefs[SORT_MODE] = mode
        }
    }

    override suspend fun getSortMode(): Flow<String> {  // 파일 접근을 위해 data 사용, 실패 시 catch 로 사용
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { prefs ->
                prefs[SORT_MODE] ?: Sort.ACCURACY.value  //기본 값
            }
    }

    override fun getFavoritePagingBooks(): Flow<PagingData<Book>> {
        val pagingSourceFactory = { db.bookSearchDao().getFavoritePagingBooks() }

        return Pager(
            config = PagingConfig(
                pageSize = PAGING_SIZE,
                enablePlaceholders = false,  // 데이터를 필요한 만큼만 로딩하기 위해 false
                maxSize = PAGING_SIZE * 3
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

}