package com.project.laybare.fragment.contact

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.project.laybare.R
import com.project.laybare.databinding.FragmentContactBinding
import com.project.laybare.dialog.ImageSelectDialog
import com.project.laybare.dialog.ImageSelectDialogListener
import com.project.laybare.ssot.ImageDetailData
import com.project.laybare.util.PermissionChecker
import com.project.laybare.util.PhotoTaker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
            mPhotoTaker.getPhotoUri()?.let {
                mViewModel.setProfileImage(it.toString())
                setProfileImage()
            }
        }
    }
    // 이미지 선택 다이얼로그에서 선택된 이미지 리스너
    private val mPickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let{
            mViewModel.setProfileImage(it.toString())
            setProfileImage()
        }
    }

    private val requestContactPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

        if (isGranted) {
            mViewModel.addContact(requireActivity().contentResolver, requireContext())
        } else {
            Snackbar.make(mBinding.root, "연락처를 저장하기 위해 권한이 필요해요", Snackbar.LENGTH_SHORT).show()
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

        mBinding.ContactName.addTextChangedListener { text ->
            mViewModel.setEditedName(text.toString())
        }

        mBinding.ContactAddBtn.setOnClickListener {
            if(PermissionChecker().checkContactPermission(requireActivity(), requestContactPermission)) {
                mViewModel.addContact(requireActivity().contentResolver, requireContext())
            }
        }

        mBinding.ContactSelectProfile.setOnClickListener {
            createImageSelectOptionDialog()
        }

        mViewModel.getNumberAdapter().setAdapterListener(object : ContactListListListener{
            override fun onContactSelected(contact: String) {
                mViewModel.contactSelected("number", contact)
            }
        })

        mViewModel.getEmailAdapter().setAdapterListener(object : ContactListListListener{
            override fun onContactSelected(contact: String) {
                mViewModel.contactSelected("email", contact)
            }
        })
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.mProgressVisibility.collectLatest {
                mBinding.ContactProgress.isVisible = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.mCreateSnackBar.collectLatest {
                Snackbar.make(mBinding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    private fun initUI() {
        Glide.with(requireContext())
            .load(mViewModel.getImageUrl())
            .into(mBinding.ContactImage)

        mBinding.ContactName.setText(mViewModel.getEditedName())
        mBinding.ContactProfileImage.clipToOutline = true

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

        setProfileImage()

        initListener()
        initObserver()
    }

    private fun setProfileImage() {
        val image = mViewModel.getProfileImage()
        if(image.isNotEmpty()){
            Glide.with(requireContext()).load(image).into(mBinding.ContactProfileImage)
        }else{
            Glide.with(requireContext()).load(R.drawable.empty_profile).into(mBinding.ContactProfileImage)
        }
    }

    private fun createImageSelectOptionDialog() {
        val dialog = ImageSelectDialog()
        dialog.setImageSelectDialogListener(object : ImageSelectDialogListener {
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}