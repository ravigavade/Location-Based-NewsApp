package com.csaim.apicallinglearnings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SourceScreenAdapter(val sourceScreenData:List<SourceScreenData>):RecyclerView.Adapter<SourceScreenAdapter.ViewHolder>() {
    class ViewHolder(rootLayout: View): RecyclerView.ViewHolder(rootLayout){
        val title:TextView=rootLayout.findViewById(R.id.title)
        val description:TextView=rootLayout.findViewById(R.id.description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val rootLayout: View =layoutInflater.inflate(R.layout.source_screen_item_layout, parent, false)
        val viewHolder= ViewHolder(rootLayout)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return sourceScreenData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentSource=sourceScreenData[position]
        holder.title.text=currentSource.sourceName
        holder.description.text=currentSource.sourceDescription
    }
}