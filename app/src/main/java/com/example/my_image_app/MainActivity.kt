package com.example.my_image_app

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.my_image_app.databinding.ActivityMainBinding
import com.example.my_image_app.save.SaveFragment
import com.example.my_image_app.search.SearchFragment


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

        // SearchFragment 의 상태를 유지하기 위해 탭을 바꿀 때마다 새로 생성하지 않고 hide 를 사용.

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