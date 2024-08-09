package com.project.laybare.Location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.domain.entity.ImageEntity
import com.project.laybare.databinding.LocationImageViewBinding

class LocationImageAdapter(private val mPictureList : ArrayList<ImageEntity>) : RecyclerView.Adapter<LocationImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LocationImageViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mPictureList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mPictureList.getOrNull(position)?.let {
            holder.bind(it)
        }
    }

    inner class ViewHolder(private val mBinding : LocationImageViewBinding) : RecyclerView.ViewHolder(mBinding.root) {

        init{
            mBinding.LocationImageView.clipToOutline = true
        }
        fun bind(image : ImageEntity) {
            Glide.with(itemView.context)
                .load(image.link)
                .override(300, 600)
                .into(mBinding.LocationImageView)
        }
    }


}