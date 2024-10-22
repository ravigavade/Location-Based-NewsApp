package com.csaim.apicallinglearnings

import android.os.Bundle
import android.view.View
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

        binding.progressBar.visibility=View.VISIBLE


        val results=intent.getStringExtra("passSearch")
        supportActionBar?.title = "All Results for $results"

        setupRecyclerView()
        fetchAndDisplayNews()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchAndDisplayNews() {
        val apiKey = getString(R.string.apiKey)

        val allSourceNewsManager = AllSourceScreenManager()
        var allSourceData = listOf<AllSourcesScreenData>()
        val searchedSource=intent.getStringExtra("passSearch")
        val q=searchedSource.toString()

        lifecycleScope.launch {

            withContext(Dispatchers.IO) {
                allSourceData = allSourceNewsManager.retrieveSourceNews(apiKey,q)
            }
            val filteredNewsData = allSourceData.filter {
                it.newsTitle!="[Removed]"&& it.newsDescription!="[Removed]"&& !it.newsIcon.isNullOrEmpty()
            }

            val SourceNewsAdapter=AllSourceScreenAdapter(filteredNewsData)

            binding.recyclerView.adapter = SourceNewsAdapter
            binding.progressBar.visibility= View.GONE

        }
    }
}
