package com.example.my_image_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide

class OtherAdapter(val items : MutableList<RstListDto>) : RecyclerView.Adapter<ViewHolder>() {
    companion object{
        const val item = 1
        const val loading = 0
    }

    inner class ItemHolder(itemView : View) : ViewHolder(itemView){
        fun bind(item : RstListDto){
            val container = itemView.findViewById<ConstraintLayout>(R.id.itemSearchContainer)
            val img = itemView.findViewById<ImageView>(R.id.itemSearchImg)
            val date = itemView.findViewById<TextView>(R.id.itemSearchTime)
            Glide.with(container).load(item.thumbnail).into(img)
            date.text = item.datetime
        }
    }
    inner class LoadingHolder(itemView: View) : ViewHolder(itemView){
        val progress = itemView.findViewById<TextView>(R.id.itemLoading)
    }

    override fun getItemViewType(position: Int): Int {
        return if(items[position].thumbnail=="null"){
            item
        }else{
            loading
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if(viewType== item){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
            ItemHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            LoadingHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(holder is ItemHolder){
            holder.bind(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}