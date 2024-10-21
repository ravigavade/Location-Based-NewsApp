package com.csaim.apicallinglearnings

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MapsAdapter(val mapsData:List<MapsData>):RecyclerView.Adapter<MapsAdapter.ViewHolder>() {
    class ViewHolder(rootLayout: View): RecyclerView.ViewHolder(rootLayout){
        val title:TextView=rootLayout.findViewById(R.id.title)
        val description:TextView=rootLayout.findViewById(R.id.description)
        val icon: ImageView =rootLayout.findViewById(R.id.icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val rootLayout: View =layoutInflater.inflate(R.layout.maps_item_layout, parent, false)
        val viewHolder= ViewHolder(rootLayout)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return mapsData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNews=mapsData[position]
        holder.title.text=currentNews.newsTitle
        holder.description.text=currentNews.newsDescription

        if(currentNews.newsIcon.isNotEmpty()){
            Picasso.get().setIndicatorsEnabled(true)
            Picasso.get()
                .load(currentNews.newsIcon)
                .into(holder.icon)
        }

        val url=currentNews.newsUrl
        val newsContext=holder.itemView.context


        holder.itemView.setOnClickListener{
            val intent=Intent(Intent.ACTION_VIEW)
            intent.data=Uri.parse(url)
            newsContext.startActivity(intent)

        }
    }


}