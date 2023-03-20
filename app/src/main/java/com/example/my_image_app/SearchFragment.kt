package com.example.my_image_app

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
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
    private lateinit var searchRstList : ArrayList<Any>
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

        binding.searchBtn.setOnClickListener {
            Toast.makeText(context, "aaa", Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.Main).launch {
                searchRst(binding.searchInput.text.toString())
            }
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
                val adapter = SearchItemAdapter(it)
                binding.searchList.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                binding.searchList.adapter = adapter
            }
        }
    }
}