package com.example.my_image_app

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.ui.graphics.Color
import com.example.my_image_app.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private var searchFragment : SearchFragment? = null
    private var saveFragment : SaveFragment? = null

    @SuppressLint("MissingInflatedId", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchFragment = SearchFragment()
        supportFragmentManager.beginTransaction().replace(R.id.mainFragmentContainer, searchFragment!!).commit()
        binding.mainFragmentSaveIcon.setColorFilter(android.graphics.Color.parseColor("#E3E1E1"))

        binding.mainFragmentSearchContainer.setOnClickListener {
            binding.mainFragmentSearchIcon.setColorFilter(android.graphics.Color.parseColor("#FF000000"))
            binding.mainFragmentSaveIcon.setColorFilter(android.graphics.Color.parseColor("#E3E1E1"))
            if(searchFragment!=null) supportFragmentManager.beginTransaction().show(searchFragment!!).commit()
            if(saveFragment!=null) supportFragmentManager.beginTransaction().hide(saveFragment!!).commit()
        }

        binding.mainFragmentSaveContainer.setOnClickListener {
            binding.mainFragmentSearchIcon.setColorFilter(android.graphics.Color.parseColor("#E3E1E1"))
            binding.mainFragmentSaveIcon.setColorFilter(android.graphics.Color.parseColor("#FF000000"))
            if(searchFragment!=null) supportFragmentManager.beginTransaction().hide(searchFragment!!).commit()
            saveFragment = SaveFragment()
            supportFragmentManager.beginTransaction().add(R.id.mainFragmentContainer, saveFragment!!).commit()

        }

    }

}