package com.example.my_image_app

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.my_image_app.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel : MainViewModel
    private lateinit var viewModelFactory : MainViewModelFactory
    private lateinit var binding : ActivityMainBinding
    private val key = "KakaoAK 771b6707ddc9077bf7ad7c7ae0a92272"
    @SuppressLint("MissingInflatedId", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        viewModelFactory = MainViewModelFactory(Repository())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        binding.save.setOnClickListener {
            val shared = getSharedPreferences("s1", MODE_PRIVATE)
            val editor  : SharedPreferences.Editor = shared.edit()
            editor.putString("a", "https://search4.kakaocdn.net/argon/130x130_85_c/BcYqEVBCPq2")
            editor.apply()
        }

        binding.load.setOnClickListener {
            val shared = getSharedPreferences("s1", MODE_PRIVATE)
            val url = shared.getString("a", "null")
            if(url=="null"){
                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show()
            }else{
                Glide.with(this@MainActivity).load(url).into(binding.img)
            }
        }
        

        binding.button.setOnClickListener {
            Toast.makeText(this, "aaa", Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.Main).launch { search(binding.input.text.toString()) }
            Log.d(TAG, "onCreate: dfdfdfdfdfdf")
        }

    }
    private suspend fun search(word : String){
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.searchImg(key, word, "recency")
            viewModel.repositories1.observe(this@MainActivity){
                Log.d(TAG, "onCreate: ${it}")

            }
        }
    }
}