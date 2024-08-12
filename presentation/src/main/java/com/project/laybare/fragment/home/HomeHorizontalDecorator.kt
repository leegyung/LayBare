package com.project.laybare.fragment.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HomeHorizontalDecorator(private val padding : Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val total = parent.adapter?.itemCount?:0
        val position = parent.getChildAdapterPosition(view)

        when(position) {
            0 -> {
                outRect.apply {
                    left = padding * 2
                    right = padding
                }
            }
            total - 1 -> {
                outRect.right = padding * 2
            }
            else -> {
                outRect.right = padding
            }
        }

    }
}