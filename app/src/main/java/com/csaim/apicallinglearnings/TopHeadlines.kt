package com.csaim.apicallinglearnings

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

    private var currentPage=1
    private val itemsPerPage=6
    private var totalPages = 4
    private var allTopHeadlinesData: List<TopHeadlinesData> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopHeadlinesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Top Headlines"

        topHeadlinesManager = TopHeadlinesManager()
        binding.progressBar.visibility = View.VISIBLE
        val apiKey = getString(com.csaim.apicallinglearnings.R.string.apiKey)

        val categories = listOf("Sports","Technology","Business","Entertainment","Health","Science","General")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategories.adapter = adapter

        binding.spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position].lowercase()
                fetchNews(apiKey, selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.buttonPrevious.setOnClickListener {
            if (currentPage>1) {
                currentPage--
                displayNewsPage()
            }
        }

        binding.buttonNext.setOnClickListener {
            if (currentPage<totalPages) {
                currentPage++
                displayNewsPage()
            }
        }
    }



    private fun fetchNews(apiKey: String, category: String) {
        lifecycleScope.launch {
            val news = withContext(Dispatchers.IO) {
                topHeadlinesManager.retrieveNews(apiKey, category)
            }

            val filteredNewsData = news.filter {
                it.newsTitle!="[Removed]"&& it.newsDescription!="[Removed]"&& !it.newsIcon.isNullOrEmpty()
            }

            allTopHeadlinesData = filteredNewsData
            totalPages=(allTopHeadlinesData.size+itemsPerPage-1)/itemsPerPage
            currentPage=1


            displayNewsPage()
        }

    }
    private fun displayNewsPage() {
        val startIndex=(currentPage-1)*itemsPerPage
        val endIndex=minOf(startIndex+itemsPerPage,allTopHeadlinesData.size)

        val currentData=allTopHeadlinesData.subList(startIndex, endIndex)
        val topHeadlineAdapter=TopHeadlineAdapter(currentData)
        binding.recyclerView.layoutManager=LinearLayoutManager(this@TopHeadlines)
        binding.recyclerView.adapter = topHeadlineAdapter

        binding.buttonPrevious.isEnabled=currentPage>1
        binding.buttonNext.isEnabled=currentPage<totalPages


//
        binding.progressBar.visibility=View.GONE

        binding.textViewPageIndicator.text = "Page $currentPage/$totalPages"

        if (totalPages>1){
            binding.textViewPageIndicator.visibility = View.VISIBLE
        } else {
            binding.textViewPageIndicator.visibility = View.GONE
        }
    }
}
