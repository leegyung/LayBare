package com.project.laybare.fragment.contact

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.project.laybare.R
import com.project.laybare.databinding.FragmentContactBinding
import com.project.laybare.ssot.ImageDetailData
import com.project.laybare.util.PhotoTaker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Contact : Fragment() {
    private val mViewModel : ContactViewModel by viewModels()
    private var _binding : FragmentContactBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mNavController: NavController
    private lateinit var mPhotoTaker : PhotoTaker

    // 사진 촬영 어플에서 사진 찍은 결과 리스너
    private val mTakePictureResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            ImageDetailData.setNewImageData(mPhotoTaker.getPhotoUri().toString())
            findNavController().navigate(R.id.action_home_to_imageDetail)
        }
    }
    // 이미지 선택 다이얼로그에서 선택된 이미지 리스너
    private val mPickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            ImageDetailData.setNewImageData(uri.toString())
            findNavController().navigate(R.id.action_home_to_imageDetail)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mNavController = findNavController()

        initUI()
    }

    private fun initListener() {

    }


    private fun initUI() {
        Glide.with(requireContext())
            .load(mViewModel.getImageUrl())
            .into(mBinding.ContactImage)


        mBinding.ContactNumberList.apply {
            if(itemDecorationCount == 0){
                addItemDecoration(ContactListDecorator(resources.getDimensionPixelSize(R.dimen.dp_10)))
            }
            adapter = mViewModel.getNumberAdapter()
        }

        mBinding.ContactEmailList.apply {
            if(itemDecorationCount == 0){
                addItemDecoration(ContactListDecorator(resources.getDimensionPixelSize(R.dimen.dp_10)))
            }
            adapter = mViewModel.getEmailAdapter()
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}