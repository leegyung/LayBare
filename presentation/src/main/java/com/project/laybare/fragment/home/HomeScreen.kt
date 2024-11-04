package com.project.laybare.fragment.home

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.domain.entity.HomeImageSectionEntity
import com.project.domain.entity.ImageEntity
import com.project.laybare.R
import com.project.laybare.dialog.ImageSelectOptionDialog
import com.project.laybare.util.PhotoTaker


@Composable
fun HomeMainScreen(viewModel : HomeViewModel, navController: NavController) {

    val mContext = LocalContext.current
    val uiState by viewModel.container.stateFlow.collectAsState()
    val sideEffectFlow = viewModel.container.sideEffectFlow
    var showImageSelectOptDialog by remember { mutableStateOf(false) }

    val mPhotoTaker by lazy { PhotoTaker(mContext) }

    // 앨범에서 선탣한 사진 uri 결과
    val mSelectedAlbumImageResult = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            viewModel.onHandleEvent(HomeEvent.MoveToImageDetail(it.toString()))
        }
    }
    // 촬영 어플에서 찍은 사진 결과
    val mTakePictureResult = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            viewModel.onHandleEvent(HomeEvent.MoveToImageDetail(mPhotoTaker.getPhotoUri().toString()))
        }
    }
    // 카메라 사용 권한 요청 결과
    val mCameraPermissionResult = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if(isGranted){
            mPhotoTaker.dispatchTakePictureIntent(mTakePictureResult)
        }else{
            Toast.makeText(mContext, "사진 촬영을 위해 권한이 필요해요", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        sideEffectFlow.collect{ sideEffect ->
            when(sideEffect){
                is HomeSideEffect.CreateDialog -> Unit
                is HomeSideEffect.NavigateToSearchPage -> navController.navigate(R.id.action_home_to_search)
                is HomeSideEffect.NavigateToImageDetailPage -> navController.navigate(R.id.action_home_to_imageDetail)
                is HomeSideEffect.CreateImageSelectDialog -> showImageSelectOptDialog = true
                is HomeSideEffect.LaunchCamera -> {
                    if(mPhotoTaker.checkCameraPermission(mCameraPermissionResult)){
                        mPhotoTaker.dispatchTakePictureIntent(mTakePictureResult)
                    }
                }
                is HomeSideEffect.OpenAlbum -> mSelectedAlbumImageResult.launch("image/*")
            }
        }
    }



    HomeScreen(
        uiState = uiState
    ) {
        viewModel.onHandleEvent(it)
    }


    if(showImageSelectOptDialog){
        ImageSelectOptionDialog(
            onAlbumSelected = {
                viewModel.onHandleEvent(HomeEvent.SelectImageFromAlbumClicked)
                showImageSelectOptDialog = false
            },
            onCameraSelected = {
                viewModel.onHandleEvent(HomeEvent.TakePictureByCameraClicked)
                showImageSelectOptDialog = false
            },
            onCancel = {
                showImageSelectOptDialog = false
            }
        )
    }


}

@Composable
fun HomeScreen(uiState: HomeUiState, onHandleEvent : (event : HomeEvent) -> Unit) {
    val imageList = uiState.imageSections

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = R.drawable.lay_bare),
                contentDescription = null,
                modifier = Modifier
                    .width(112.dp)
                    .height(50.dp)
            )


            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .weight(1f)
                    .padding(10.dp, 0.dp, 10.dp, 0.dp)
                    .background(
                        color = colorResource(id = R.color.gray_bg),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .clickable {
                        onHandleEvent(HomeEvent.SearchBtnClicked)
                    }
            ){
                Image(
                    painter = painterResource(id = R.drawable.search_icon),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))
            }



            Image(
                painter = painterResource(id = R.drawable.camera_icon),
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        onHandleEvent(HomeEvent.SelectImageBtnClicked)
                    },
                contentDescription = null
            )
        }


        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(imageList.count()) { i ->
                val section = imageList[i]

                if(section.sectionType == "Banner") {
                    HomeBannerHolder(section.imageList, onHandleEvent)
                }else {
                    HomeRegularImageHolder(section.imageList, section.keyword, i, onHandleEvent)
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