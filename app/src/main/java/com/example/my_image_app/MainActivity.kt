package com.example.my_image_app

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        binding.mainFragmentSearchContainer.setBackgroundResource(R.drawable.main_menubar_true)

        binding.mainFragmentSearchContainer.setOnClickListener {
            binding.mainFragmentSearchContainer.setBackgroundResource(R.drawable.main_menubar_true)
            binding.mainFragmentSaveContainer.setBackgroundResource(R.drawable.main_menubar_false)
            supportFragmentManager.beginTransaction().replace(R.id.mainFragmentContainer, SearchFragment()).commit()
        }

        binding.mainFragmentSaveContainer.setOnClickListener {
            binding.mainFragmentSaveContainer.setBackgroundResource(R.drawable.main_menubar_true)
            binding.mainFragmentSearchContainer.setBackgroundResource(R.drawable.main_menubar_false)
            supportFragmentManager.beginTransaction().replace(R.id.mainFragmentContainer, SaveFragment()).commit()
        }

    }

}