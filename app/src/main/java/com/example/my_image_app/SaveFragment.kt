package com.example.my_image_app

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.my_image_app.databinding.FragmentSaveBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SaveFragment : Fragment() {
    private var _binding : FragmentSaveBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : MainViewModel
    private lateinit var viewModelFactory : MainViewModelFactory
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSaveBinding.inflate(layoutInflater)
        val view = binding.root


        viewModelFactory = MainViewModelFactory(Repository())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        binding.saveList.run { GridItemAlign(2, 5) }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getPref("key", "null")
            viewModel.repositories3.observe(viewLifecycleOwner){
                Log.d(TAG, "onCreateView: $it")
                it.reverse()
                val adapter = SaveItemAdapter(it)
                binding.saveList.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                binding.saveList.adapter = adapter
            }
        }

        return view
    }
}