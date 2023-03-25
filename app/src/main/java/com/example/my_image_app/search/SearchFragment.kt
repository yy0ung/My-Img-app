package com.example.my_image_app.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.my_image_app.BuildConfig
import com.example.my_image_app.MainViewModel
import com.example.my_image_app.MainViewModelFactory
import com.example.my_image_app.Repository
import com.example.my_image_app.databinding.FragmentSearchBinding
import com.example.my_image_app.retrofit.dto.RstListDto
import com.example.my_image_app.utils.GridItemAlign
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : MainViewModel
    private lateinit var viewModelFactory : MainViewModelFactory
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchItemAdapter

    private val repository = Repository()
    private var data = mutableListOf<RstListDto>()
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val queryItemSize = 30
    private val apiKey = "KakaoAK ${BuildConfig.API_KEY}"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        val view = binding.root
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        viewModelFactory = MainViewModelFactory(Repository())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        binding.searchBtn.setColorFilter(android.graphics.Color.parseColor("#F7E34B"))


        binding.searchBtn.setOnClickListener {
            if(binding.searchInput.text.toString().isEmpty()){
                Toast.makeText(context, "검색어를 입력하세요", Toast.LENGTH_SHORT).show()
            }else{
                CoroutineScope(Dispatchers.Main).launch {
                    recyclerView = binding.searchList
                    recyclerView.setItemViewCacheSize(queryItemSize*2)
                    recyclerView.clearAnimation()
                    recyclerView.run { GridItemAlign(2, 5) }
                    searchRst(binding.searchInput.text.toString())
                    imm.hideSoftInputFromWindow(binding.searchBtn.windowToken,0)
                }
            }
        }
        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private suspend fun searchRst(word : String){
        // initialize current page
        currentPage = 1
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.searchRst(apiKey, word, "recency", currentPage, queryItemSize)
            viewModel.repositories1.observe(viewLifecycleOwner){
                viewModel.remainList.clear()
                data.clear()
                data.addAll(it)
                recyclerView.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
                adapter = SearchItemAdapter(requireActivity(), data)
                recyclerView.adapter = adapter
            }
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading && !isLastPage) {
                    if (!recyclerView.canScrollVertically(1)) {
                        Toast.makeText(context, "LOADING . . .", Toast.LENGTH_SHORT).show()
                        isLoading = true
                        loadData()
                    }
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadData() {
        currentPage++
        recyclerView.setItemViewCacheSize((queryItemSize*2)*(currentPage+1))
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            val totalItemCount = layoutManager.itemCount

            CoroutineScope(Dispatchers.IO).launch {
                viewModel.loadNextPage(
                    apiKey, binding.searchInput.text.toString(), "recency", currentPage, queryItemSize, data, adapter, totalItemCount, totalItemCount)
            }

            isLoading = false
            // 마지막 페이지 확인
            if(repository.isLastPage){
                isLastPage = true
            }
        },1000)
        }

    }



