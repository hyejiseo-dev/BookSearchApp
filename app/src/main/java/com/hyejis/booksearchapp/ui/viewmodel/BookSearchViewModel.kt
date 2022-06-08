package com.hyejis.booksearchapp.ui.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hyejis.booksearchapp.data.model.Book
import com.hyejis.booksearchapp.data.model.SearchResponse
import com.hyejis.booksearchapp.data.repository.BookSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class BookSearchViewModel(
    private val bookSearchRepository: BookSearchRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _searchResult = MutableLiveData<SearchResponse>()
    val searchResult: LiveData<SearchResponse> get() = _searchResult

    fun searchBooks(query: String) = viewModelScope.launch(Dispatchers.IO) {
        val response: Response<SearchResponse> =
            bookSearchRepository.searchBooks(query, getSortMode(), 1, 15)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                _searchResult.postValue(body)
            }
        }
    }

    //Room
    //viewModelScope -> suspend 함수 사용하는 곳
    fun saveBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        bookSearchRepository.insertBooks(book)
    }

    fun deleteBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        bookSearchRepository.deleteBooks(book)
    }

    //val favoriteBooks: Flow<List<Book>> = bookSearchRepository.getFavoriteBooks()
    //StateFlow Type 변경
    val favoriteBooks: StateFlow<List<Book>> = bookSearchRepository.getFavoriteBooks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())

    //SavedState - 재시작 시 데이터 유지
    var query = String()
        set(value) {
            field = value
            savedStateHandle.set(SAVE_STATE_KEY, value)
        }

    init {
        query = savedStateHandle.get<String>(SAVE_STATE_KEY) ?: ""
    }

    companion object {
        private const val SAVE_STATE_KEY = "query"
    }

    //DataStore
    fun saveSortMode(value: String) = viewModelScope.launch(Dispatchers.IO) {
        bookSearchRepository.saveSortMode(value)
    }

    suspend fun getSortMode() = withContext(Dispatchers.IO) {
        bookSearchRepository.getSortMode().first()
    }

    //Paging
    val favoritePagingBooks: StateFlow<PagingData<Book>> =
        bookSearchRepository.getFavoritePagingBooks()
            .cachedIn(viewModelScope)  //코루틴이 캐시, 공유가능
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                PagingData.empty()
            )  //UI에서 감시해야하기 때문에 stateIn
}