package com.csaim.apicallinglearnings

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject

class SourceScreenManager {

    val okHttpClient: OkHttpClient

    init{
        val builder= OkHttpClient.Builder()
        val loggingInterceptor= HttpLoggingInterceptor()
        loggingInterceptor.level= HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)
        okHttpClient=builder.build()

    }
    suspend fun retrieveSources(apikey:String,keyword:String,category: String): List<SourceScreenData>
    {
        val request= Request.Builder()
            .url("https://newsapi.org/v2/sources?q=$keyword&category=$category&language=en")
            .header("authorization","Bearer $apikey")
            .get()
            .build()

        val response: Response =okHttpClient.newCall(request).execute()
        val responseBody=response.body?.string()
        if (response.isSuccessful && !responseBody.isNullOrEmpty()){
            val sourceScreenDataList=mutableListOf<SourceScreenData>()

            val json=JSONObject(responseBody)
            val source=json.getJSONArray("sources")

            for (i in 0 until source.length()){
                val currentSource=source.getJSONObject(i)

                val name=currentSource.getString("name")
                val description=currentSource.getString("description")
                val url=currentSource.getString("url")

                Log.d("Sources","the source is $source")
                val sourceScreenData=SourceScreenData(
                    sourceName = name,
                    sourceDescription = description,
                    sourceUrl = url
                )
                sourceScreenDataList.add(sourceScreenData)
            }
            return sourceScreenDataList
        }else{
            return listOf()
        }
    }
}