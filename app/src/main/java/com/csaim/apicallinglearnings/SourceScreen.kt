package com.csaim.apicallinglearnings

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.csaim.apicallinglearnings.databinding.ActivitySourceScreenBinding
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

class SourceScreen : AppCompatActivity() {
    private lateinit var binding:ActivitySourceScreenBinding
    private lateinit var sourceScreenManager:SourceScreenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySourceScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility=View.VISIBLE

        sourceScreenManager=SourceScreenManager()

        val apiKey=getString(com.csaim.apicallinglearnings.R.string.apiKey)
        val category="sports"
        val keyword=binding.tvSearchTerm.getText().toString().trim()
        val searchedSource=intent.getStringExtra("passSearch")

//        binding.tvSearchTerm.text=searchedSource
        supportActionBar?.title = "Search for $searchedSource"

        val categories = listOf("Sports","Technology","Business","Entertainment","Health","Science","General")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter

        binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position].lowercase()
                fetchNews(apiKey, keyword, selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }

        //implementing the recyc view
        val sourceManage=SourceScreenManager()
        var sourceData= listOf<SourceScreenData>()
        lifecycleScope.launch {
            // Fetch source data in the background
            withContext(Dispatchers.IO) {
                sourceData = sourceManage.retrieveSources(apiKey,keyword,category)
            }

            val filteredSourceData = sourceData.filter {
                it.sourceName!="[Removed]"&& it.sourceDescription!="[Removed]"&& !it.sourceUrl.isNullOrEmpty()
            }

            // Create a new adapter with the filtered data
            val sourceAdapter = SourceScreenAdapter(filteredSourceData)
            binding.recyclerView.layoutManager = LinearLayoutManager(this@SourceScreen)
            binding.recyclerView.adapter = sourceAdapter
        }

        binding.btnSkip.setOnClickListener {
            startActivity(Intent(this,AllSourcesScreen::class.java).putExtra("passSearch",searchedSource))
        }
    }

    private fun fetchNews(apiKey: String, keyword:String, category: String) {
        lifecycleScope.launch {
            val source = withContext(Dispatchers.IO) {
                sourceScreenManager.retrieveSources(apiKey,keyword, category)
            }
            val sourceScreenAdapter = SourceScreenAdapter(source)
            binding.recyclerView.adapter = sourceScreenAdapter
            binding.progressBar.visibility=View.GONE

        }
    }

}