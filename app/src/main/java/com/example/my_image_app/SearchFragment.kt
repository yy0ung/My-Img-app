package com.example.my_image_app

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.my_image_app.databinding.FragmentSearchBinding
import com.example.my_image_app.retrofit.dto.RstListDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : MainViewModel
    private lateinit var viewModelFactory : MainViewModelFactory
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchItemAdapter

    private var data = mutableListOf<RstListDto>()
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var lastSize = 0


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

        recyclerView = binding.searchList
        
        recyclerView.clearAnimation()
        recyclerView.run { GridItemAlign(2, 5) }

        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.searchBtn.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                recyclerView.setItemViewCacheSize(20)
                searchRst(binding.searchInput.text.toString())
                imm.hideSoftInputFromWindow(binding.searchBtn.windowToken,0)
            }
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading && !isLastPage) {
                    if (!recyclerView.canScrollVertically(1)) {
                        isLoading = true
                        loadData()
                    }
                }
            }
        })

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
            viewModel.searchRst(key, word, "recency", currentPage,10)
            viewModel.repositories1.observe(viewLifecycleOwner){
                viewModel.unallocList.clear()
                data.clear()
                data.addAll(it)
                recyclerView.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
                adapter = SearchItemAdapter(requireActivity(), data)
                recyclerView.adapter = adapter
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                lastSize = totalItemCount

            }
        }
        
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadData() {
        currentPage++
        recyclerView.setItemViewCacheSize(20*currentPage+1)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            val totalItemCount = layoutManager.itemCount

            val re = Repository()

            CoroutineScope(Dispatchers.Main).launch {

                viewModel.loadNextPage(key, binding.searchInput.text.toString(), "recency", currentPage, 10, data, adapter, totalItemCount, totalItemCount)

            }
            isLoading = false
            // 마지막 페이지 확인
            if(re.isLastPage){
                isLastPage = true
            }
        },1000)
        }

    }



