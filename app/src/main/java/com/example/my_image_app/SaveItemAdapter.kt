package com.example.my_image_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SaveItemAdapter(private val saveItemList : ArrayList<SaveListDto>)
    : RecyclerView.Adapter<SaveItemAdapter.CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_save, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        Glide.with(holder.itemContainer)
            .load(saveItemList[position].thumbnail).into(holder.itemImg)

    }

    override fun getItemCount(): Int {
        return saveItemList.size
    }

    inner class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val itemContainer : ConstraintLayout = itemView.findViewById(R.id.itemSaveContainer)
        val itemImg : ImageView = itemView.findViewById(R.id.itemSaveImg)
        val itemCheck : ImageView = itemView.findViewById(R.id.itemSaveCheck)
    }
}