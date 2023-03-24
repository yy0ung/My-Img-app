package com.example.my_image_app

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.my_image_app.retrofit.dto.RstListDto
import com.example.my_image_app.retrofit.dto.SaveItemDto
import com.example.my_image_app.utils.GlideApp
import com.example.my_image_app.utils.GlobalApplication

class SearchItemAdapter(private val context: Context, private val data: List<RstListDto>) : RecyclerView.Adapter<ViewHolder>(){
    companion object {
        const val ITEM = 1
        const val LOADING = 0
    }

    inner class ItemViewHolder(itemView: View) : ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindItems(item: RstListDto) {
            val imgContainer : ImageView = itemView.findViewById(R.id.itemSearchImg)
            val datetimeText : TextView = itemView.findViewById(R.id.itemSearchTime)
            val saveChecked : ImageView = itemView.findViewById(R.id.itemSaveCheck)

            val spList = GlobalApplication.save.getPref("key", "null")
            val bool = spList.contains(SaveItemDto(item.thumbnail))
            val detail = item.datetime

            datetimeText.text =
                detail.substring(0,10)+" "+detail.substring(11,13)+"시 "+detail.substring(14,16)+"분"

            if(bool){
                saveChecked.setBackgroundResource(R.drawable.full_like)
            }

            GlideApp.with(context)
                .load(item.thumbnail)
                .into(imgContainer)



        }
    }

    inner class LoadingViewHolder(itemView: View) : ViewHolder(itemView) {
        val progressBar = itemView.findViewById<TextView>(R.id.itemLoading)!!
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].thumbnail != "null") {
            ITEM
        } else {
            LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == ITEM) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
            ItemViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bindItems(data[position])

            holder.itemView.findViewById<ConstraintLayout>(R.id.itemSearchContainer).setOnClickListener {
                val spList = GlobalApplication.save.getPref("key", "null")
                val bool = spList.contains(SaveItemDto(data[position].thumbnail))
                if(bool){
                    GlobalApplication.save.removePref("key", data[position].thumbnail)
                    Toast.makeText(context, "보관함에서 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    holder.itemView.findViewById<ImageView>(R.id.itemSaveCheck).setBackgroundResource(R.drawable.empty_like)
                }else{
                    GlobalApplication.save.setPref("key", data[position].thumbnail)
                    Toast.makeText(context, "보관함에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                    holder.itemView.findViewById<ImageView>(R.id.itemSaveCheck).setBackgroundResource(R.drawable.full_like)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }
}
