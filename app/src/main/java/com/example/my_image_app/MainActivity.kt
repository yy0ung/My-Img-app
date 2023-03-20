package com.example.my_image_app

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.ui.graphics.Color
import com.example.my_image_app.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    @SuppressLint("MissingInflatedId", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment = supportFragmentManager.beginTransaction()
        fragment.add(R.id.mainFragmentContainer, SearchFragment()).commit()
        binding.mainFragmentSaveIcon.setColorFilter(android.graphics.Color.parseColor("#E3E1E1"))

        binding.mainFragmentSearchContainer.setOnClickListener {
            binding.mainFragmentSearchIcon.setColorFilter(android.graphics.Color.parseColor("#FF000000"))
            binding.mainFragmentSaveIcon.setColorFilter(android.graphics.Color.parseColor("#E3E1E1"))
            supportFragmentManager.beginTransaction().replace(R.id.mainFragmentContainer, SearchFragment()).commit()
        }

        binding.mainFragmentSaveContainer.setOnClickListener {
            binding.mainFragmentSearchIcon.setColorFilter(android.graphics.Color.parseColor("#E3E1E1"))
            binding.mainFragmentSaveIcon.setColorFilter(android.graphics.Color.parseColor("#FF000000"))
            supportFragmentManager.beginTransaction().replace(R.id.mainFragmentContainer, SaveFragment()).commit()
        }

    }

}