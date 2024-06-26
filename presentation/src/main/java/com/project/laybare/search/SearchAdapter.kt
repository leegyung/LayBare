package com.project.laybare.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.project.domain.entity.ImageEntity
import com.project.laybare.databinding.SearchResultViewBinding

class SearchAdapter(private val mSearchList : ArrayList<ImageEntity>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var mListener : SearchAdapterInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SearchResultViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mSearchList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mSearchList.getOrNull(position)?.let{
            holder.bind(it)
        }
    }

    fun setListener(listener : SearchAdapterInterface?) {
        mListener = listener
    }


    inner class ViewHolder(private val mBinding : SearchResultViewBinding) : RecyclerView.ViewHolder(mBinding.root) {

        init{
            mBinding.SearchImage.clipToOutline = true
            mBinding.SearchImage.transitionName = "expand_image"
            mBinding.SearchImage.setOnClickListener {
                mSearchList.getOrNull(bindingAdapterPosition)?.let { image ->
                    mListener?.onImageClicked(mBinding.SearchImage, image.link, image.thumbnailLink)
                }
            }

        }

        fun bind(image : ImageEntity) {
            Glide.with(itemView.context)
                .load(image.link)
                .override(900)
                .error(Glide.with(itemView).load(image.thumbnailLink))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mBinding.SearchImage)
        }
    }


}