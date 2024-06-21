package com.project.laybare.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.project.domain.entity.ImageEntity
import com.project.laybare.databinding.HomeHorizontalImageViewBinding

class HomeHorizontalAdapter : RecyclerView.Adapter<HomeHorizontalAdapter.ViewHolder>() {
    private val mImageList = arrayListOf<ImageEntity>()

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

    inner class ViewHolder(private val mBinding: HomeHorizontalImageViewBinding) : RecyclerView.ViewHolder(mBinding.root) {
        init {
            //mBinding.HorizontalImageView.clipToOutline = true
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