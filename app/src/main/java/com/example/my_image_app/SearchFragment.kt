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
    private var isLoading = false
    private val handler = Handler()
    private var searchRstList = ArrayList<RstListDto>()
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
        scroll()

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
            viewModel.searchRst(key, word, "recency")
            viewModel.repositories1.observe(viewLifecycleOwner){
                searchRstList.addAll(it)
                val adapter = SearchItemAdapter(it, requireContext())
                binding.searchList.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                binding.searchList.adapter = adapter

            }
        }
    }
    private fun scroll(){
        binding.searchList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val manager = recyclerView.layoutManager as GridLayoutManager
                if(!isLoading){
                    if(recyclerView.canScrollVertically(1)){
                        more()
                        isLoading = true
                    }
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun more(){
        searchRstList.add(RstListDto("null", "null"))
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            binding.searchList.adapter?.notifyItemInserted(searchRstList.size-1)
            searchRstList.removeAt(searchRstList.size-1)
            val cnt = searchRstList.size
            binding.searchList.adapter?.notifyItemRemoved(cnt)
            var currentCnt = cnt
            val next = currentCnt + 10

            while(currentCnt-1<next){
                searchRstList.add(RstListDto("a", "b"))
                currentCnt+=1
            }
            binding.searchList.adapter?.notifyDataSetChanged()
            isLoading = false
        }, 2000)
    }

}