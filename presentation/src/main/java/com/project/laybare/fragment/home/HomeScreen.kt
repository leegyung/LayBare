package com.project.laybare.fragment.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.project.laybare.R
import com.project.laybare.ssot.ImageDetailData
import com.project.laybare.util.PhotoTaker


@Composable
fun HomeMainScreen(viewModel : HomeViewModel, navController: NavController) {

    val mContext = LocalContext.current
    val mPhotoTaker by lazy { PhotoTaker(mContext) }

    // 앨범에서 선탣한 사진 uri 결과
    val mSelectedAlbumImageResult = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {

            println(it.toString())

            //ImageDetailData.setNewImageData(uri.toString())
            //findNavController().navigate(R.id.action_home_to_imageDetail)
        }
    }

    // 촬영 어플에서 찍은 사진 결과
    val mTakePictureResult = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            //ImageDetailData.setNewImageData(mPhotoTaker.getPhotoUri().toString())
            //findNavController().navigate(R.id.action_home_to_imageDetail)
        }
    }
    // 카메라 사용 권한 요청 결과
    val mCameraPermissionResult = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if(isGranted){
            //mPhotoTaker.dispatchTakePictureIntent(requireContext(), mTakePictureResult)
        }else{
            //Snackbar.make(mBinding.root, "사진 촬영을 위해 권한이 필요해요", Snackbar.LENGTH_SHORT).show()
        }
    }









}

@Composable
fun HomeScreen() {




    Column(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenTest() {

    MaterialTheme {
        HomeScreen()
    }

}