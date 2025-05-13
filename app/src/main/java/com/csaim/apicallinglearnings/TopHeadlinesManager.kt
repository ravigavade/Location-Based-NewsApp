package com.csaim.apicallinglearnings

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject


class TopHeadlinesManager {

    val okHttpClient: OkHttpClient
    init{
        val builder= OkHttpClient.Builder()
        val loggingInterceptor= HttpLoggingInterceptor()
        loggingInterceptor.level= HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)
        okHttpClient=builder.build()
    }
    suspend fun retrieveNews(apikey:String,category: String): List<TopHeadlinesData>
    {
        val request= Request.Builder()
            .url("https://newsapi.org/v2/top-headlines?country=us&category=$category")
            .header("authorization","Bearer $apikey")
            .get()
            .build()

        val response: Response =okHttpClient.newCall(request).execute()
        val responseBody=response.body?.string()
        if (response.isSuccessful && !responseBody.isNullOrEmpty()){
            val topHeadlinesDataList=mutableListOf<TopHeadlinesData>()

            val json=JSONObject(responseBody)
            val article=json.getJSONArray("articles")

            for (i in 0 until article.length()){
                val currentNews=article.getJSONObject(i)

                val title=currentNews.getString("title")
                val description=currentNews.getString("description")
                val url=currentNews.getString("url")
                val icon=currentNews.getString("urlToImage")

                //setting the data to the data class
                Log.d("apiCaling","fetch title is $title")
                val topHeadlinesData=TopHeadlinesData(
                    newsTitle = title,
                    newsDescription = description,
                    newsUrl = url,
                    newsIcon=icon

                )
                topHeadlinesDataList.add(topHeadlinesData)
            }
            return topHeadlinesDataList
        }else{
            return listOf()
        }

    }

}