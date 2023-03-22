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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

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
        recyclerView = binding.searchList
        CoroutineScope(Dispatchers.Main).launch {
            searchRst("연세")
        }

        binding.searchBtn.setOnClickListener {
            Log.d(TAG, "onCreateView: ${data.get(data.size-1).thumbnail}")
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
                recyclerView.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
                adapter = MyAdapter(requireActivity(), data)
                recyclerView.adapter = adapter

            }
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {

                    if (!recyclerView.canScrollVertically(1)) {
                        loadData()
                    }
                }
                //Log.d(TAG, "onScrolled: $visibleItemCount $totalItemCount $firstVisibleItemPosition")
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadData() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val re = Repository()
            isLoading = true
            re.test(key, "연세", "recency", 2,4)
                .enqueue(
                    object : Callback<RetrofitSearchImg>{
                        override fun onFailure(call: Call<RetrofitSearchImg>, t: Throwable) {
                            Log.d(TAG, "onFailure: 실패")
                        }

                        override fun onResponse(
                            call: Call<RetrofitSearchImg>,
                            response: Response<RetrofitSearchImg>
                        ) {
                            val v = response.body()
                            val arr = ArrayList<RstListDto>()
                            for(i in 0 until v?.documents?.size!!){
                                arr.add(RstListDto(v.documents[i].thumbnail, v.documents[i].datetime))
                            }
                            data.addAll(arr)
                            adapter.notifyDataSetChanged()
                            Log.d(TAG, "onResponse: ㅇㅇㅇㅇㅇ")
                            Log.d(TAG, "onResponse: $arr")
                        }
                    }
                )
            isLastPage = true
            isLoading = false
            Log.d(TAG, "loadMoreItems: done")
//            isLoading = true
//            val newData = RstListDto("https://search4.kakaocdn.net/argon/138x78_80_pr/AnbEJl5mTtg", "aaa")
//            data.add(newData)
//            adapter.notifyDataSetChanged()
//            isLastPage = true
//            isLoading = false
//            Log.d(TAG, "loadMoreItems: done")
        },2000)

        }


    }

//    private fun loadMoreItems() {
//        val handler = Handler(Looper.getMainLooper())
//        handler.postDelayed({
//            isLoading = true
//
//            // Do the data loading logic here for the next page
//            // ...
//
//            // Update the data and notify the adapter
//            val newData = RstListDto("aaa", "aaa")
//            data.addAll(listOf(newData))
//            adapter.notifyItemRangeInserted(data.size - 1, 1)
//
//            isLoading = false
//
//            if (true) {
//                isLastPage = true
//                Log.d(TAG, "loadMoreItems: done")
//            } else {
//                currentPage++
//            }
//        },1000)
//
//    }

