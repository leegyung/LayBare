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
import com.project.laybare.databinding.ImageViewMatchParentBinding

class HomeBannerAdapter : RecyclerView.Adapter<HomeBannerAdapter.ViewHolder>() {
    private val mImageList = arrayListOf<ImageEntity>()
    private var mListener : HomeImageListListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ImageViewMatchParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun setListener(listener : HomeImageListListener?) {
        mListener = listener
    }

    inner class ViewHolder(private val mBinding: ImageViewMatchParentBinding) : RecyclerView.ViewHolder(mBinding.root) {
        init {
            mBinding.MatchParentImageView.clipToOutline = true


            mBinding.MatchParentImageView.setOnClickListener {
                mImageList.getOrNull(bindingAdapterPosition)?.let{
                    mListener?.onImageClicked(if(it.linkError) it.thumbnailLink else it.link)
                }
            }

        }

        fun bind(image : ImageEntity) {
            Glide.with(itemView.context)
                .load(if(image.linkError) image.thumbnailLink else image.link)
                .override(900)
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
                .into(mBinding.MatchParentImageView)
        }
    }


}