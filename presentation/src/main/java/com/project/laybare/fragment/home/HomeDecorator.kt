package com.project.laybare.fragment.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HomeDecorator(private val padding : Int) : RecyclerView.ItemDecoration() {

    private val BANNER_TYPE = 1
    private val HORIZONTAL_TYPE = 2
    private val PICTURE_TYPE = 3

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val viewType = parent.adapter?.getItemViewType(position)

        when(viewType){
            PICTURE_TYPE -> {
                outRect.apply {
                    top = padding
                    bottom = padding
                    left = padding
                    right = padding
                }
            }
            HORIZONTAL_TYPE -> {
                val next = parent.adapter?.getItemViewType(position + 1)
                if(next == PICTURE_TYPE) {
                    outRect.bottom = padding * 4
                }else{
                    outRect.bottom = 0
                }
            }
            BANNER_TYPE -> {

            }
        }
    }
}