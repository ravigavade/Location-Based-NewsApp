package com.csaim.apicallinglearnings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.csaim.apicallinglearnings.databinding.ActivityAllSourcesScreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllSourcesScreen : AppCompatActivity() {

    private lateinit var binding: ActivityAllSourcesScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllSourcesScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchAndDisplayNews()
    }

    // Setup RecyclerView with the TopHeadlineAdapter
    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this) // Set layout manager
    }

    // Fetch data using TopHeadlinesManager and update RecyclerView
    private fun fetchAndDisplayNews() {
        val apiKey = getString(R.string.apiKey) // Your API key

        val allSourceNewsManager = AllSourceScreenManager()
        var allSourceData = listOf<AllSourcesScreenData>()

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                allSourceData = allSourceNewsManager.retrieveSourceNews(apiKey)
            }

            // Filter out "removed" or null news items
            val filteredNewsData = allSourceData.filter {
                it.newsTitle != "[Removed]" && it.newsDescription != "[Removed]" && !it.newsIcon.isNullOrEmpty()
            }

            // Create a new adapter with the filtered data
            val SourceNewsAdapter = AllSourceScreenAdapter(filteredNewsData)

            // Update RecyclerView with the new adapter
            binding.recyclerView.adapter = SourceNewsAdapter
        }
    }
}
