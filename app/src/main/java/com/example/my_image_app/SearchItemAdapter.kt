package com.example.my_image_app

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SearchItemAdapter(private val context: Context, private val data: List<RstListDto>) :
    RecyclerView.Adapter<SearchItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val detail = item.datetime.toString()
        val spList = GlobalApplication.save.getPref("key", "null")
        val bool = spList.contains(SaveItemDto(item.thumbnail))
        if(bool){
            holder.saveChecked.setBackgroundResource(R.drawable.full_like)
        }
        holder.datetimeText.text =
            detail.substring(0,10)+" "+detail.substring(11,13)+"시 "+detail.substring(14,16)+"분"

        Glide.with(context)
            .load(item.thumbnail)
            .into(holder.imgContainer)


        holder.itemContainer.setOnClickListener {
            if(bool){
                GlobalApplication.save.removePref("key", item.thumbnail)
                Toast.makeText(context, "보관함에서 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                holder.saveChecked.setBackgroundResource(R.drawable.empty_like)
            }else{
                GlobalApplication.save.setPref("key", item.thumbnail)
                Toast.makeText(context, "보관함에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                holder.saveChecked.setBackgroundResource(R.drawable.full_like)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgContainer : ImageView = itemView.findViewById(R.id.itemSearchImg)
        val datetimeText : TextView = itemView.findViewById(R.id.itemSearchTime)
        val saveChecked : ImageView = itemView.findViewById(R.id.itemSaveCheck)
        val itemContainer: ConstraintLayout = itemView.findViewById(R.id.itemSearchContainer)
    }
}

