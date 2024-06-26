package com.project.laybare.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.project.domain.entity.ImageEntity
import com.project.laybare.databinding.HomeHorizontalImageViewBinding
import com.project.laybare.home.HomeListInterface

class HomeHorizontalAdapter : RecyclerView.Adapter<HomeHorizontalAdapter.ViewHolder>() {
    private val mImageList = arrayListOf<ImageEntity>()
    private var mListener : HomeListInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = HomeHorizontalImageViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mImageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mImageList.getOrNull(position)?.let{
            holder.bind(it)
        }
    }

    fun setImageList(list : ArrayList<ImageEntity>) {
        mImageList.apply {
            clear()
            addAll(list)
        }
    }

    fun setListener(listener : HomeListInterface?) {
        mListener = listener
    }

    inner class ViewHolder(private val mBinding: HomeHorizontalImageViewBinding) : RecyclerView.ViewHolder(mBinding.root) {
        init {
            mBinding.HorizontalImageView.clipToOutline = true
            mBinding.HorizontalImageView.setOnClickListener {
                mImageList.getOrNull(bindingAdapterPosition)?.let{
                    mListener?.onImageClicked(it, mBinding.HorizontalImageView)
                }
            }
        }

        fun bind(image : ImageEntity) {
            Glide.with(itemView.context)
                .load(image.link)
                .override(200,400)
                .error(Glide.with(itemView.context).load(image.thumbnailLink))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mBinding.HorizontalImageView)
        }
    }


}