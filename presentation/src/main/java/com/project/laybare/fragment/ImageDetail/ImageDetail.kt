package com.project.laybare.fragment.ImageDetail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.project.laybare.R
import com.project.laybare.databinding.FragmentImageDetailBinding
import com.project.laybare.dialog.AlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ImageDetail : Fragment() {
    private var _binding : FragmentImageDetailBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mContext : Context
    private lateinit var mNavController: NavController
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

        mNavController = findNavController()

        if(mViewModel.requireImageDataSetting()) {
            checkResource()
        }

        initUI()
    }


    private fun checkResource() {
        val type = arguments?.getString("imageType")?:""
        val url = arguments?.getString("imageUrl")?:""
        val uri = arguments?.getString("imageUri")?:""

        if(type.isEmpty() && url.isEmpty() && uri.isEmpty()){
            Toast.makeText(mContext, "이미지 로딩 애러", Toast.LENGTH_SHORT).show()
            return
        }

        mViewModel.setImageData(type, url, uri.toUri())
    }


    private fun initUI(){
        val isUrlType = mViewModel.isUrlType()

        Glide.with(this).load(
            if(isUrlType){
                mViewModel.getImageUrl()
            }else{
                mViewModel.getImageUri()
            }
        ).into(mBinding.ImageDetailImage)

        mBinding.ImageDetailDownload.isVisible = isUrlType

        initListener()
        initObserver()
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.mCreateAlert.collectLatest {
                mBinding.ImageDetailProgress.isVisible = false
                createDialog(it)
                //Snackbar.make(mBinding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.mTextRecognitionResult.collectLatest { result ->
                mNavController.navigate(R.id.action_imageDetail_to_textResult, bundleOf("ExtractedText" to result))
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.mLandmarkResult.collectLatest { result ->
                mBinding.ImageDetailProgress.isVisible = false

                val bundle = Bundle().apply {
                    putString("location", Gson().toJson(result))
                }
                mNavController.navigate(R.id.action_imageDetail_to_location, bundle)
            }
        }


    }

    private fun initListener() {
        mBinding.ImageDetailBackBtn.setOnClickListener {
            mNavController.popBackStack()
        }

        // 사진 다운로드 버튼 클릭 리스너
        mBinding.ImageDetailDownload.setOnClickListener {
            mViewModel.downloadImage(mContext)
        }
        // 사진 텍스트 추출 리스너
        mBinding.ImageDetailTextRecognize.setOnClickListener {
            mViewModel.extractText(mBinding.ImageDetailImage.drawable?.toBitmap())
        }
        // 위치 찾기 버튼 리스너
        mBinding.ImageDetailLocation.setOnClickListener {
            mBinding.ImageDetailProgress.isVisible = true
            mViewModel.getLocationData(mBinding.ImageDetailImage.drawable?.toBitmap())
        }
    }

    private fun createDialog(msg : String) {

        val width = resources.displayMetrics.widthPixels
        val constructor = AlertDialog(mContext, width)
        val dialog = constructor.createDialog(1, msg, "확인")
        dialog.setCancelable(true)

        constructor.setItemClickListener(object : AlertDialog.AlertDialogClickListener{
            override fun onClickOk() {
                dialog.dismiss()
            }

            override fun onClickCancel() {
                dialog.dismiss()
            }
        })

        dialog.show()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}