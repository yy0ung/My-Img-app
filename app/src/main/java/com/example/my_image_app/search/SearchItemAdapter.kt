package com.example.my_image_app.search

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
import com.bumptech.glide.Glide
import com.example.my_image_app.R
import com.example.my_image_app.retrofit.dto.RstListDto
import com.example.my_image_app.retrofit.dto.SaveItemDto
import com.example.my_image_app.utils.GlobalApplication

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

        var spList = GlobalApplication.save.getPref("key", "null")
        var bool = spList.contains(SaveItemDto(item.thumbnail))
        if(bool){
            holder.saveChecked.setBackgroundResource(R.drawable.full_like)
        }

        val detail = item.datetime
        holder.datetimeText.text =
            detail.substring(0,10)+" "+detail.substring(11,13)+"시 "+detail.substring(14,16)+"분"

        Glide.with(context)
            .load(item.thumbnail)
            .into(holder.imgContainer)


        holder.itemContainer.setOnClickListener {
            // 사진 선택할 때마다 SharedPreference에 저장되어있는 사진인지 확인하고 보관함에서 추가/삭제
            spList = GlobalApplication.save.getPref("key", "null")
            bool = spList.contains(SaveItemDto(item.thumbnail))
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
