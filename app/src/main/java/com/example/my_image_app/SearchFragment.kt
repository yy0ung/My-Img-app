package com.example.my_image_app

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.OnScrollListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.my_image_app.databinding.FragmentSearchBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : MainViewModel
    private lateinit var viewModelFactory : MainViewModelFactory
    private var searchRstList = ArrayList<RstListDto>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private var data = mutableListOf<RstListDto>()

    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val PAGE_SIZE = 10

    private val key = "KakaoAK 771b6707ddc9077bf7ad7c7ae0a92272"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        val view = binding.root

        viewModelFactory = MainViewModelFactory(Repository())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        binding.searchBtn.setColorFilter(android.graphics.Color.parseColor("#F7E34B"))

        CoroutineScope(Dispatchers.Main).launch {
            searchRst("기현")
        }

        binding.searchBtn.setOnClickListener {
            Log.d(TAG, "onCreateView: ${searchRstList.get(searchRstList.size-2)}")
        }


        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private suspend fun searchRst(word : String){
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.searchRst(key, word, "recency",1,4)
            viewModel.repositories1.observe(viewLifecycleOwner){
                data.addAll(it)
                recyclerView = binding.searchList
                recyclerView.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
                adapter = MyAdapter(requireActivity(), data)
                recyclerView.adapter = adapter

                recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        val layoutManager = recyclerView.layoutManager as GridLayoutManager
                        val visibleItemCount = layoutManager.childCount
                        val totalItemCount = layoutManager.itemCount
                        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                        if (!isLoading && !isLastPage) {
                            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                                loadData()
                            }
                        }
                    }
                })
                loadData()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadData() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val newData = RstListDto("aaa", "aaa")
            data.addAll(listOf(newData))
            adapter.notifyDataSetChanged()
            isLastPage = true
            Log.d(TAG, "loadMoreItems: done")

//            CoroutineScope(Dispatchers.Main).launch {
//                viewModel.searchRst(key, "기현", "recency",3, 4)
//                viewModel.repositories1.observe(viewLifecycleOwner){
//                    data.addAll(it)
//                    adapter.notifyDataSetChanged()
//                    isLastPage = true
//                    Log.d(TAG, "loadMoreItems: ${data.size}")
//                }
//            }
        },1000)
        // Do the data loading logic here
        // ...

        // Update the data and notify the adapter

    }

    private fun loadMoreItems() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            isLoading = true

            // Do the data loading logic here for the next page
            // ...

            // Update the data and notify the adapter
            val newData = RstListDto("aaa", "aaa")
            data.addAll(listOf(newData))
            adapter.notifyItemRangeInserted(data.size - 1, 1)

            isLoading = false

            if (true) {
                isLastPage = true
                Log.d(TAG, "loadMoreItems: done")
            } else {
                currentPage++
            }
        },1000)

    }

}