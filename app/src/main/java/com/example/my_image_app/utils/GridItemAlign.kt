package com.example.my_image_app.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

// recyclerview item 정렬
class GridItemAlign(private val spanCnt : Int, private val space : Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCnt + 1

        if (position < spanCnt){
            outRect.top = space
        }
        if (column == spanCnt){
            outRect.right = space
        }
        outRect.left = space
        outRect.bottom = space
    }
}