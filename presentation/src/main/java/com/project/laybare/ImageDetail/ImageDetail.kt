package com.project.laybare.ImageDetail

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.project.laybare.R
import com.project.laybare.databinding.FragmentImageDetailBinding


class ImageDetail : Fragment() {
    private var _binding : FragmentImageDetailBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mContext : Context


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContext = requireContext()
        _binding = FragmentImageDetailBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = arguments?.getString("imageUrl")
        val thumbnail = arguments?.getString("thumbnail")

        if(!url.isNullOrEmpty()){
            initUI(url)
        }else{
            if(!thumbnail.isNullOrEmpty()){
                initUI(thumbnail)
            }else{
                Toast.makeText(mContext, "이미지 링크 오류", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun initUI(url : String){
        Glide.with(this)
            .load(url)
            .into(mBinding.MatchParentImageView)
    }

}