package com.hyejis.booksearchapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.hyejis.booksearchapp.data.model.Book
import com.hyejis.booksearchapp.databinding.ItemBookPreviewBinding

class BookSearchPagingAdapter : PagingDataAdapter<Book, BookSearchViewHolder>(BookDiffCallBack) {
    override fun onBindViewHolder(holder: BookSearchViewHolder, position: Int) {
        val pagedBook = getItem(position)
        pagedBook?.let { book ->
            holder.bind(book)
            holder.itemView.setOnClickListener { onItemClickListener?.let { it(book) } }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookSearchViewHolder {
        return BookSearchViewHolder(
            ItemBookPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    private var onItemClickListener: ((Book) -> Unit)? = null

    fun setOnClickListener(listener: (Book) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val BookDiffCallBack = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.isbn == newItem.isbn
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }
        }
    }
}