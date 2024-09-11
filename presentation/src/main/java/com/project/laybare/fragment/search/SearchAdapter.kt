package com.project.laybare.fragment.search

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.project.domain.entity.ImageEntity
import com.project.laybare.R
import com.project.laybare.databinding.SearchResultViewBinding

class SearchAdapter(private val mSearchList : ArrayList<ImageEntity>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    companion object {
        private const val PRELOAD_SIZE = 10
    }


    private var mListener : SearchAdapterInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SearchResultViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mSearchList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mSearchList[position])

        preload(holder, position)
    }

    fun setListener(listener : SearchAdapterInterface?) {
        mListener = listener
    }

    private fun preload(holder : ViewHolder, currentPosition: Int) {
        val endPosition = (currentPosition + PRELOAD_SIZE).coerceAtMost(mSearchList.size - 1)

        mSearchList
            .subList(currentPosition, endPosition)
            .forEach { data -> if(!data.linkError) preload(holder.itemView.context, data.link) }


    }

    private fun preload(context: Context, url: String) {

        Glide.with(context)
            .load(url)
            .override(600, 1200)
            .transform(CenterCrop(), RoundedCorners(context.resources.getDimensionPixelSize(R.dimen.dp_8)))
            .preload()
    }



    inner class ViewHolder(private val mBinding : SearchResultViewBinding) : RecyclerView.ViewHolder(mBinding.root) {

        init{
            mBinding.SearchImage.clipToOutline = true
            mBinding.SearchImage.transitionName = "expand_image"
            mBinding.SearchImage.setOnClickListener {
                mSearchList.getOrNull(bindingAdapterPosition)?.let { image ->
                    mListener?.onImageClicked(if(image.linkError) image.thumbnailLink else image.link)
                }
            }

        }

        fun bind(image : ImageEntity) {


            if(!image.linkError) {

                Glide.with(itemView.context)
                    .load(image.link)
                    .override(600, 1200)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            image.linkError = true
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }
                    })
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(mBinding.SearchImage)

            }

        }
    }


}