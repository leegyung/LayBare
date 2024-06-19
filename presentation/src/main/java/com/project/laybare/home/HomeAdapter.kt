package com.project.laybare.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.project.domain.entity.ImageEntity
import com.project.laybare.databinding.HomeBannerViewBinding
import kotlin.math.abs

class HomeAdapter(private val mSections : ArrayList<HomeListSectionData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val BANNER_TYPE = 1
    private val HORIZONTAL_TYPE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == BANNER_TYPE) {
            val view = HomeBannerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return BannerViewHolder(view)
        }


        val view = HomeBannerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        when(mSections.getOrNull(position)?.section) {
            "BANNER" -> return BANNER_TYPE
            else -> return HORIZONTAL_TYPE
        }

    }

    override fun getItemCount(): Int {
        return mSections.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is BannerViewHolder) {
            mSections.getOrNull(position)?.imageList?.let{
                holder.bind(it)
            }
        }
    }

    inner class BannerViewHolder(private val mBinding : HomeBannerViewBinding) : RecyclerView.ViewHolder(mBinding.root) {
        private val mAdapter = HomeBannerAdapter()
        fun bind(imageList : ArrayList<ImageEntity>) {

            /*
            val pageTransformer = CompositePageTransformer().apply {
                addTransformer(MarginPageTransformer(40)) // 페이지 간의 간격을 40dp로 설정
                addTransformer { page, position ->
                    val scale = 1 - abs(position) * 0.1f
                    page.scaleY = scale
                }
            }

             */

            val pageTransformer = CompositePageTransformer().apply {
                // 페이지 간의 간격을 40dp로 설정
                addTransformer(MarginPageTransformer(0))
                // 스케일링 효과 추가
                addTransformer { page, position ->
                    //val scale = 1 - abs(position) * 0.1f
                    //page.scaleY = scale
                }
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
        }
    }


}