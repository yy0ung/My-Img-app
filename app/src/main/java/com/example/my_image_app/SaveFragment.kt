package com.example.my_image_app

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getPref("key", "no")
            viewModel.repositories3.observe(viewLifecycleOwner){
                Log.d(TAG, "onCreateView: $it")
            }
        }

//        val shared = requireActivity().getSharedPreferences("s1", activity.MODE_PRIVATE)
//            val url = shared.getString("a", "null")
//            if(url=="null"){
//                Toast.makeText(context, "null", Toast.LENGTH_SHORT).show()
//            }else{
//                Glide.with(requireActivity()).load(url).into(binding.img)
//            }
        return view
    }
}