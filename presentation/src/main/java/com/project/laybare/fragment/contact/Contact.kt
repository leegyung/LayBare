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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.project.laybare.R
import com.project.laybare.databinding.FragmentContactBinding
import com.project.laybare.dialog.AlertDialog
import com.project.laybare.dialog.ImageSelectDialog
import com.project.laybare.dialog.ImageSelectDialogListener
import com.project.laybare.util.ContactCreator
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

    // 카메라 어플에서 찍은 사진 결과
    private val mTakePictureResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            mPhotoTaker.getPhotoUri()?.let {
                mViewModel.setProfileImage(it.toString())
                setProfileImage()
            }
        }
    }
    // 앨범에서 선택한 사진 선택 결과
    private val mPickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let{
            mViewModel.setProfileImage(it.toString())
            setProfileImage()
        }
    }
    // 연락처 write 권한 요청 결과
    private val mContactPermissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            mViewModel.addContact(requireActivity().contentResolver, requireContext())
        } else {
            Snackbar.make(mBinding.root, "연락처를 저장하기 위해 권한이 필요해요", Snackbar.LENGTH_SHORT).show()
        }
    }
    // 카메라 사용 권한 요청 결과
    private val mCameraPermissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if(isGranted){
            mPhotoTaker.dispatchTakePictureIntent(requireContext(), mTakePictureResult)
        }else{
            Snackbar.make(mBinding.root, "사진 촬영을 위해 권한이 필요해요", Snackbar.LENGTH_SHORT).show()
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
        // 이름 입력 텍스트 리스너
        mBinding.ContactName.addTextChangedListener { text ->
            mViewModel.setEditedName(text.toString())
        }
        // 연락처 추가 버튼 리스너
        mBinding.ContactAddBtn.setOnClickListener {
            if(ContactCreator().checkContactPermission(requireActivity(), mContactPermissionResult)) {
                mViewModel.addContact(requireActivity().contentResolver, requireContext())
            }
        }
        // 프로필 사진 선택 버튼 리스너
        mBinding.ContactSelectProfile.setOnClickListener {
            createImageSelectOptionDialog()
        }
        // 전화 번호 리스트 리스너
        mViewModel.getNumberAdapter().setAdapterListener(object : ContactListListListener{
            override fun onContactSelected(contact: String) {
                mViewModel.contactSelected("number", contact)
            }
        })
        // 이메일 리스트 리스너
        mViewModel.getEmailAdapter().setAdapterListener(object : ContactListListListener{
            override fun onContactSelected(contact: String) {
                mViewModel.contactSelected("email", contact)
            }
        })
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 로딩 바 visibility 설정
                launch {
                    mViewModel.mProgressVisibility.collectLatest {
                        mBinding.ContactProgress.isVisible = it
                    }
                }
                // 스넥바 생성
                launch {
                    mViewModel.mCreateSnackBar.collectLatest {
                        Snackbar.make(mBinding.root, it, Snackbar.LENGTH_SHORT).show()
                    }
                }
                // 다이얼로그 생성
                launch {
                    mViewModel.mCreateDialog.collectLatest {
                        createDialog(it)
                    }
                }
            }
        }
    }


    private fun initUI() {
        Glide.with(requireContext())
            .load(mViewModel.getImageUrl())
            .into(mBinding.ContactImage)

        mBinding.ContactProfileImage.clipToOutline = true
        setProfileImage()

        mBinding.ContactName.setText(mViewModel.getEditedName())

        mBinding.ContactNumberList.apply {
            animation = null
            itemAnimator = null
            if(itemDecorationCount == 0){
                addItemDecoration(ContactListDecorator(resources.getDimensionPixelSize(R.dimen.dp_10)))
            }
            adapter = mViewModel.getNumberAdapter()
        }

        mBinding.ContactEmailList.apply {
            animation = null
            itemAnimator = null
            if(itemDecorationCount == 0){
                addItemDecoration(ContactListDecorator(resources.getDimensionPixelSize(R.dimen.dp_10)))
            }
            adapter = mViewModel.getEmailAdapter()
        }


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
                    mPhotoTaker = PhotoTaker()
                }

                val permissionGranted = mPhotoTaker.checkCameraPermission(requireContext(), mCameraPermissionResult)
                if(permissionGranted){
                    mPhotoTaker.dispatchTakePictureIntent(requireContext(), mTakePictureResult)
                }
            }
        })
        dialog.show(childFragmentManager, dialog.tag)
    }


    private fun createDialog(msg : String){
        val constructor = AlertDialog(requireContext(), resources.displayMetrics.widthPixels)
        val dialog = constructor.createDialog(1, msg, "확인")
        constructor.setItemClickListener(object : AlertDialog.AlertDialogClickListener{
            override fun onClickOk() {
                mNavController.popBackStack()
                dialog.dismiss()
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