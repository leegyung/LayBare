package com.project.laybare.fragment.similarImage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.project.domain.entity.ImageEntity
import com.project.domain.entity.ImageLabelEntity
import com.project.laybare.R
import com.project.laybare.dialog.SingleChoiceDialog
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun SimilarImageMainScreen(
    viewModel: SimilarImageViewModel,
    navController: NavController
) {
    val state by viewModel.container.stateFlow.collectAsState()
    val sideEffectFlow = viewModel.container.sideEffectFlow
    var showDialog by remember { mutableStateOf<SimilarImageSideEffect.ShowDialog?>(null) }


    // Side Effect 를 가져와서 flow로 관찰
    LaunchedEffect(Unit) {
        sideEffectFlow.collect { effect ->
            when(effect){
                is SimilarImageSideEffect.NavigateToImageDetail -> {
                    val navOption = NavOptions.Builder()
                        .setEnterAnim(R.anim.next_page_in_anim)
                        .setExitAnim(R.anim.previous_page_out_anim)
                        .setPopEnterAnim(R.anim.previous_page_in_anim)
                        .setPopExitAnim(R.anim.next_page_out_anim)
                        .build()
                    navController.navigate(R.id.imageDetail, null, navOption)
                }
                is SimilarImageSideEffect.PopBackstack -> {
                    navController.popBackStack()
                }

                is SimilarImageSideEffect.ShowDialog -> {
                    showDialog = effect
                }
            }
        }
    }


    SimilarImageScreen(
        state,
        onPerformEvent = {
            viewModel.processEvent(it)
        }
    )


    if(showDialog != null){
        val moveToPrevious = showDialog?.moveToPrevious?:false
        SingleChoiceDialog(
            content = showDialog?.message,
            btnText = showDialog?.option,
            onConfirmClicked = {
                showDialog = null
                if(moveToPrevious){
                    navController.popBackStack()
                }
            }
        )
    }



}



@Composable
fun SimilarImageScreen(
    uiState : SimilarImageState,
    onPerformEvent : (event : SimilarImageEvent) -> Unit
) {
    Column(
        Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(20.dp, 0.dp, 20.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = null,
                Modifier
                    .size(35.dp)
                    .clickable {
                        onPerformEvent(SimilarImageEvent.OnBackClicked)
                    }
            )

            Image(
                painter = painterResource(id = R.drawable.lay_bare),
                contentDescription = null,
                Modifier.size(54.dp, 26.dp)
            )
        }


        Spacer(modifier = Modifier.height(10.dp))

        KeywordList(uiState, onPerformEvent)

        Spacer(modifier = Modifier.height(20.dp))

        ImageList(uiState, onPerformEvent)
    }

    ProgressBar(uiState.isLoading)

}


@Composable
fun KeywordList(
    uiState : SimilarImageState,
    onPerformEvent : (event : SimilarImageEvent) -> Unit
) {

    val keyWordList = uiState.keyword

    LazyRow (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){

        itemsIndexed(keyWordList){ index, item ->
            if(index == 0) {
                Spacer(modifier = Modifier.size(20.dp))
            }

            KeywordBox(
                item,
                Modifier.clickable {
                    onPerformEvent(SimilarImageEvent.OnKeywordClicked(index))
                }
            )

            if(index == keyWordList.size - 1){
                Spacer(modifier = Modifier.size(20.dp))
            }

        }
    }
}



@Composable
fun KeywordBox(keyword : ImageLabelEntity, modifier: Modifier = Modifier){
    Surface(
        color = if(keyword.isSelected) colorResource(id = R.color.black) else colorResource(id = R.color.gray_bg),
        shape = RoundedCornerShape(15.dp), // 모서리 둥글게
    ) {
        Text(
            text = keyword.label,
            style = TextStyle(
                color = if(keyword.isSelected) colorResource(id = R.color.white) else colorResource(id = R.color.textBlack),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            ),
            modifier = modifier.padding(15.dp, 5.dp, 15.dp, 5.dp)
        )
    }
}


@Composable
fun ImageList(
    uiState : SimilarImageState,
    onPerformEvent : (event : SimilarImageEvent) -> Unit
) {

    val imagePagingItem = uiState.imageList.collectAsLazyPagingItems()

    when{

        imagePagingItem.loadState.refresh is LoadState.NotLoading -> {
            onPerformEvent(SimilarImageEvent.OnLoadingStateChanged(false))
        }

        imagePagingItem.loadState.refresh is LoadState.Error -> {
            val errorState = imagePagingItem.loadState.refresh as LoadState.Error
            onPerformEvent(SimilarImageEvent.OnPagingError(errorState.error.message?:""))
        }

        imagePagingItem.loadState.append is LoadState.Error -> {
            val errorState = imagePagingItem.loadState.append as LoadState.Error
            onPerformEvent(SimilarImageEvent.OnPagingError(errorState.error.message?:""))
        }
    }


    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp), // 열 사이의 간격 설정
        verticalArrangement = Arrangement.spacedBy(10.dp) // 행 사이의 간격 설정
    ) {
        items(imagePagingItem.itemCount) { index ->
            imagePagingItem[index]?.let{ image ->
                SimilarImageView(onPerformEvent, image)
            }
        }
    }



}




@Composable
fun SimilarImageView(
    onPerformEvent : (event : SimilarImageEvent) -> Unit,
    image : ImageEntity
){

    Column(
        Modifier.height(350.dp)
    ) {
        Surface(
            modifier = Modifier
                .clickable { onPerformEvent(SimilarImageEvent.OnImageClicked(image)) },
            color = colorResource(id = R.color.gray_bg),
            shape = RoundedCornerShape(8.dp), // 모서리 둥글게
        ) {


            GlideImage(
                modifier = Modifier
                    .fillMaxSize(),
                imageModel = { if(image.linkError) image.thumbnailLink else image.link },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    requestSize = IntSize(400, 800)
                ),
                loading = {},
                failure = {
                    // 썸네일로 한번도 로딩을 시도하지 않았다면
                    if(!image.linkError) {
                        image.linkError = true
                    }
                    GlideImage(
                        modifier = Modifier
                            .fillMaxSize(),
                        imageModel = { image.thumbnailLink },
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            requestSize = IntSize(400, 800)
                        )
                    )

                }
            )
        }
    }
}



@Composable
fun ProgressBar(isLoading : Boolean) {
    if(isLoading){
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.gray50), // 프로그래스 바 색상
                strokeWidth = 4.dp, // 두께
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center)
            )
        }
    }
}






@Preview(showBackground = true)
@Composable
fun ItemListPreview() {

    val state = SimilarImageState(
        keyword = arrayListOf(
            ImageLabelEntity("안녕", true),
            ImageLabelEntity("안녕", false),
            ImageLabelEntity("안녕", true),
        ),
        isLoading = true

    )


    MaterialTheme {
        SimilarImageScreen(
            state,
            onPerformEvent = {}
        )
    }
}





