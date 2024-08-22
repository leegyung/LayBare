package com.project.laybare.fragment.ImageDetail

import android.content.Context
import android.net.Uri
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
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

        if(mViewModel.getImageUrl().isEmpty()){
            createDialog("이미지 데이터를 가져오지 못했어요...", true)
        }else{
            initUI()
        }
    }



    private fun initUI(){
        val image = mViewModel.getImageUrl()

        Glide.with(this)
            .load(image)
            .into(mBinding.ImageDetailImage)


        initListener()
        initObserver()
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.mCreateAlert.collectLatest {
                createDialog(it, false)
                //Snackbar.make(mBinding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.mApiLoading.collectLatest {
                mBinding.ImageDetailProgress.isVisible = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.mTextRecognitionResult.collectLatest {
                mNavController.navigate(R.id.action_imageDetail_to_textResult)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.mLandmarkResult.collectLatest {
                mNavController.navigate(R.id.action_imageDetail_to_location)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.mContractResult.collectLatest {
                mNavController.navigate(R.id.action_imageDetail_to_contact)
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
            mViewModel.extractText(mBinding.ImageDetailImage.drawable?.toBitmap(), false)
        }
        // 위치 찾기 버튼 리스너
        mBinding.ImageDetailLocation.setOnClickListener {
            mViewModel.getLandmarkData(mBinding.ImageDetailImage.drawable?.toBitmap())
        }

        mBinding.ImageDetailContact.setOnClickListener {
            mViewModel.extractText(mBinding.ImageDetailImage.drawable?.toBitmap(), true)
        }
    }

    private fun createDialog(msg : String, isPop : Boolean) {

        val width = resources.displayMetrics.widthPixels
        val constructor = AlertDialog(mContext, width)
        val dialog = constructor.createDialog(1, msg, "확인")
        dialog.setCancelable(!isPop)

        constructor.setItemClickListener(object : AlertDialog.AlertDialogClickListener{
            override fun onClickOk() {
                dialog.dismiss()
                if(isPop){
                    mNavController.popBackStack()
                }
            }

            override fun onClickCancel() {}
        })

        dialog.show()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}