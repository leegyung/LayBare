package com.project.laybare.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.project.domain.entity.ImageEntity
import com.project.laybare.databinding.HomeBannerViewBinding
import com.project.laybare.databinding.HomeHorizontalViewBinding
import com.project.laybare.databinding.HomeNormalImageViewBinding
import com.project.laybare.home.data.HomeListSectionData

class HomeAdapter(private val mSections : ArrayList<HomeListSectionData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val BANNER_TYPE = 1
    private val HORIZONTAL_TYPE = 2
    private val PICTURE_TYPE = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == BANNER_TYPE) {
            val view = HomeBannerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return BannerViewHolder(view)
        }

        if(viewType == HORIZONTAL_TYPE) {
            val view = HomeHorizontalViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return HorizontalViewHolder(view)
        }


        val view = HomeNormalImageViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return when(mSections.getOrNull(position)?.section) {
            "BANNER" -> BANNER_TYPE
            "HORIZONTAL" -> HORIZONTAL_TYPE
            else -> PICTURE_TYPE
        }

    }

    override fun getItemCount(): Int {
        return mSections.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is BannerViewHolder -> {
                mSections.getOrNull(position)?.imageList?.let{
                    holder.bind(it)
                }
            }
            is HorizontalViewHolder -> {
                mSections.getOrNull(position)?.let{
                    holder.bind(it)
                }
            }
            is ImageViewHolder -> {
                mSections.getOrNull(position)?.image?.let{
                    holder.bind(it)
                }
            }
        }

    }

    inner class BannerViewHolder(private val mBinding : HomeBannerViewBinding) : RecyclerView.ViewHolder(mBinding.root) {
        private val mAdapter = HomeBannerAdapter()
        fun bind(imageList : ArrayList<ImageEntity>) {

            val pageTransformer = CompositePageTransformer().apply {
                /*
                // 페이지 간의 간격을 40dp로 설정
                addTransformer(MarginPageTransformer(0))
                // 스케일링 효과 추가
                addTransformer { page, position ->
                    val scale = 1 - abs(position) * 0.1f
                    page.scaleY = scale
                }
                 */
                // 이전 페이지와 다음 페이지를 틸트 효과 추가
                addTransformer { page, position ->
                    page.rotationY = -10f * position
                }
            }

            mAdapter.setImageList(imageList)
            mBinding.HomeBanner.apply {
                offscreenPageLimit = 1
                setPageTransformer(pageTransformer)
                adapter = mAdapter
            }

            mBinding.HomeBanner.doOnPreDraw {
                mBinding.HomeBanner.setCurrentItem(3, false)
            }

        }
    }

    inner class HorizontalViewHolder(private val mBinding : HomeHorizontalViewBinding) : RecyclerView.ViewHolder(mBinding.root) {
        private val mAdapter = HomeHorizontalAdapter()
        fun bind(data : HomeListSectionData) {
            mBinding.HorizontalViewTitle.text = data.keyword
            mAdapter.setImageList(data.imageList?: arrayListOf())
            mBinding.HorizontalViewList.adapter = mAdapter
        }
    }

    inner class ImageViewHolder(private val mBinding : HomeNormalImageViewBinding) : RecyclerView.ViewHolder(mBinding.root) {
        fun bind(img : ImageEntity) {
            Glide.with(itemView.context)
                .load(img.link)
                .override(900)
                .error(Glide.with(itemView.context).load(img.thumbnailLink))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mBinding.HomeNormalImageView)

        }
    }


}