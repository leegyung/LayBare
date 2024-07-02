package com.project.laybare.ImageDetail

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.project.laybare.R
import com.project.laybare.databinding.FragmentImageDetailBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ImageDetail : Fragment() {
    private var _binding : FragmentImageDetailBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mContext : Context
    private val mViewModel : ImageDetailViewModel by viewModels()


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


        if(mViewModel.requireUrlSetting()){
            val url = arguments?.getString("imageUrl")?:""
            val thumbnail = arguments?.getString("thumbnail")?:""
            if(url.isEmpty() && thumbnail.isEmpty()){
                Toast.makeText(mContext, "이미지 로딩 애러", Toast.LENGTH_SHORT).show()
            }else{
                mViewModel.setImageUrl(url, thumbnail)
                initUI()
            }
        }else{
            initUI()
        }


    }

    private fun initUI(){
        Glide.with(this)
            .load(mViewModel.getImageUrl())
            .error(
                Glide.with(this)
                    .load(mViewModel.getThumbnail())
            )
            .into(mBinding.ImageDetailImage)

        initListener()
        initObserver()
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.mCreateToast.collectLatest {
                if(it.isNotEmpty()){
                    Toast.makeText(mContext, it, Toast.LENGTH_SHORT).show()
                    mViewModel.mCreateToast.value = ""
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.mTextRecognitionResult.collectLatest { result ->
                if(result.isNotEmpty()){
                    Log.v("라인", result)
                    mViewModel.mTextRecognitionResult.value = ""
                }
            }
        }


    }

    private fun initListener() {
        // 사진 다운로드 버튼 클릭 리스너
        mBinding.ImageDetailDownload.setOnClickListener {
            mViewModel.downloadImage(mContext)
        }
        // 사진 텍스트 추출 리스너
        mBinding.ImageDetailTextRecognize.setOnClickListener {
            mViewModel.extractText(mBinding.ImageDetailImage.drawable?.toBitmap())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}