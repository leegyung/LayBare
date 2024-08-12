package com.project.laybare.fragment.home

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.project.domain.entity.ImageEntity
import com.project.laybare.databinding.HomeHorizontalImageViewBinding

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
                mImageList.getOrNull(bindingAdapterPosition)?.let{ image ->
                    mListener?.onImageClicked(if(image.linkError) image.thumbnailLink else image.link)
                }
            }
        }

        fun bind(image : ImageEntity) {
            Glide.with(itemView.context)
                .load(if(image.linkError) image.thumbnailLink else image.link)
                .override(200,400)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        if(!image.linkError){
                            image.linkError = true
                        }
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(Glide.with(itemView.context).load(image.thumbnailLink))
                .into(mBinding.HorizontalImageView)
        }
    }


}