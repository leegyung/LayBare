package com.project.laybare.fragment.ImageDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.laybare.R
import com.project.laybare.databinding.ImageDetailOptionViewBinding




interface ImageDetailOptionListener{
    fun onOptionClicked(option : String)
}


class ImageDetailOptionAdapter : RecyclerView.Adapter<ImageDetailOptionAdapter.ViewHolder>() {

    private var mListener : ImageDetailOptionListener? = null
    private val mOptions = arrayListOf(
        "download",
        "text",
        "location",
        "contact",
        "label"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ImageDetailOptionViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mOptions.getOrNull(position)?.let{
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
        return mOptions.size
    }

    fun setOptionClickListener(listener: ImageDetailOptionListener) {
        mListener = listener
    }

    inner class ViewHolder(private val mBinding : ImageDetailOptionViewBinding) : RecyclerView.ViewHolder(mBinding.root) {

        init {
            mBinding.ImageDetailOption.setOnClickListener {
                mOptions.getOrNull(bindingAdapterPosition)?.let{
                    mListener?.onOptionClicked(it)
                }
            }
        }

        fun bind(option : String) {
            when(option){
                "download" -> {
                    mBinding.ImageDetailOption.setBackgroundResource(R.drawable.download)
                }
                "text" -> {
                    mBinding.ImageDetailOption.setBackgroundResource(R.drawable.text_icon)
                }
                "location" -> {
                    mBinding.ImageDetailOption.setBackgroundResource(R.drawable.location_icon)
                }
                "contact" -> {
                    mBinding.ImageDetailOption.setBackgroundResource(R.drawable.business_card)
                }
                "label" -> {
                    mBinding.ImageDetailOption.setBackgroundResource(R.drawable.similar_image_icon)
                }
            }
        }
    }
}