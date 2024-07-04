package com.project.laybare.home.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.project.domain.entity.ImageEntity
import com.project.laybare.R
import com.project.laybare.databinding.HomeBannerViewBinding
import com.project.laybare.databinding.HomeHorizontalViewBinding
import com.project.laybare.databinding.HomeNormalImageViewBinding
import com.project.laybare.home.HomeListInterface
import com.project.laybare.home.data.HomeListSectionData

class HomeAdapter(private val mSections : ArrayList<HomeListSectionData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListener : HomeListInterface? = null

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

    fun setListener(listener : HomeListInterface?) {
        mListener = listener
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
                    holder.bind(it, position)
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

            mAdapter.apply {
                setImageList(imageList)
                setListener(mListener)
            }
            mBinding.HomeBanner.apply {
                offscreenPageLimit = 1
                setPageTransformer(pageTransformer)
                adapter = mAdapter
            }


        }
    }

    inner class HorizontalViewHolder(private val mBinding : HomeHorizontalViewBinding) : RecyclerView.ViewHolder(mBinding.root) {
        private val mAdapter = HomeHorizontalAdapter()
        fun bind(data : HomeListSectionData, pos : Int) {
            mBinding.HorizontalViewTitle.text = data.keyword
            mAdapter.apply {
                setImageList(data.imageList?: arrayListOf())
                setListener(mListener)
            }
            mBinding.HorizontalViewList.apply {
                if(itemDecorationCount == 0){
                    addItemDecoration(HomeHorizontalDecorator(resources.getDimensionPixelSize(R.dimen.dp_10)))
                }
                adapter = mAdapter
            }

            setBrush(pos)
        }

        private fun setBrush(pos : Int) {
            when(pos){
                1 -> mBinding.imageView2.setImageResource(R.drawable.brush1)
                2 -> mBinding.imageView2.setImageResource(R.drawable.brush2)
                3 -> mBinding.imageView2.setImageResource(R.drawable.brush3)
                else -> mBinding.imageView2.setImageResource(R.drawable.brush7)
            }
        }
    }

    inner class ImageViewHolder(private val mBinding : HomeNormalImageViewBinding) : RecyclerView.ViewHolder(mBinding.root) {

        init {
            mBinding.HomeNormalImageView.clipToOutline = true
            mBinding.HomeNormalImageView.setOnClickListener {
                mSections.getOrNull(bindingAdapterPosition)?.image?.let{
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
                .error(Glide.with(itemView.context).load(image.thumbnailLink))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mBinding.HomeNormalImageView)

        }
    }


}