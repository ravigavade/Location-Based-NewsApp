package com.csaim.apicallinglearnings

import android.R
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.csaim.apicallinglearnings.databinding.ActivityTopHeadlinesBinding
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

        val apiKey=getString(com.csaim.apicallinglearnings.R.string.apiKey)
        val category="sports"

        // Setup Spinner with categories
        val categories = listOf("Sports", "Technology", "Business", "Entertainment","Health", "Science","General")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, categories)
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
        val newsManager = TopHeadlinesManager()
        var topHeadlinesData = listOf<TopHeadlinesData>()
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                // Retrieve the news articles
                topHeadlinesData = newsManager.retrieveNews(apiKey, category)
            }

            // Once filtering is done, set the adapter and layout manager
            val topHeadlineAdapter = TopHeadlineAdapter(topHeadlinesData)
            binding.recyclerView.layoutManager = LinearLayoutManager(this@TopHeadlines)
            binding.recyclerView.adapter = topHeadlineAdapter
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