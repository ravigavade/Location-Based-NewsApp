package com.csaim.newsproject1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csaim.newsproject1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.tvNews.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }
            //main thing as of 1st submission
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnSearch.isEnabled=!s.isNullOrEmpty()
            }
            override fun afterTextChanged(s: Editable?) {
                //
            }
        })
        binding.btnSearch.setOnClickListener {
            val passText=binding.tvNews.text.toString().trim()
//            startActivity(Intent(this, SourceScreen::class.java).putExtra("passSearch",passText))
        }


        binding.btnTopNews.setOnClickListener {
            startActivity(Intent(this,TopHeadlines::class.java))
        }


        binding.btnLocalNews.setOnClickListener {
//            startActivity(Intent(this,MapsActivity::class.java))
        }
    }
}
