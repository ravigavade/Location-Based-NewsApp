package com.csaim.newsproject1

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.csaim.newsproject1.databinding.ActivityTopHeadlinesBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TopHeadlines : AppCompatActivity() {
    private lateinit var binding: ActivityTopHeadlinesBinding
    private lateinit var topHeadlinesManager: TopHeadlinesManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTopHeadlinesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        topHeadlinesManager = TopHeadlinesManager()


        val apiKey=getString(com.csaim.newsproject1.R.string.apiKey)
        val category="sports"


        // Setup Spinner with categories
        val categories = listOf("Sports", "Technology", "Business", "Entertainment", "Health", "Science", "General")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories) // Use default layout if custom one doesn't exist
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategories.adapter = adapter



        binding.spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position].lowercase()
                fetchNews(apiKey, selectedCategory)
            }


            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected (if needed)
            }
        }


        //implementing the recyc view
        val newsManage=TopHeadlinesManager()
        var topHeadlinesData= listOf<TopHeadlinesData>()
        lifecycleScope.launch {
            withContext(Dispatchers.IO){
                topHeadlinesData=newsManage.retrieveNews(apiKey,category)
            }
            val topHeadlineAdapter=TopHeadlineAdapter(topHeadlinesData)
            binding.recyclerView.layoutManager= LinearLayoutManager(this@TopHeadlines)
            binding.recyclerView.adapter=topHeadlineAdapter
        }
    }


    private fun fetchNews(apiKey: String, category: String) {
        lifecycleScope.launch {
            val news = withContext(Dispatchers.IO) {
                // Update the API URL dynamically based on the category
                topHeadlinesManager.retrieveNews(apiKey, category)
            }
            // Set the adapter with the newly fetched news


            val topHeadlineAdapter = TopHeadlineAdapter(news) // Assuming you have a NewsAdapter
            binding.recyclerView.adapter = topHeadlineAdapter
        }
    }
}
