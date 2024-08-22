package com.project.laybare.fragment.contact

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ContactListDecorator(private val padding : Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val total = parent.adapter?.itemCount?:0
        val position = parent.getChildAdapterPosition(view)

        if(position != total - 1){
            outRect.bottom = padding
        }
    }
}