package com.project.laybare.search

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SearchAdapterDecorator(private val padding : Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        if(position % 2 == 1){
            outRect.apply {
                left = padding
                right = padding * 2
                bottom = padding * 2
            }
        }else{
            outRect.apply {
                left = padding * 2
                right = padding
                bottom = padding * 2
            }
        }
    }


}