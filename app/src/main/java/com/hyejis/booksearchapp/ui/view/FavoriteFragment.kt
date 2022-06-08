package com.hyejis.booksearchapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hyejis.booksearchapp.databinding.FragmentFavoriteBinding
import com.hyejis.booksearchapp.ui.adapter.BookSearchPagingAdapter
import com.hyejis.booksearchapp.ui.viewmodel.BookSearchViewModel
import com.hyejis.booksearchapp.util.collectLatestStateFlow

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookSearchViewModel: BookSearchViewModel

    //  private lateinit var bookSearchAdapter: BookSearchAdapter

    private lateinit var bookSearchAdapter: BookSearchPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookSearchViewModel = (activity as MainActivity).bookSearchViewModel

        setupRecyclerView()
        setupTouchHelper(view)
//        bookSearchViewModel.favoriteBooks.observe(viewLifecycleOwner) {
//            bookSearchAdapter.submitList(it)
//        }

        // livedata > flow로 변환
//        lifecycleScope.launch {
//            bookSearchViewModel.favoriteBooks.collectLatest {
//                bookSearchAdapter.submitList(it)
//            }
//        }
        //Stateflow 구독하기
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                bookSearchViewModel.favoriteBooks.collectLatest {
//                    bookSearchAdapter.submitList(it)
//                }
//            }
//        }

        //확장함수 적용 - extensions
//        collectLatestStateFlow(bookSearchViewModel.favoriteBooks) {
//            bookSearchAdapter.submitList(it)
//        }

        //기존에 페이징 값을 취소하고 새로 구독하도록 latest로 사용
        collectLatestStateFlow(bookSearchViewModel.favoritePagingBooks) {
            bookSearchAdapter.submitData(it)
        }
    }


    private fun setupRecyclerView() {
        //       bookSearchAdapter = BookSearchAdapter()
        bookSearchAdapter = BookSearchPagingAdapter()
        binding.rvFavoriteBooks.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
            adapter = bookSearchAdapter
        }

        bookSearchAdapter.setOnClickListener {
            val action: NavDirections =
                SearchFragmentDirections.actionSearchFragmentToBookFragment(it)
            findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setupTouchHelper(view: View) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT   //드래그 설정안함, 스와이프 방향 왼쪽으로
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
//                val book = bookSearchAdapter.currentList[position]
//                bookSearchViewModel.deleteBook(book)  //삭제 동작
//                Snackbar.make(view, "Book has deleted", Snackbar.LENGTH_SHORT).apply {
//                    setAction("Undo") {
//                        bookSearchViewModel.saveBook(book)  //Undo 누르면 다시 아이템을 저장할 수 있도록
//                    }
//                }.show()

                val pagedBook = bookSearchAdapter.peek(position)  //null 처리!
                pagedBook?.let {
                    bookSearchViewModel.deleteBook(pagedBook)
                    Snackbar.make(view, "Book has deleted", Snackbar.LENGTH_SHORT).apply {
                        setAction("Undo") {
                            bookSearchViewModel.saveBook(pagedBook)  //Undo 누르면 다시 아이템을 저장할 수 있도록
                        }
                    }.show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvFavoriteBooks)  //스와이프 인식 가능하게 해줌
        }

    }

}