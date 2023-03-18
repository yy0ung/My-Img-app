package com.example.my_image_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.my_image_app.databinding.FragmentSaveBinding


class SaveFragment : Fragment() {
    private var _binding : FragmentSaveBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSaveBinding.inflate(layoutInflater)
        val view = binding.root
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