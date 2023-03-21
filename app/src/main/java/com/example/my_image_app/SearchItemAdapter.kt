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

class SearchItemAdapter(private val searchItemList : ArrayList<RstListDto>, context: Context)
    : RecyclerView.Adapter<SearchItemAdapter.CustomViewHolder>(){
    private val context = context
    companion object{
        const val loading = 1
        const val item = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if(searchItemList[position].thumbnail=="null"){ loading }else{ item }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return  CustomViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        Glide.with(holder.itemContainer)
            .load(searchItemList[position].thumbnail).into(holder.itemImg)
        val bool = GlobalApplication.save.checkPref("key", searchItemList[position].thumbnail)

        if(bool){
            holder.itemCheck.setBackgroundResource(R.drawable.full_like)
        }

        val datetime = searchItemList[position].datetime
        holder.itemTime.text =
            datetime.substring(0,10)+"\n"+datetime.substring(11,13)+"시 "+datetime.substring(14,16)+"분"

        // 누르면 보관함에 보관
        holder.itemContainer.setOnClickListener {
            if(bool){
                GlobalApplication.save.removePref("key", searchItemList[position].thumbnail)
                Toast.makeText(context, "보관함에서 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                holder.itemCheck.setBackgroundResource(R.drawable.empty_like)
            }else{
                GlobalApplication.save.setPref("key", searchItemList[position].thumbnail)
                Toast.makeText(context, "보관함에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                holder.itemCheck.setBackgroundResource(R.drawable.full_like)
            }
        }

    }

    override fun getItemCount(): Int {
        return searchItemList.size
    }
    inner class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val itemContainer : ConstraintLayout = itemView.findViewById(R.id.itemSearchContainer)
        val itemImg : ImageView = itemView.findViewById(R.id.itemSearchImg)
        val itemTime : TextView = itemView.findViewById(R.id.itemSearchTime)
        val itemCheck : ImageView = itemView.findViewById(R.id.itemSaveCheck)
    }
}