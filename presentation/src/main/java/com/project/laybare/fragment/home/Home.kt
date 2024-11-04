package com.project.laybare.fragment.home

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.project.laybare.R
import com.project.laybare.databinding.FragmentHomeBinding
import com.project.laybare.dialog.AlertDialog
import com.project.laybare.dialog.ImageSelectDialog
import com.project.laybare.dialog.ImageSelectDialogListener
import com.project.laybare.ssot.ImageDetailData
import com.project.laybare.util.PhotoTaker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Home : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel : HomeViewModel by viewModels()
    private lateinit var mNavController: NavController
    private val mPhotoTaker by lazy { PhotoTaker(requireContext()) }

    // 촬영 어플에서 찍은 사진 결과
    private val mTakePictureResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            ImageDetailData.setNewImageData(mPhotoTaker.getPhotoUri().toString())
            findNavController().navigate(R.id.action_home_to_imageDetail)
        }
    }
    // 앨범에서 선택한 사진 선택 결과
    private val mPickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            ImageDetailData.setNewImageData(uri.toString())
            findNavController().navigate(R.id.action_home_to_imageDetail)
        }
    }
    // 카메라 사용 권한 요청 결과
    private val mPermissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if(isGranted){
            mPhotoTaker.dispatchTakePictureIntent(mTakePictureResult)
        }else{
            Snackbar.make(mBinding.root, "사진 촬영을 위해 권한이 필요해요", Snackbar.LENGTH_SHORT).show()
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
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 다이얼로그 생성
                launch {
//                    mViewModel.mApiError.collectLatest {
//                        imageLoadErrorDialog(it)
//                    }
                }
                // 프로그레스 바 visibility 설정
                launch {
//                    mViewModel.mLoadingState.collectLatest {
//                        mBinding.HomeProgressBar.isVisible = it
//                    }
                }
            }
        }
    }



    private fun initListener(){
        // 사진 리스트 리스너
//        mViewModel.getHomeAdapter().setListener(object : HomeImageListListener{
//            override fun onImageClicked(image: String) {
//                ImageDetailData.setNewImageData(image)
//                findNavController().navigate(R.id.action_home_to_imageDetail)
//            }
//        })

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
//        initObserver()
//        initListener()
//
//
//        val layoutManager = GridLayoutManager(this.context, 2)
//        val adapter = mViewModel.getHomeAdapter()
//
//        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                return when (adapter.getItemViewType(position)) {
//                    3 -> 1
//                    else -> 2
//                }
//            }
//        }
//
//        mBinding.HomeRecyclerView.apply {
//            if(itemDecorationCount == 0){
//                addItemDecoration(HomeDecorator(resources.getDimensionPixelSize(R.dimen.dp_10)))
//            }
//            setHasFixedSize(true)
//            this.layoutManager =layoutManager
//            this.adapter = adapter
//        }
    }


    private fun createImageSelectOptionDialog() {
        val dialog = ImageSelectDialog()
        dialog.setImageSelectDialogListener(object : ImageSelectDialogListener{
            override fun onAlbumClicked() {
                mPickImage.launch("image/*")
            }
            override fun onCameraClicked() {
                val permissionGranted = mPhotoTaker.checkCameraPermission(requireContext(), mPermissionResult)
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