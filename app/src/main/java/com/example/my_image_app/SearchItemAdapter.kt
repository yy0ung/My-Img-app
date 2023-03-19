package com.example.my_image_app

import android.content.ContentValues.TAG
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL

class SearchItemAdapter(private val searchItemList : RetrofitSearchImg)
    : RecyclerView.Adapter<SearchItemAdapter.CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return  CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        Glide.with(holder.itemContainer)
            .load(searchItemList.documents[position].thumbnail).into(holder.itemImg)
        holder.itemTime.text = searchItemList.documents[position].datetime

        // 저장된 사진 표시
        if(GlobalApplication.save.getPref("key", "no")==searchItemList.documents[position].thumbnail){
            holder.itemCheck.setBackgroundResource(R.drawable.ic_launcher_background)
        }

        // 누르면 보관함에 보관
        holder.itemContainer.setOnClickListener {
            GlobalApplication.save.setPref("key", searchItemList.documents[position].thumbnail)
        }

    }

    override fun getItemCount(): Int {
        return searchItemList.documents.size
    }
    inner class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val itemContainer : ConstraintLayout = itemView.findViewById(R.id.itemSearchContainer)
        val itemImg : ImageView = itemView.findViewById(R.id.itemSearchImg)
        val itemTime : TextView = itemView.findViewById(R.id.itemSearchTime)
        val itemCheck : ImageView = itemView.findViewById(R.id.itemSaveCheck)
    }
}