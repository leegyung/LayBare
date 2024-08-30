package com.project.laybare.fragment.ImageDetail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
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

        mBinding.ImageDetailOptionList.apply {
            itemAnimator = null
            animation = null
            adapter = mViewModel.getOptionAdapter()
            if(itemDecorationCount == 0) {
                addItemDecoration(ImageDetailOptionDecorator(resources.getDimensionPixelSize(R.dimen.dp_50)))
            }
        }


        initListener()
        initObserver()
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    mViewModel.mCreateAlert.collectLatest {
                        createDialog(it, false)
                    }
                }

                launch {
                    mViewModel.mApiLoading.collectLatest {
                        mBinding.ImageDetailProgress.isVisible = it
                    }
                }

                launch {
                    mViewModel.mTextRecognitionResult.collectLatest {
                        mNavController.navigate(R.id.action_imageDetail_to_textResult)
                    }
                }

                launch {
                    mViewModel.mLandmarkResult.collectLatest {
                        mNavController.navigate(R.id.action_imageDetail_to_location)
                    }
                }

                launch {
                    mViewModel.mContractResult.collectLatest {
                        mNavController.navigate(R.id.action_imageDetail_to_contact)
                    }
                }
            }
        }

    }

    private fun initListener() {
        mBinding.ImageDetailBackBtn.setOnClickListener {
            mNavController.popBackStack()
        }

        mViewModel.getOptionAdapter().setOptionClickListener(object : ImageDetailOptionListener{
            override fun onOptionClicked(option: String) {
                when(option) {
                    "download" -> {
                        mViewModel.downloadImage(mContext)
                    }
                    "text" -> {
                        mViewModel.extractText(mBinding.ImageDetailImage.drawable?.toBitmap(), false)
                    }
                    "location" -> {
                        mViewModel.getLandmarkData(mBinding.ImageDetailImage.drawable?.toBitmap())
                    }
                    "contact" -> {
                        mViewModel.extractText(mBinding.ImageDetailImage.drawable?.toBitmap(), true)
                    }
                    "similar" -> {
                        mViewModel.extractImageLabel(mBinding.ImageDetailImage.drawable?.toBitmap())
                    }
                }
            }
        })


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