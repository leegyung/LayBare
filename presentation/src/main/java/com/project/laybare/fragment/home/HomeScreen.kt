package com.project.laybare.fragment.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.project.domain.entity.HomeImageSectionEntity
import com.project.domain.entity.ImageEntity
import com.project.laybare.R
import com.project.laybare.ssot.ImageDetailData
import com.project.laybare.util.PhotoTaker


@Composable
fun HomeMainScreen(viewModel : HomeViewModel, navController: NavController) {

    val mContext = LocalContext.current
    val uiState by viewModel.container.stateFlow.collectAsState()
    val sideEffectFlow = viewModel.container.sideEffectFlow


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

    LaunchedEffect(Unit) {
        sideEffectFlow.collect{ sideEffect ->
            when(sideEffect){
                is HomeSideEffect.CreateDialog -> {}
            }
        }
    }



    HomeScreen(
        uiState = uiState
    ) {
        viewModel.onHandleEvent(it)
    }


}

@Composable
fun HomeScreen(uiState: HomeUiState, onHandleEvent : (event : HomeEvent) -> Unit) {
    val imageList = uiState.imageSections

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(imageList.count()) { i ->
                val section = imageList[i]

                if(section.sectionType == "Banner") {
                    HomeBannerHolder(section.imageList)
                }else {
                    HomeRegularImageHolder(section.imageList, section.keyword, i)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenTest() {
    val image = ImageEntity(
        "",
        "https://natureconservancy-h.assetsadobe.com/is/image/content/dam/tnc/nature/en/photos/w/o/WOPA160517_D056-resized.jpg?crop=864%2C0%2C1728%2C2304&wid=600&hei=800&scl=2.88",
        "",
        0,
        0,
        ""
    )

    val bannerSectionEntity = HomeImageSectionEntity(
        sectionType = "Banner",
        keyword = "Hello",
        imageList = listOf(image, image, image, image, image)
    )

    val normalSection = HomeImageSectionEntity(
        sectionType = "Normal",
        keyword = "Hello",
        imageList = listOf(image, image, image, image, image)
    )



    val state = HomeUiState(
        false,
        listOf(bannerSectionEntity, normalSection, normalSection, normalSection)
    )



    MaterialTheme {
        HomeScreen(state){

        }
    }

}