package com.project.laybare.home

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.project.laybare.R
import com.project.laybare.databinding.FragmentHomeBinding
import com.project.laybare.dialog.AlertDialog
import com.project.laybare.dialog.ImageSelectDialog
import com.project.laybare.dialog.ImageSelectDialogListener
import com.project.laybare.util.PhotoTaker
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Home : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel : HomeViewModel by viewModels()
    private lateinit var mNavController: NavController
    private var mListInterface : HomeListInterface? = null
    private lateinit var mPhotoTaker : PhotoTaker

    // 사진 촬영 어플에서 사진 찍은 결과 리스너
    private val mTakePictureResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            val bundle = bundleOf("imageUri" to mPhotoTaker.getPhotoUri().toString(), "imageType" to "URI")
            findNavController().navigate(R.id.action_home_to_imageDetail, bundle)
        }
    }
    // 이미지 선택 다이얼로그에서 선택된 이미지 리스너
    private val mPickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val bundle = bundleOf("imageUri" to it.toString(), "imageType" to "URI")
            findNavController().navigate(R.id.action_home_to_imageDetail, bundle)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mNavController = findNavController()

        initUI()

        if(mViewModel.requireImageData()){
            mViewModel.getInitialData()
        }

    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.mApiError.collectLatest {
                imageLoadErrorDialog(it)
            }
        }
    }



    private fun initListener(){
        // 사진 리스트 리스너
        mListInterface = object : HomeListInterface{
            override fun onImageClicked(image: String) {
                val bundle = bundleOf("imageUrl" to image, "imageType" to "URL")
                findNavController().navigate(R.id.action_home_to_imageDetail, bundle)
            }
        }

        // 검색 버튼 클릭 리스너
        mBinding.HomeSearchBtn.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                mBinding.HomeSearchBtn to "shared_text",
                mBinding.HomeLogo to "shared_logo"
            )
            mNavController.navigate(R.id.action_home_to_search, null, null, extras)
        }

        // 사진기 버튼 클릭 리스너
        mBinding.HomeCameraBtn.setOnClickListener {
            createImageSelectOptionDialog()
        }


    }

    private fun initUI() {
        initObserver()
        initListener()


        val layoutManager = GridLayoutManager(this.context, 2)
        val adapter = mViewModel.getHomeAdapter(mListInterface)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    3 -> 1
                    else -> 2
                }
            }
        }

        mBinding.HomeRecyclerView.apply {
            if(itemDecorationCount == 0){
                addItemDecoration(HomeDecorator(resources.getDimensionPixelSize(R.dimen.dp_10)))
            }
            setHasFixedSize(true)
            this.layoutManager =layoutManager
            this.adapter = adapter
        }
    }


    private fun createImageSelectOptionDialog() {
        val dialog = ImageSelectDialog()
        dialog.setImageSelectDialogListener(object : ImageSelectDialogListener{
            override fun onAlbumClicked() {
                mPickImage.launch("image/*")
            }
            override fun onCameraClicked() {
                if(!::mPhotoTaker.isInitialized){
                    mPhotoTaker = PhotoTaker(requireActivity())
                }

                val permissionGranted = mPhotoTaker.checkCameraPermission()
                if(permissionGranted){
                    mPhotoTaker.dispatchTakePictureIntent(mTakePictureResult)
                }
            }

        })
        dialog.show(childFragmentManager, dialog.tag)
    }

    private fun imageLoadErrorDialog(msg : String){
        val constructor = AlertDialog(requireContext(), resources.displayMetrics.widthPixels)
        val dialog = constructor.createDialog(2, msg, "제시도", "취소")
        constructor.setItemClickListener(object : AlertDialog.AlertDialogClickListener{
            override fun onClickOk() {
                mViewModel.getInitialData()
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